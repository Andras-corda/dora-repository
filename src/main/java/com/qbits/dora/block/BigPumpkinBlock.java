package com.qbits.dora.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class BigPumpkinBlock extends Block {
    public static final IntegerProperty PART = IntegerProperty.create("part", 0, 7);
    private static final int SIZE = 2;

    public BigPumpkinBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(PART, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART);
    }

    public static int part(int x, int y, int z) {
        return x | y << 1 | z << 2;
    }

    public static BlockPos offset(int part) {
        return new BlockPos(part & 1, part >> 1 & 1, part >> 2 & 1);
    }

    public static BlockPos origin(BlockPos pos, BlockState state) {
        return pos.subtract(offset(state.getValue(PART)));
    }

    public boolean canFit(LevelAccessor level, BlockPos origin, Predicate<BlockPos> replaceable) {
        for (int part = 0; part < SIZE * SIZE * SIZE; part++) {
            if (!replaceable.test(origin.offset(offset(part)))) {
                return false;
            }
        }
        return true;
    }

    public boolean hasGround(LevelAccessor level, BlockPos origin) {
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                BlockPos below = origin.offset(x, -1, z);
                if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void place(LevelAccessor level, BlockPos origin, int flags) {
        for (int part = 0; part < SIZE * SIZE * SIZE; part++) {
            level.setBlock(origin.offset(offset(part)), defaultBlockState().setValue(PART, part), flags);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        Direction right = facing.getClockWise();
        int x = facing.getStepX() != 0 ? (facing.getStepX() > 0 ? 0 : 1) : (right.getStepX() > 0 ? 0 : 1);
        int z = facing.getStepZ() != 0 ? (facing.getStepZ() > 0 ? 0 : 1) : (right.getStepZ() > 0 ? 0 : 1);
        BlockPos clicked = context.getClickedPos();
        BlockPos origin = clicked.offset(-x, 0, -z);
        Level level = context.getLevel();
        boolean fits = canFit(level, origin, pos -> pos.equals(clicked) || level.getBlockState(pos).canBeReplaced(context))
                && origin.getY() + SIZE <= level.getMaxBuildHeight();
        return fits ? defaultBlockState().setValue(PART, part(x, 0, z)) : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockPos origin = origin(pos, state);
        for (int part = 0; part < SIZE * SIZE * SIZE; part++) {
            BlockPos target = origin.offset(offset(part));
            if (!target.equals(pos)) {
                level.setBlock(target, defaultBlockState().setValue(PART, part), Block.UPDATE_ALL);
            }
        }
    }
}
