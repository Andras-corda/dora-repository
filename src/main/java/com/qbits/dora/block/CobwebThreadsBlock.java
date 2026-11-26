package com.qbits.dora.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CobwebThreadsBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty BRIDGE = BooleanProperty.create("bridge");
    private static final float STRING_CHANCE = 0.2F;
    private static final VoxelShape X_PLANE = Block.box(0, 0, 7.5, 16, 16, 8.5);
    private static final VoxelShape Z_PLANE = Block.box(7.5, 0, 0, 8.5, 16, 16);

    public CobwebThreadsBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(BRIDGE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BRIDGE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_PLANE : Z_PLANE;
    }

    private static boolean anchors(LevelReader level, BlockPos pos, Direction side) {
        BlockPos neighbor = pos.relative(side);
        return level.getBlockState(neighbor).isFaceSturdy(level, neighbor, side.getOpposite());
    }

    private static boolean grounded(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Nullable
    public BlockState stateFor(LevelReader level, BlockPos pos, Direction anchor) {
        if (!grounded(level, pos) || !anchors(level, pos, anchor)) {
            return null;
        }
        return defaultBlockState().setValue(FACING, anchor).setValue(BRIDGE, anchors(level, pos, anchor.getOpposite()));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clicked = context.getClickedFace().getOpposite();
        if (clicked.getAxis().isHorizontal()) {
            BlockState state = stateFor(context.getLevel(), context.getClickedPos(), clicked);
            if (state != null) {
                return state;
            }
        }
        for (Direction side : context.getNearestLookingDirections()) {
            if (side.getAxis().isHorizontal()) {
                BlockState state = stateFor(context.getLevel(), context.getClickedPos(), side);
                if (state != null) {
                    return state;
                }
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return grounded(level, pos) && anchors(level, pos, state.getValue(FACING));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state.setValue(BRIDGE, anchors(level, pos, state.getValue(FACING).getOpposite()));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || !(entity instanceof Player player) || player.isCrouching() || player.isSpectator()) {
            return;
        }
        level.destroyBlock(pos, false, player);
        if (level.random.nextFloat() < STRING_CHANCE) {
            Block.popResource(level, pos, new ItemStack(Items.STRING));
        }
    }
}
