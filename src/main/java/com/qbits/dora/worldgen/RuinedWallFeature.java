package com.qbits.dora.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.List;

public class RuinedWallFeature extends Feature<NoneFeatureConfiguration> {
    private static final float GAP_CHANCE = 0.15F;
    private static final float MOSSY_CHANCE = 0.35F;
    private static final float COBWEB_CHANCE = 0.3F;

    public RuinedWallFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        Direction axis = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        Direction side = axis.getClockWise();
        int length = 3 + random.nextInt(6);
        int bend = random.nextInt(length);
        List<BlockPos> walls = new ArrayList<>();
        BlockPos column = context.origin();

        for (int i = 0; i < length; i++) {
            if (i == bend && i > 0) {
                column = column.relative(random.nextBoolean() ? side : side.getOpposite());
            }
            BlockPos base = findBase(level, column);
            column = column.relative(axis);
            if (base == null || (i > 0 && i < length - 1 && random.nextFloat() < GAP_CHANCE)) {
                continue;
            }
            boolean mossy = random.nextFloat() < MOSSY_CHANCE;
            boolean pillar = (i == 0 || i == length - 1) && random.nextBoolean();
            int height = 1 + (random.nextFloat() < 0.4F ? 1 : 0) + (pillar ? 1 : 0);
            for (int dy = 0; dy < height; dy++) {
                BlockPos pos = base.above(dy);
                if (!level.getBlockState(pos).canBeReplaced()) {
                    break;
                }
                BlockState state = pillar && dy < height - 1
                        ? (mossy ? Blocks.MOSSY_COBBLESTONE : Blocks.COBBLESTONE).defaultBlockState()
                        : (mossy ? Blocks.MOSSY_COBBLESTONE_WALL : Blocks.COBBLESTONE_WALL).defaultBlockState();
                level.setBlock(pos, state, Block.UPDATE_CLIENTS);
                walls.add(pos);
            }
            if (random.nextFloat() < COBWEB_CHANCE) {
                BlockPos web = base.above(random.nextInt(height + 1)).relative(random.nextBoolean() ? side : side.getOpposite());
                if (level.isEmptyBlock(web)) {
                    level.setBlock(web, Blocks.COBWEB.defaultBlockState(), Block.UPDATE_CLIENTS);
                }
            }
        }
        for (BlockPos pos : walls) {
            level.setBlock(pos, Block.updateFromNeighbourShapes(level.getBlockState(pos), level, pos), Block.UPDATE_CLIENTS);
        }
        return !walls.isEmpty();
    }

    private static BlockPos findBase(WorldGenLevel level, BlockPos column) {
        for (int dy = 2; dy >= -3; dy--) {
            BlockPos pos = column.above(dy);
            if (level.getBlockState(pos).canBeReplaced() && level.getBlockState(pos.below()).isSolid()
                    && !level.getFluidState(pos).isSource()) {
                return pos;
            }
        }
        return null;
    }
}
