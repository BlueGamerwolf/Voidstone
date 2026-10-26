package dev.blue_gamerwolf.voidstone.data;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * A validated link from a Minecraft account UUID to a Discord user ID.
 *
 * <p>This record represents public account-link data only. It does not prove
 * Discord identity or grant permissions.</p>
 */
public final class VoidstonePlayerLink {
    private static final Pattern DISCORD_ID = Pattern.compile("^[0-9]{17,20}$");

    private final UUID minecraftUuid;
    private final String discordUid;

    public VoidstonePlayerLink(UUID minecraftUuid, String discordUid) {
        this.minecraftUuid = Objects.requireNonNull(minecraftUuid, "minecraftUuid");
        if (discordUid == null || !DISCORD_ID.matcher(discordUid).matches()) {
            throw new IllegalArgumentException("Invalid Discord user ID");
        }
        this.discordUid = discordUid;
    }

    public UUID getMinecraftUuid() {
        return minecraftUuid;
    }

    public String getDiscordUid() {
        return discordUid;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof VoidstonePlayerLink)) return false;
        VoidstonePlayerLink that = (VoidstonePlayerLink) other;
        return minecraftUuid.equals(that.minecraftUuid) && discordUid.equals(that.discordUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minecraftUuid, discordUid);
    }

    @Override
    public String toString() {
        return "VoidstonePlayerLink{minecraftUuid=" + minecraftUuid + ", discordUid='" + discordUid + "'}";
    }
}
