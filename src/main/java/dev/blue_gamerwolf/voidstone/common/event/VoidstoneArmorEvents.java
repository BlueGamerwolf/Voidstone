package dev.blue_gamerwolf.voidstone.common.event;

import dev.blue_gamerwolf.voidstone.common.item.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "voidstone")
public class VoidstoneArmorEvents {

    private static final double VOID_HEIGHT = -64.0D;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Run once per tick, at the end of the player's tick.
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Player player = event.player;

        if (player.level().isClientSide()) {
            return;
        }

        if (!hasFullVoidstoneArmor(player)) {
            return;
        }

        if (player.getY() < VOID_HEIGHT) {
            if (!(player.level() instanceof ServerLevel level)) {
                return;
            }

            BlockPos safePos = findSafePosition(
                    level,
                    player.blockPosition());

            if (safePos != null) {
                if (level.isEmptyBlock(safePos.below())) {
                    level.setBlock(
                            safePos.below(),
                            Blocks.OBSIDIAN.defaultBlockState(),
                            3);
                }

                player.teleportTo(
                        safePos.getX() + 0.5D,
                        safePos.getY(),
                        safePos.getZ() + 0.5D);

                player.fallDistance = 0.0F;
                player.setHealth(player.getMaxHealth());
                player.clearFire();
            }
        }
    }

    private static BlockPos findSafePosition(
            ServerLevel level,
            BlockPos start) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(
                start.getX(),
                level.getMaxBuildHeight() - 1,
                start.getZ());

        while (pos.getY() > level.getMinBuildHeight()) {
            if (!level.isEmptyBlock(pos)) {
                return pos.above().immutable();
            }

            pos.move(0, -1, 0);
        }

        return level.getSharedSpawnPos();
    }

    private static boolean hasFullVoidstoneArmor(Player player) {
        ItemStack helmet = player.getInventory().getArmor(3);
        ItemStack chest = player.getInventory().getArmor(2);
        ItemStack legs = player.getInventory().getArmor(1);
        ItemStack boots = player.getInventory().getArmor(0);

        return helmet.is(ModItems.VOIDSTONE_HELMET.get())
                && chest.is(ModItems.VOIDSTONE_CHESTPLATE.get())
                && legs.is(ModItems.VOIDSTONE_LEGGINGS.get())
                && boots.is(ModItems.VOIDSTONE_BOOTS.get());
    }

}
