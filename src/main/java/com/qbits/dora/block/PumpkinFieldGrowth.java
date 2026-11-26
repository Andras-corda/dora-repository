package com.qbits.dora.block;

import com.qbits.dora.Dora;
import com.qbits.dora.init.ModBiomes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.StemBlock;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Dora.MODID)
public class PumpkinFieldGrowth {
    private static final float BONUS_CHANCE = 0.1F;

    @SubscribeEvent
    public static void onCropGrow(BlockEvent.CropGrowEvent.Pre event) {
        LevelAccessor level = event.getLevel();
        if (event.getState().getBlock() instanceof StemBlock && event.getResult() == Event.Result.DEFAULT
                && level.getRandom().nextFloat() < BONUS_CHANCE && level.getBiome(event.getPos()).is(ModBiomes.PUMPKIN_FIELD)) {
            event.setResult(Event.Result.ALLOW);
        }
    }
}
