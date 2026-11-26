package com.qbits.dora.client;

import com.qbits.dora.Dora;
import com.qbits.dora.init.ModBlockEntities;
import com.qbits.dora.init.ModEntities;
import com.qbits.dora.init.ModParticles;
import com.qbits.dora.init.ModWoodTypes;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Dora.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Sheets.addWoodType(ModWoodTypes.MAPLE);
            Sheets.addWoodType(ModWoodTypes.ENGLISH_OAK);
            Sheets.addWoodType(ModWoodTypes.DRAINED_MAPLE);
            Sheets.addWoodType(ModWoodTypes.WISTERIA);
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CARVING.get(), CarvingBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SIGN.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.HANGING_SIGN.get(), HangingSignRenderer::new);
        event.registerEntityRenderer(ModEntities.MAPLE_BOAT.get(), context -> new DoraBoatRenderer(context, "maple", false));
        event.registerEntityRenderer(ModEntities.MAPLE_CHEST_BOAT.get(), context -> new DoraBoatRenderer(context, "maple", true));
        event.registerEntityRenderer(ModEntities.ENGLISH_OAK_BOAT.get(), context -> new DoraBoatRenderer(context, "english_oak", false));
        event.registerEntityRenderer(ModEntities.ENGLISH_OAK_CHEST_BOAT.get(), context -> new DoraBoatRenderer(context, "english_oak", true));
        event.registerEntityRenderer(ModEntities.DRAINED_MAPLE_BOAT.get(), context -> new DoraBoatRenderer(context, "drained_maple", false));
        event.registerEntityRenderer(ModEntities.DRAINED_MAPLE_CHEST_BOAT.get(), context -> new DoraBoatRenderer(context, "drained_maple", true));
        event.registerEntityRenderer(ModEntities.WISTERIA_BOAT.get(), context -> new DoraBoatRenderer(context, "wisteria", false));
        event.registerEntityRenderer(ModEntities.WISTERIA_CHEST_BOAT.get(), context -> new DoraBoatRenderer(context, "wisteria", true));
        event.registerEntityRenderer(ModEntities.MANUL.get(), ManulRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        ModParticles.FALLING_LEAVES.forEach(type -> event.registerSpriteSet(type.get(), FallingLeafParticle.Provider::new));
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ManulModel.LAYER, ManulModel::createBodyLayer);
    }
}
