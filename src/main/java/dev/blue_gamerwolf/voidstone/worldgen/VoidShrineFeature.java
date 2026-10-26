package dev.blue_gamerwolf.voidstone.worldgen;

import dev.blue_gamerwolf.voidstone.common.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class VoidShrineFeature extends Feature<NoneFeatureConfiguration> {
    public VoidShrineFeature() { super(NoneFeatureConfiguration.CODEC); }
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        if (!level.getBlockState(origin.below()).is(ModBlocks.NULLSTONE.get())) return false;
        for (int x = -3; x <= 3; x++) for (int z = -3; z <= 3; z++) {
            if (Math.abs(x) + Math.abs(z) <= 5) {
                BlockPos p = origin.offset(x, 0, z);
                if (level.getBlockState(p).isAir()) level.setBlock(p, ModBlocks.VOID_RUIN_BRICKS.get().defaultBlockState(), 3);
            }
        }
        for (int[] corner : new int[][]{{-2,-2},{-2,2},{2,-2},{2,2}}) {
            BlockPos p = origin.offset(corner[0], 1, corner[1]);
            if (level.getBlockState(p).isAir()) level.setBlock(p, ModBlocks.VOID_CRYSTAL.get().defaultBlockState(), 3);
        }
        for (int y = 1; y <= 3; y++) {
            BlockPos p = origin.above(y);
            if (level.getBlockState(p).isAir()) level.setBlock(p, ModBlocks.VOID_CRYSTAL.get().defaultBlockState(), 3);
        }
        return true;
    }
}
