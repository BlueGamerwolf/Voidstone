package dev.blue_gamerwolf.voidstone.common.event;

import dev.blue_gamerwolf.voidstone.VoidstoneMod;
import dev.blue_gamerwolf.voidstone.common.enchantment.ModEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

/**
 * Handles entry into and return from the Void Dimension.
 * Entry requires vanilla Netherite armor with Dimensional on all four pieces.
 * Voidstone armor is deliberately not an entry requirement.
 */
@Mod.EventBusSubscriber(modid = VoidstoneMod.MODID)
public final class VoidstoneArmorEvents {
    public static final ResourceKey<Level> VOID_DIMENSION = ResourceKey.create(
            Registries.DIMENSION, new ResourceLocation(VoidstoneMod.MODID, "void_dimension"));

    private static final String DATA_PREFIX = "voidstone_return_";
    private static final double VOID_TRIGGER_Y = -64.0D;

    private VoidstoneArmorEvents() {
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()
                || !(event.player instanceof ServerPlayer player)) {
            return;
        }

        // Keep the last grounded position so returning never sends a player back into the void.
        if (!player.level().dimension().equals(VOID_DIMENSION) && player.onGround()
                && player.getY() >= player.level().getMinBuildHeight() + 2) {
            saveReturnPoint(player);
            return;
        }

        if (player.level().dimension().equals(VOID_DIMENSION)
                || player.getY() >= VOID_TRIGGER_Y
                || !hasDimensionalNetherite(player)) {
            return;
        }

        ServerLevel destination = player.server.getLevel(VOID_DIMENSION);
        if (destination == null) {
            // If the dimension failed to load, leave vanilla void damage behavior untouched.
            return;
        }

        // Preserve the most recent grounded return point, rather than the falling coordinates.
        BlockPos spawn = findSafeSpawn(destination);
        player.fallDistance = 0.0F;
        player.teleportTo(destination, spawn.getX() + 0.5D, spawn.getY(),
                spawn.getZ() + 0.5D, Set.of(), player.getYRot(), 0.0F);
        player.fallDistance = 0.0F;
        player.clearFire();
    }

    private static void saveReturnPoint(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        data.putString(DATA_PREFIX + "dimension", player.level().dimension().location().toString());
        data.putDouble(DATA_PREFIX + "x", player.getX());
        data.putDouble(DATA_PREFIX + "y", player.getY());
        data.putDouble(DATA_PREFIX + "z", player.getZ());
        data.putFloat(DATA_PREFIX + "yaw", player.getYRot());
        data.putFloat(DATA_PREFIX + "pitch", player.getXRot());
    }

    /**
     * Called by an activated Lost Gateway. Returns false if the player has no saved
     * safe entry point or the original dimension is unavailable.
     */
    public static boolean returnPlayer(ServerPlayer player) {
        if (!player.level().dimension().equals(VOID_DIMENSION)) {
            return false;
        }

        CompoundTag data = player.getPersistentData();
        ResourceLocation location = ResourceLocation.tryParse(data.getString(DATA_PREFIX + "dimension"));
        if (location == null || !data.contains(DATA_PREFIX + "x")
                || !data.contains(DATA_PREFIX + "y") || !data.contains(DATA_PREFIX + "z")) {
            return false;
        }

        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, location);
        ServerLevel destination = player.server.getLevel(dimensionKey);
        if (destination == null) {
            return false;
        }

        double x = data.getDouble(DATA_PREFIX + "x");
        double y = data.getDouble(DATA_PREFIX + "y");
        double z = data.getDouble(DATA_PREFIX + "z");
        float yaw = data.getFloat(DATA_PREFIX + "yaw");
        float pitch = data.getFloat(DATA_PREFIX + "pitch");

        player.fallDistance = 0.0F;
        player.teleportTo(destination, x, y, z, Set.of(), yaw, pitch);
        player.fallDistance = 0.0F;
        player.clearFire();
        return true;
    }

    private static boolean hasDimensionalNetherite(ServerPlayer player) {
        return hasDimensionalPiece(player.getItemBySlot(EquipmentSlot.HEAD), Items.NETHERITE_HELMET)
                && hasDimensionalPiece(player.getItemBySlot(EquipmentSlot.CHEST), Items.NETHERITE_CHESTPLATE)
                && hasDimensionalPiece(player.getItemBySlot(EquipmentSlot.LEGS), Items.NETHERITE_LEGGINGS)
                && hasDimensionalPiece(player.getItemBySlot(EquipmentSlot.FEET), Items.NETHERITE_BOOTS);
    }

    private static boolean hasDimensionalPiece(ItemStack stack, Item expectedItem) {
        return stack.is(expectedItem)
                && EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.DIMENSIONAL.get(), stack) >= 1;
    }

    private static BlockPos findSafeSpawn(ServerLevel level) {
        for (int radius = 0; radius <= 32; radius += 4) {
            int step = Math.max(1, radius);
            for (int x = -radius; x <= radius; x += step) {
                for (int z = -radius; z <= radius; z += step) {
                    int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                    BlockPos feet = new BlockPos(x, surfaceY, z);
                    if (level.getBlockState(feet.below()).isSolid()
                            && level.getBlockState(feet).isAir()
                            && level.getBlockState(feet.above()).isAir()) {
                        return feet;
                    }
                }
            }
        }
        return new BlockPos(0, Math.max(100, level.getMinBuildHeight() + 16), 0);
    }
}
