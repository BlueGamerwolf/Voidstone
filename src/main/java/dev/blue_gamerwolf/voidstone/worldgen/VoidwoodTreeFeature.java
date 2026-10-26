package dev.blue_gamerwolf.voidstone.worldgen;

import dev.blue_gamerwolf.voidstone.common.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class VoidwoodTreeFeature extends Feature<NoneFeatureConfiguration> {
    public VoidwoodTreeFeature() { super(NoneFeatureConfiguration.CODEC); }
    @Override public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        if (!level.getBlockState(origin.below()).is(ModBlocks.NULLSTONE.get()) || !level.getBlockState(origin).isAir()) return false;
        int height = 5 + random.nextInt(4);
        for (int y = 0; y < height; y++) {
            BlockPos p = origin.above(y);
            if (!level.getBlockState(p).isAir() && !level.getBlockState(p).is(ModBlocks.VOID_LEAVES.get())) return false;
        }
        for (int y = 0; y < height; y++) level.setBlock(origin.above(y), ModBlocks.VOIDWOOD_LOG.get().defaultBlockState(), 3);
        BlockPos crown = origin.above(height - 1);
        for (int dy = -2; dy <= 2; dy++) for (int dx = -2; dx <= 2; dx++) for (int dz = -2; dz <= 2; dz++) {
            int distance = Math.abs(dx) + Math.abs(dz) + Math.abs(dy);
            if (distance > 4 || (distance == 4 && random.nextBoolean())) continue;
            BlockPos leaf = crown.offset(dx, dy, dz);
            if (level.getBlockState(leaf).isAir()) level.setBlock(leaf, ModBlocks.VOID_LEAVES.get().defaultBlockState(), 3);
        }
        if (random.nextInt(3) == 0) {
            BlockPos crystal = crown.above(2);
            if (level.getBlockState(crystal).isAir()) level.setBlock(crystal, ModBlocks.VOID_CRYSTAL.get().defaultBlockState(), 3);
        }
        return true;
    }
}
