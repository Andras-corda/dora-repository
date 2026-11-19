package com.qbits.dora.block;

import com.qbits.dora.init.ModBlocks;
import com.qbits.dora.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class StrippedMapleLogBlock extends FlammableLogBlock {
    public static final int MAX_SAP = 3;
    public static final IntegerProperty SAP = IntegerProperty.create("sap", 0, MAX_SAP);

    public StrippedMapleLogBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(SAP, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SAP);
    }

    public static boolean canTap(BlockState state, Direction face) {
        return state.getBlock() instanceof StrippedMapleLogBlock && state.getValue(SAP) == 0 && face.getAxis() != state.getValue(AXIS);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        int sap = state.getValue(SAP);
        if (sap == 0 || !stack.is(Items.GLASS_BOTTLE)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            BlockState next = sap == 1
                    ? ModBlocks.DRAINED_MAPLE_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS))
                    : state.setValue(SAP, sap - 1);
            level.setBlock(pos, next, Block.UPDATE_ALL);
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(ModItems.MAPLE_SYRUP.get())));
            player.awardStat(Stats.ITEM_USED.get(Items.GLASS_BOTTLE));
            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
