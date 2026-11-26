package com.qbits.dora.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FlammableLeavesBlock extends LeavesBlock {
    private static final int FALL_CHANCE = 10;

    @Nullable
    private Supplier<? extends ParticleOptions> fallingLeaf;

    public FlammableLeavesBlock(Properties properties) {
        super(properties);
    }

    public FlammableLeavesBlock fallingLeaves(Supplier<? extends ParticleOptions> particle) {
        this.fallingLeaf = particle;
        return this;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (fallingLeaf == null || random.nextInt(FALL_CHANCE) != 0) {
            return;
        }
        BlockPos below = pos.below();
        if (!isFaceFull(level.getBlockState(below).getCollisionShape(level, below), Direction.UP)) {
            ParticleUtils.spawnParticleBelow(level, pos, random, fallingLeaf.get());
        }
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }
}
