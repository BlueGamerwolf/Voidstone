package dev.blue_gamerwolf.voidstone.common.block;

import dev.blue_gamerwolf.voidstone.common.event.VoidstoneArmorEvents;
import dev.blue_gamerwolf.voidstone.common.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A damaged gateway which must be repaired with a Void Sentinel Core.
 * Once activated, it can return players to their individually saved safe entry point.
 */
public class LostGatewayBlock extends Block {
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public LostGatewayBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(ACTIVATED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }

        if (!state.getValue(ACTIVATED)) {
            ItemStack held = player.getItemInHand(hand);
            if (!held.is(ModItems.VOID_SENTINEL_CORE.get())) {
                return InteractionResult.PASS;
            }

            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
            level.setBlock(pos, state.setValue(ACTIVATED, true), 3);
            level.levelEvent(2001, pos, Block.getId(state));
            return InteractionResult.CONSUME;
        }

        return VoidstoneArmorEvents.returnPlayer(serverPlayer)
                ? InteractionResult.CONSUME
                : InteractionResult.PASS;
    }
}
