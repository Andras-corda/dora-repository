package com.qbits.dora.block;

import com.qbits.dora.Dora;
import com.qbits.dora.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Dora.MODID)
public class BigPumpkinGrowth {
    private static final float CHANCE = 0.05F;
    private static final Map<Supplier<? extends Block>, Supplier<? extends Block>> BIG_VARIANTS = Map.of(
            () -> Blocks.PUMPKIN, ModBlocks.BIG_PUMPKIN,
            ModBlocks.LUMINA, ModBlocks.BIG_LUMINA,
            ModBlocks.WARTY_GOBLIN, ModBlocks.BIG_WARTY_GOBLIN,
            ModBlocks.JARRAHDALE, ModBlocks.BIG_JARRAHDALE
    );

    @SubscribeEvent
    public static void onCropGrow(BlockEvent.CropGrowEvent.Post event) {
        BlockState state = event.getState();
        if (!(state.getBlock() instanceof AttachedStemBlock) || event.getOriginalState().getBlock() instanceof AttachedStemBlock) {
            return;
        }
        LevelAccessor level = event.getLevel();
        if (level.getRandom().nextFloat() >= CHANCE) {
            return;
        }
        Direction facing = state.getValue(AttachedStemBlock.FACING);
        BlockPos fruit = event.getPos().relative(facing);
        Block fruitBlock = level.getBlockState(fruit).getBlock();
        for (Map.Entry<Supplier<? extends Block>, Supplier<? extends Block>> variant : BIG_VARIANTS.entrySet()) {
            if (variant.getKey().get() == fruitBlock && variant.getValue().get() instanceof BigPumpkinBlock big) {
                grow(level, fruit, facing, big);
                return;
            }
        }
    }

    private static void grow(LevelAccessor level, BlockPos fruit, Direction facing, BigPumpkinBlock big) {
        for (Direction side : new Direction[]{facing.getClockWise(), facing.getCounterClockWise()}) {
            BlockPos far = fruit.relative(facing).relative(side);
            BlockPos origin = new BlockPos(Math.min(fruit.getX(), far.getX()), fruit.getY(), Math.min(fruit.getZ(), far.getZ()));
            boolean fits = big.canFit(level, origin, pos -> pos.equals(fruit) || level.getBlockState(pos).canBeReplaced());
            if (fits && big.hasGround(level, origin)) {
                big.place(level, origin, Block.UPDATE_ALL);
                return;
            }
        }
    }
}
