package dev.blue_gamerwolf.voidstone.worldgen;

import dev.blue_gamerwolf.voidstone.common.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class VoidRuinFeature extends Feature<NoneFeatureConfiguration> {
    public VoidRuinFeature() { super(NoneFeatureConfiguration.CODEC); }
    @Override public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        if (!level.getBlockState(origin.below()).is(ModBlocks.NULLSTONE.get())) return false;
        int radius = 4;
        for (int x = -radius; x <= radius; x++) for (int z = -radius; z <= radius; z++) {
            if (Math.abs(x) == radius || Math.abs(z) == radius || (Math.abs(x) <= 1 && Math.abs(z) <= 1)) {
                if (context.random().nextFloat() > 0.16F) {
                    BlockPos p = origin.offset(x, 0, z);
                    if (level.getBlockState(p).isAir()) level.setBlock(p, ModBlocks.VOID_RUIN_BRICKS.get().defaultBlockState(), 3);
                }
            }
        }
        for (int[] corner : new int[][]{{-4,-4},{-4,4},{4,-4},{4,4}}) {
            int h = 2 + context.random().nextInt(4);
            for (int y = 0; y < h; y++) {
                BlockPos p = origin.offset(corner[0], y, corner[1]);
                if (level.getBlockState(p).isAir()) level.setBlock(p, ModBlocks.VOID_RUIN_BRICKS.get().defaultBlockState(), 3);
            }
        }
        if (level.getBlockState(origin.above()).isAir()) level.setBlock(origin.above(), ModBlocks.VOID_CRYSTAL.get().defaultBlockState(), 3);
        return true;
    }
}
