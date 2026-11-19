package com.qbits.dora.carving;

import com.qbits.dora.Dora;
import com.qbits.dora.block.CarvablePumpkinBlock;
import com.qbits.dora.init.ModTags;
import com.qbits.dora.item.CarvingChiselItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Dora.MODID)
public class CarvingEvents {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (event.getHand() != InteractionHand.MAIN_HAND
                || !player.getMainHandItem().is(Items.TORCH)
                || !(player.getOffhandItem().getItem() instanceof CarvingChiselItem)) {
            return;
        }
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (!state.is(ModTags.CARVABLE) || canLight(level, pos, state)) {
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(CarvingChiselItem.startCarving(level, player, pos, event.getFace(), InteractionHand.OFF_HAND));
    }

    private static boolean canLight(Level level, BlockPos pos, BlockState state) {
        return state.hasProperty(CarvablePumpkinBlock.LIT) && !state.getValue(CarvablePumpkinBlock.LIT)
                && level.getBlockEntity(pos) instanceof CarvingBlockEntity entity && !entity.getCarving().isEmpty();
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        CompoundTag tag = BlockItem.getBlockEntityData(event.getItemStack());
        if (tag != null && tag.contains(CarvingBlockEntity.TAG)) {
            event.getToolTip().add(Component.translatable("tooltip.dora.carved").withStyle(ChatFormatting.GRAY));
        }
    }
}
