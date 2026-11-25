package com.qbits.dora.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FlammableLogBlock extends RotatedPillarBlock {
    @Nullable
    private final Supplier<? extends Block> stripped;

    public FlammableLogBlock(Properties properties, @Nullable Supplier<? extends Block> stripped) {
        super(properties);
        this.stripped = stripped;
    }

    private int encouragement = 5;
    private int flammability = 5;

    public FlammableLogBlock(Properties properties) {
        this(properties, null);
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (toolAction == ToolActions.AXE_STRIP && stripped != null) {
            return stripped.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return flammability;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return encouragement;
    }

    public FlammableLogBlock burnsFaster() {
        encouragement = 10;
        flammability = 10;
        return this;
    }
}
