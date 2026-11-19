package com.qbits.dora.item;

import com.qbits.dora.block.StrippedMapleLogBlock;
import com.qbits.dora.client.ClientHooks;
import com.qbits.dora.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CarvingChiselItem extends Item {
    private static final int MAX_STACK_SIZE = 8;

    private final Tier tier;

    public CarvingChiselItem(Tier tier, int durability, Properties properties) {
        super(properties.durability(durability));
        this.tier = tier;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return MAX_STACK_SIZE;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(context.getClickedPos());
        if (player == null || !state.is(ModTags.CARVABLE) && !StrippedMapleLogBlock.canTap(state, context.getClickedFace())) {
            return InteractionResult.PASS;
        }
        return startCarving(level, player, context.getClickedPos(), context.getClickedFace(), context.getHand());
    }

    public static InteractionResult startCarving(Level level, Player player, BlockPos pos, Direction face, InteractionHand hand) {
        if (level.isClientSide) {
            ClientHooks.openCarving(pos, face, hand);
        } else {
            player.getItemInHand(hand).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public int getEnchantmentValue() {
        return tier.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        return tier.getRepairIngredient().test(repair);
    }
}
