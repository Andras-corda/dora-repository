package com.qbits.dora.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.HashMap;
import java.util.Map;

public class PondFeature extends Feature<NoneFeatureConfiguration> {
    private static final float LILY_PAD_CHANCE = 0.2F;
    private static final float SUGAR_CANE_CHANCE = 0.1F;

    public PondFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos center = context.origin().below();
        int rx = 2 + random.nextInt(3);
        int rz = 2 + random.nextInt(3);

        Map<BlockPos, Integer> cells = new HashMap<>();
        for (int dx = -rx; dx <= rx; dx++) {
            for (int dz = -rz; dz <= rz; dz++) {
                double d = (double) (dx * dx) / (rx * rx) + (double) (dz * dz) / (rz * rz);
                if (d <= 1.0 - random.nextDouble() * 0.25) {
                    cells.put(center.offset(dx, 0, dz), d < 0.35 ? 2 : 1);
                }
            }
        }

        for (BlockPos cell : cells.keySet()) {
            if (!level.getBlockState(cell).is(BlockTags.DIRT) || !level.getBlockState(cell.above()).canBeReplaced()) {
                return false;
            }
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos edge = cell.relative(dir);
                if (!cells.containsKey(edge) && !level.getBlockState(edge).isSolid()) {
                    return false;
                }
            }
        }

        BlockState water = Blocks.WATER.defaultBlockState();
        for (Map.Entry<BlockPos, Integer> entry : cells.entrySet()) {
            BlockPos cell = entry.getKey();
            int depth = entry.getValue();
            for (int dy = 1; dy <= 2; dy++) {
                if (level.getBlockState(cell.above(dy)).canBeReplaced()) {
                    level.setBlock(cell.above(dy), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
                }
            }
            for (int dy = 0; dy < depth; dy++) {
                BlockPos pos = cell.below(dy);
                level.setBlock(pos, water, Block.UPDATE_CLIENTS);
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    BlockPos side = pos.relative(dir);
                    if (!level.getBlockState(side).isSolid() && !level.getFluidState(side).isSource()) {
                        level.setBlock(side, Blocks.DIRT.defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                }
            }
            BlockPos floor = cell.below(depth);
            if (random.nextFloat() < 0.3F || !level.getBlockState(floor).isSolid()) {
                level.setBlock(floor, random.nextBoolean() ? Blocks.CLAY.defaultBlockState() : Blocks.DIRT.defaultBlockState(), Block.UPDATE_CLIENTS);
            }
            if (random.nextFloat() < LILY_PAD_CHANCE) {
                level.setBlock(cell.above(), Blocks.LILY_PAD.defaultBlockState(), Block.UPDATE_CLIENTS);
            }
        }

        for (BlockPos cell : cells.keySet()) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos edge = cell.relative(dir);
                if (!cells.containsKey(edge) && random.nextFloat() < SUGAR_CANE_CHANCE
                        && level.getBlockState(edge).is(BlockTags.DIRT) && level.isEmptyBlock(edge.above())) {
                    int height = 1 + random.nextInt(3);
                    for (int dy = 1; dy <= height && level.isEmptyBlock(edge.above(dy)); dy++) {
                        level.setBlock(edge.above(dy), Blocks.SUGAR_CANE.defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
        return true;
    }
}
