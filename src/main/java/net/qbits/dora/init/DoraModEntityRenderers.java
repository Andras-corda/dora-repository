/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.qbits.dora.init;

import net.qbits.dora.client.renderer.PalacecatRenderer;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DoraModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(DoraModEntities.PALACECAT.get(), PalacecatRenderer::new);
	}
}