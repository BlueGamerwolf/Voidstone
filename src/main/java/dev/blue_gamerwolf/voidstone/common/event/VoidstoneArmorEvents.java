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
 * Handles the one-way fall trigger into the Void Dimension.
 *
 * Entry requires vanilla Netherite armor with the Dimensional enchantment on
 * every armor piece. Voidstone armor is intentionally unrelated to entry.
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
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) {
            return;
        }

        if (!(event.player instanceof ServerPlayer player)
                || player.level().dimension().equals(VOID_DIMENSION)
                || player.getY() >= VOID_TRIGGER_Y
                || !hasDimensionalNetherite(player)) {
            return;
        }

        ServerLevel destination = player.server.getLevel(VOID_DIMENSION);
        if (destination == null) {
            // A missing/invalid dimension datapack should never delete the player.
            return;
        }

        CompoundTag data = player.getPersistentData();
        data.putString(DATA_PREFIX + "dimension", player.level().dimension().location().toString());
        data.putDouble(DATA_PREFIX + "x", player.getX());
        data.putDouble(DATA_PREFIX + "y", player.getY());
        data.putDouble(DATA_PREFIX + "z", player.getZ());
        data.putFloat(DATA_PREFIX + "yaw", player.getYRot());
        data.putFloat(DATA_PREFIX + "pitch", player.getXRot());

        BlockPos spawn = findSafeSpawn(destination);
        player.fallDistance = 0.0F;
        player.teleportTo(destination,
                spawn.getX() + 0.5D, spawn.getY(), spawn.getZ() + 0.5D,
                Set.of(), player.getYRot(), 0.0F);
        player.fallDistance = 0.0F;
        player.clearFire();
    }

    private static boolean hasDimensionalNetherite(ServerPlayer player) {
        return hasDimensionalPiece(player.getItemBySlot(EquipmentSlot.HEAD), Items.NETHERITE_HELMET)
                && hasDimensionalPiece(player.getItemBySlot(EquipmentSlot.CHEST), Items.NETHERITE_CHESTPLATE)
                && hasDimensionalPiece(player.getItemBySlot(EquipmentSlot.LEGS), Items.NETHERITE_LEGGINGS)
                && hasDimensionalPiece(player.getItemBySlot(EquipmentSlot.FEET), Items.NETHERITE_BOOTS);
    }

    private static boolean hasDimensionalPiece(ItemStack stack, net.minecraft.world.item.Item expectedItem) {
        return stack.is(expectedItem)
                && EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.DIMENSIONAL.get(), stack) >= 1;
    }

    private static BlockPos findSafeSpawn(ServerLevel level) {
        // Search nearby columns for a solid floor with two air blocks above it.
        for (int radius = 0; radius <= 32; radius += 4) {
            for (int x = -radius; x <= radius; x += Math.max(1, radius)) {
                for (int z = -radius; z <= radius; z += Math.max(1, radius)) {
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
        // The dimension is intended to have terrain; this fallback is above the void.
        return new BlockPos(0, Math.max(100, level.getMinBuildHeight() + 16), 0);
    }
}
