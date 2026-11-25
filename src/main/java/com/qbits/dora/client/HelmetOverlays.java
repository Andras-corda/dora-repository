package com.qbits.dora.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.qbits.dora.Dora;
import com.qbits.dora.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Dora.MODID, value = Dist.CLIENT)
public class HelmetOverlays {
    private static final ResourceLocation PUMPKIN_BLUR = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/misc/pumpkinblur.png");

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.HELMET.type()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }
        ItemStack helmet = minecraft.player.getItemBySlot(EquipmentSlot.HEAD);
        if (helmet.is(ModBlocks.CARVED_LUMINA.get().asItem())) {
            drawFullscreen(event.getGuiGraphics(), PUMPKIN_BLUR);
        }
    }

    private static void drawFullscreen(GuiGraphics graphics, ResourceLocation texture) {
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(texture, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
}
