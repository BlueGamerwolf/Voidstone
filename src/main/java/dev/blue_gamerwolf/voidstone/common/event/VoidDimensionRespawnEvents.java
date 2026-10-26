package dev.blue_gamerwolf.voidstone.common.event;

import dev.blue_gamerwolf.voidstone.VoidstoneMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Players who die in the Void Dimension respawn there instead of escaping to
 * the Overworld. Their ordinary inventory/keepInventory rules are unchanged.
 */
@Mod.EventBusSubscriber(modid = VoidstoneMod.MODID)
public final class VoidDimensionRespawnEvents {
    private static final String STRANDED_KEY = "voidstone_stranded_respawn";
    private static final String RETURN_PREFIX = "voidstone_return_";

    private VoidDimensionRespawnEvents() {
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof ServerPlayer player
                && player.level().dimension().equals(VoidstoneArmorEvents.VOID_DIMENSION)) {
            player.getPersistentData().putBoolean(STRANDED_KEY, true);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }

        var original = event.getOriginal().getPersistentData();
        var replacement = event.getEntity().getPersistentData();
        replacement.putBoolean(STRANDED_KEY, original.getBoolean(STRANDED_KEY));

        // Preserve the player's saved way home across death, without changing inventory rules.
        for (String suffix : new String[] {"dimension", "x", "y", "z", "yaw", "pitch"}) {
            String key = RETURN_PREFIX + suffix;
            if (original.contains(key)) {
                replacement.put(key, original.get(key).copy());
            }
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !player.getPersistentData().getBoolean(STRANDED_KEY)) {
            return;
        }

        ServerLevel voidLevel = player.server.getLevel(VoidstoneArmorEvents.VOID_DIMENSION);
        if (voidLevel == null) {
            // Keep the marker so a later respawn can retry if the dimension becomes available.
            return;
        }

        BlockPos spawn = findSafeSpawn(voidLevel);
        player.teleportTo(voidLevel, spawn.getX() + 0.5D, spawn.getY(),
                spawn.getZ() + 0.5D, java.util.Set.of(), player.getYRot(), 0.0F);
        player.fallDistance = 0.0F;
        player.getPersistentData().remove(STRANDED_KEY);
    }

    private static BlockPos findSafeSpawn(ServerLevel level) {
        int x = 0;
        int z = 0;
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        BlockPos feet = new BlockPos(x, y, z);
        if (level.getBlockState(feet.below()).isSolid()
                && level.getBlockState(feet).isAir()
                && level.getBlockState(feet.above()).isAir()) {
            return feet;
        }
        return new BlockPos(0, Math.max(100, level.getMinBuildHeight() + 16), 0);
    }
}
