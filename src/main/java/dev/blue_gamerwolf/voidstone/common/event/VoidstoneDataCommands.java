package dev.blue_gamerwolf.voidstone.common.event;

import com.mojang.brigadier.Command;
import dev.blue_gamerwolf.voidstone.data.VoidstonePlayerData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletionException;

@Mod.EventBusSubscriber(modid = "voidstone")
public final class VoidstoneDataCommands {
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z").withZone(ZoneId.systemDefault());

    private VoidstoneDataCommands() {}

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("voidstone")
                .then(Commands.literal("data")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("refresh")
                                .executes(context -> {
                                    context.getSource().sendSuccess(
                                            () -> Component.literal("Refreshing Voidstone-Data from GitHub...")
                                                    .withStyle(ChatFormatting.YELLOW), true);

                                    VoidstonePlayerData.getInstance().refreshAsync().whenComplete((count, failure) -> {
                                        if (failure != null) {
                                            Throwable cause = failure instanceof CompletionException
                                                    && failure.getCause() != null
                                                    ? failure.getCause() : failure;
                                            System.err.println("[Voidstone] Voidstone-Data refresh failed: "
                                                    + cause.getMessage());
                                            context.getSource().getServer().execute(() ->
                                                    context.getSource().sendFailure(Component.literal(
                                                            "Voidstone-Data refresh failed. The previous cache was kept. "
                                                                    + cause.getMessage())));
                                            return;
                                        }

                                        context.getSource().getServer().execute(() ->
                                                context.getSource().sendSuccess(
                                                        () -> Component.literal("Voidstone-Data refreshed: "
                                                                + count + " player links loaded.")
                                                                .withStyle(ChatFormatting.GREEN), true));
                                    });
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("data-status")
                                .executes(context -> {
                                    VoidstonePlayerData data = VoidstonePlayerData.getInstance();
                                    long lastRefresh = data.getLastSuccessfulRefreshEpochMillis();
                                    String status = lastRefresh == 0L ? "Never refreshed"
                                            : TIME_FORMAT.format(Instant.ofEpochMilli(lastRefresh));
                                    context.getSource().sendSuccess(
                                            () -> Component.literal("Voidstone-Data cache: "
                                                    + data.getCachedPlayerCount() + " links; last successful refresh: "
                                                    + status), false);
                                    return Command.SINGLE_SUCCESS;
                                })));
    }
}
