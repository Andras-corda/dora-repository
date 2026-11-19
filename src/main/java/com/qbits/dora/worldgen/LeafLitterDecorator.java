package com.qbits.dora.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.qbits.dora.init.ModBlocks;
import com.qbits.dora.init.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class LeafLitterDecorator extends TreeDecorator {
    public static final Codec<LeafLitterDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("tries").forGetter(decorator -> decorator.tries),
            ExtraCodecs.POSITIVE_INT.fieldOf("radius").forGetter(decorator -> decorator.radius)
    ).apply(instance, LeafLitterDecorator::new));

    private static final int SEARCH_UP = 3;
    private static final int SEARCH_DOWN = 4;

    private final int tries;
    private final int radius;

    public LeafLitterDecorator(int tries, int radius) {
        this.tries = tries;
        this.radius = radius;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModFeatures.LEAF_LITTER.get();
    }

    @Override
    public void place(Context context) {
        if (context.logs().isEmpty()) {
            return;
        }
        BlockPos base = context.logs().get(0);
        RandomSource random = context.random();
        for (int i = 0; i < tries; i++) {
            BlockPos column = base.offset(random.nextInt(radius * 2 + 1) - radius, 0, random.nextInt(radius * 2 + 1) - radius);
            BlockPos ground = findGround(context, column);
            if (ground != null) {
                BlockState state = ModBlocks.LEAF_LITTER.get().defaultBlockState()
                        .setValue(PinkPetalsBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random))
                        .setValue(PinkPetalsBlock.AMOUNT, 1 + random.nextInt(4));
                context.setBlock(ground, state);
            }
        }
    }

    private static BlockPos findGround(Context context, BlockPos column) {
        for (int dy = SEARCH_UP; dy >= -SEARCH_DOWN; dy--) {
            BlockPos pos = column.above(dy);
            if (context.isAir(pos) && context.level().isStateAtPosition(pos.below(), state -> state.is(BlockTags.DIRT) && !state.is(Blocks.MUD))) {
                return pos;
            }
        }
        return null;
    }
}
