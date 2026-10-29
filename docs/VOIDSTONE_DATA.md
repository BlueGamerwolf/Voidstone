# Voidstone-Data integration

Voidstone Core exposes the public account-link records maintained in
[BlueGamerwolf/Voidstone-Data](https://github.com/BlueGamerwolf/Voidstone-Data).

## Commands

These commands require permission level 2 (operator):

- `/voidstone data refresh` — explicitly fetch and validate the public `players/` records. The command runs network I/O on a background thread.
- `/voidstone data status` — report the number of cached links and the last successful refresh.

No network request is made automatically on startup. If a refresh fails, the previous cache remains available.

## Addon API (Java 17)

```java
import dev.blue_gamerwolf.voidstone.data.VoidstonePlayerData;
import dev.blue_gamerwolf.voidstone.data.VoidstonePlayerLink;

VoidstonePlayerData links = VoidstonePlayerData.getInstance();

// Look up a player's linked Discord user ID from the in-memory cache.
links.findDiscordUid(player.getUUID()).ifPresent(discordUid -> {
    // Use the linked ID as account-link metadata.
});

// Or retrieve the complete validated record.
links.findByMinecraftUuid(player.getUUID()).ifPresent((VoidstonePlayerLink link) -> {
    String discordUid = link.getDiscordUid();
});
```

A lookup never performs network I/O. Call `refreshAsync()` when your application needs to refresh data explicitly; it returns a `CompletableFuture<Integer>` with the number of loaded links.

## Validation and failure behavior

- Records must contain exactly the two string fields `minecraft_uuid` and `discord_uid`.
- Minecraft UUIDs must parse and be in canonical UUID form.
- Discord IDs must contain 17–20 digits.
- Duplicate Minecraft UUIDs fail the entire refresh rather than choosing an ambiguous record.
- Only HTTPS raw URLs from the configured repository and branch are accepted for player records.
- A failed request or invalid record does not replace the last successful cache.

This repository contains account-link metadata only. A Discord ID link does not authenticate a Discord user and must not be used by itself as an authorization or ownership proof.
