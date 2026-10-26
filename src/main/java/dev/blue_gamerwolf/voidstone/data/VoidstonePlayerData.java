package dev.blue_gamerwolf.voidstone.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 * Client for the public BlueGamerwolf/Voidstone-Data repository.
 *
 * <p>Nothing is fetched automatically at startup. Call {@link #refreshAsync()}
 * explicitly from a command or another API consumer. A failed refresh never
 * replaces the last successfully loaded cache.</p>
 */
public final class VoidstonePlayerData {
    public static final String REPOSITORY = "BlueGamerwolf/Voidstone-Data";
    public static final String BRANCH = "main";
    private static final String PLAYERS_API =
            "https://api.github.com/repos/" + REPOSITORY + "/contents/players?ref=" + BRANCH;
    private static final Pattern DISCORD_ID = Pattern.compile("^[0-9]{17,20}$");
    private static final int CONNECT_TIMEOUT_MS = 8000;
    private static final int READ_TIMEOUT_MS = 12000;
    private static final int MAX_RESPONSE_BYTES = 2 * 1024 * 1024;

    private static final VoidstonePlayerData INSTANCE = new VoidstonePlayerData();

    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "Voidstone-PlayerData");
        thread.setDaemon(true);
        return thread;
    });
    private final AtomicReference<Map<UUID, VoidstonePlayerLink>> cache =
            new AtomicReference<>(Collections.emptyMap());
    private volatile long lastSuccessfulRefreshEpochMillis = 0L;

    private VoidstonePlayerData() {}

    public static VoidstonePlayerData getInstance() {
        return INSTANCE;
    }

    /** Returns a cached link, if the most recently loaded data contains one. */
    public Optional<VoidstonePlayerLink> findByMinecraftUuid(UUID minecraftUuid) {
        if (minecraftUuid == null) return Optional.empty();
        return Optional.ofNullable(cache.get().get(minecraftUuid));
    }

    /** Convenience lookup for the linked Discord user ID. */
    public Optional<String> findDiscordUid(UUID minecraftUuid) {
        return findByMinecraftUuid(minecraftUuid).map(VoidstonePlayerLink::getDiscordUid);
    }

    /** Immutable snapshot of the current cache. */
    public Map<UUID, VoidstonePlayerLink> getCachedLinks() {
        return cache.get();
    }

    public int getCachedPlayerCount() {
        return cache.get().size();
    }

    /** Epoch milliseconds of the last successful refresh, or zero if never refreshed. */
    public long getLastSuccessfulRefreshEpochMillis() {
        return lastSuccessfulRefreshEpochMillis;
    }

    /**
     * Explicitly downloads and validates all player records on a background
     * thread. The returned future completes with the number of unique links.
     */
    public CompletableFuture<Integer> refreshAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<UUID, VoidstonePlayerLink> loaded = downloadAndValidate();
                Map<UUID, VoidstonePlayerLink> immutable =
                        Collections.unmodifiableMap(new HashMap<>(loaded));
                cache.set(immutable);
                lastSuccessfulRefreshEpochMillis = System.currentTimeMillis();
                return immutable.size();
            } catch (IOException exception) {
                throw new java.util.concurrent.CompletionException(exception);
            }
        }, executor);
    }

    private Map<UUID, VoidstonePlayerLink> downloadAndValidate() throws IOException {
        JsonElement listingJson = requestJson(PLAYERS_API);
        if (!listingJson.isJsonArray()) {
            throw new IOException("GitHub players endpoint did not return a JSON array.");
        }

        JsonArray listing = listingJson.getAsJsonArray();
        Map<UUID, VoidstonePlayerLink> links = new HashMap<>();
        for (JsonElement entry : listing) {
            if (!entry.isJsonObject()) continue;
            JsonObject file = entry.getAsJsonObject();
            if (!file.has("type") || !"file".equals(file.get("type").getAsString())) continue;
            if (!file.has("name") || !file.has("download_url")) continue;

            String name = file.get("name").getAsString();
            if (!name.endsWith(".json")) continue;

            String downloadUrl = file.get("download_url").getAsString();
            if (!downloadUrl.startsWith("https://raw.githubusercontent.com/"
                    + REPOSITORY + "/" + BRANCH + "/players/")) {
                throw new IOException("Refusing unexpected player record URL: " + downloadUrl);
            }

            JsonElement recordJson = requestJson(downloadUrl);
            VoidstonePlayerLink link = parseRecord(recordJson, name);
            VoidstonePlayerLink previous = links.putIfAbsent(link.getMinecraftUuid(), link);
            if (previous != null) {
                throw new IOException("Duplicate Minecraft UUID found in players/: "
                        + link.getMinecraftUuid() + ". Cache was not changed.");
            }
        }
        return links;
    }

    private VoidstonePlayerLink parseRecord(JsonElement element, String filename) throws IOException {
        if (element == null || !element.isJsonObject()) {
            throw new IOException("Player record " + filename + " is not a JSON object.");
        }

        JsonObject object = element.getAsJsonObject();
        if (object.size() != 2 || !object.has("minecraft_uuid") || !object.has("discord_uid")
                || !object.get("minecraft_uuid").isJsonPrimitive()
                || !object.get("discord_uid").isJsonPrimitive()
                || !object.get("minecraft_uuid").getAsJsonPrimitive().isString()
                || !object.get("discord_uid").getAsJsonPrimitive().isString()) {
            throw new IOException("Player record " + filename
                    + " must contain exactly minecraft_uuid and discord_uid string fields.");
        }

        final UUID uuid;
        try {
            String rawUuid = object.get("minecraft_uuid").getAsString();
            uuid = UUID.fromString(rawUuid);
            if (!uuid.toString().equalsIgnoreCase(rawUuid)) {
                throw new IllegalArgumentException("UUID is not in canonical form");
            }
        } catch (IllegalArgumentException exception) {
            throw new IOException("Invalid minecraft_uuid in " + filename, exception);
        }

        String discordUid = object.get("discord_uid").getAsString();
        if (!DISCORD_ID.matcher(discordUid).matches()) {
            throw new IOException("Invalid discord_uid in " + filename);
        }

        return new VoidstonePlayerLink(uuid, discordUid);
    }

    private JsonElement requestJson(String address) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(address).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
            connection.setReadTimeout(READ_TIMEOUT_MS);
            connection.setRequestProperty("Accept", "application/vnd.github+json");
            connection.setRequestProperty("User-Agent", "Voidstone-Core/1.0.0");
            connection.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");

            int status = connection.getResponseCode();
            if (status < 200 || status >= 300) {
                throw new IOException("HTTP " + status + " while fetching Voidstone-Data. "
                        + "GitHub may be unavailable or rate-limiting requests.");
            }

            try (InputStream input = connection.getInputStream()) {
                byte[] bytes = readLimited(input, MAX_RESPONSE_BYTES);
                return JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8));
            } catch (RuntimeException exception) {
                throw new IOException("Invalid JSON returned by Voidstone-Data: " + address, exception);
            }
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    private byte[] readLimited(InputStream input, int maxBytes) throws IOException {
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int total = 0;
        int read;
        while ((read = input.read(buffer)) != -1) {
            total += read;
            if (total > maxBytes) throw new IOException("Voidstone-Data response exceeded size limit.");
            output.write(buffer, 0, read);
        }
        return output.toByteArray();
    }
}
