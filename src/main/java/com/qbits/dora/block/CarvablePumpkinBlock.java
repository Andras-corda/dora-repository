package com.qbits.dora.block;

import com.qbits.dora.carving.CarvingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ToolActions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CarvablePumpkinBlock extends StemGrownBlock implements EntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private final Supplier<? extends Block> stem;
    private final Supplier<? extends Block> attachedStem;
    @Nullable
    private final Supplier<? extends Block> carved;

    public CarvablePumpkinBlock(Properties properties, Supplier<? extends Block> stem, Supplier<? extends Block> attachedStem) {
        this(properties, stem, attachedStem, null);
    }

    public CarvablePumpkinBlock(Properties properties, Supplier<? extends Block> stem, Supplier<? extends Block> attachedStem, @Nullable Supplier<? extends Block> carved) {
        super(properties.lightLevel(state -> state.getValue(LIT) ? 15 : 0));
        this.stem = stem;
        this.attachedStem = attachedStem;
        this.carved = carved;
        registerDefaultState(defaultBlockState().setValue(LIT, false));
    }

    @Override
    public StemBlock getStem() {
        return (StemBlock) stem.get();
    }

    @Override
    public AttachedStemBlock getAttachedStem() {
        return (AttachedStemBlock) attachedStem.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CarvingBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (carved != null && !state.getValue(LIT) && stack.canPerformAction(ToolActions.SHEARS_CARVE) && !isCarved(level, pos)) {
            if (!level.isClientSide) {
                carve(level, pos, player, hand, stack, hit.getDirection());
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (!state.getValue(LIT) && stack.is(Items.TORCH) && isCarved(level, pos)) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(LIT, true), Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (state.getValue(LIT) && hand == InteractionHand.MAIN_HAND && player.isSecondaryUseActive() && stack.isEmpty()) {
            if (!level.isClientSide) {
                extinguish(level, pos, state);
                if (!player.getAbilities().instabuild && !player.getInventory().add(new ItemStack(Items.TORCH))) {
                    player.drop(new ItemStack(Items.TORCH), false);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    private void carve(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack shears, Direction side) {
        Direction facing = side.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : side;
        level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.setBlock(pos, carved.get().defaultBlockState().setValue(CarvedPumpkinBlock.FACING, facing), Block.UPDATE_ALL_IMMEDIATE);
        ItemStack seeds = getStem().getCloneItemStack(level, pos, getStem().defaultBlockState());
        seeds.setCount(4);
        ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5 + facing.getStepX() * 0.65, pos.getY() + 0.1, pos.getZ() + 0.5 + facing.getStepZ() * 0.65, seeds);
        drop.setDeltaMovement(0.05 * facing.getStepX() + level.random.nextDouble() * 0.02, 0.05, 0.05 * facing.getStepZ() + level.random.nextDouble() * 0.02);
        level.addFreshEntity(drop);
        shears.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
        level.gameEvent(player, GameEvent.SHEAR, pos);
        player.awardStat(Stats.ITEM_USED.get(shears.getItem()));
    }

    public static void extinguish(Level level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(LIT, false), Block.UPDATE_ALL);
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.6F);
    }

    private static boolean isCarved(BlockGetter level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof CarvingBlockEntity entity && !entity.getCarving().isEmpty();
    }
}
