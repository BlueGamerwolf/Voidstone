package dev.blue_gamerwolf.voidstone.worldgen;

import dev.blue_gamerwolf.voidstone.common.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class VoidTowerFeature extends Feature<NoneFeatureConfiguration> {
    public VoidTowerFeature() { super(NoneFeatureConfiguration.CODEC); }
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        if (!level.getBlockState(origin.below()).is(ModBlocks.NULLSTONE.get())) return false;
        int height = 6 + context.random().nextInt(7);
        for (int y = 0; y < height; y++) {
            for (int x = -1; x <= 1; x++) for (int z = -1; z <= 1; z++) {
                if (Math.abs(x) == 1 || Math.abs(z) == 1 || y == 0 || y == height - 1) {
                    BlockPos p = origin.offset(x, y, z);
                    if (level.getBlockState(p).isAir()) level.setBlock(p, ModBlocks.VOID_RUIN_BRICKS.get().defaultBlockState(), 3);
                }
            }
        }
        for (int y = 1; y < height - 1; y++) {
            BlockPos p = origin.above(y);
            if (level.getBlockState(p).isAir()) level.setBlock(p, ModBlocks.VOID_CRYSTAL.get().defaultBlockState(), 3);
        }
        return true;
    }
}
