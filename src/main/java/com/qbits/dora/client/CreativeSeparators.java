package com.qbits.dora.client;

import com.qbits.dora.Dora;
import com.qbits.dora.item.SeparatorItem;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Dora.MODID, value = Dist.CLIENT)
public class CreativeSeparators {
    private static final int WIDTH = SeparatorItem.ROW * 18;
    private static final int HEIGHT = 18;
    private static final int TEXT_X = 5;
    private static final int TEXT_Y = 5;
    private static final int TEXT_COLOR = 0xFFFFFF;

    @SubscribeEvent
    public static void onForeground(ContainerScreenEvent.Render.Foreground event) {
        if (!(event.getContainerScreen() instanceof CreativeModeInventoryScreen screen)) {
            return;
        }
        GuiGraphics graphics = event.getGuiGraphics();
        for (Slot slot : screen.getMenu().slots) {
            String name = SeparatorItem.name(slot.getItem());
            if (name == null || !slot.isActive()) {
                continue;
            }
            int x = slot.x - 1;
            int y = slot.y - 1;
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 300);
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(Dora.MODID, "textures/gui/separator/" + name + ".png");
            graphics.blit(texture, x, y, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
            graphics.drawString(Minecraft.getInstance().font, Component.translatable("separator.dora." + name), x + TEXT_X, y + TEXT_Y, TEXT_COLOR, true);
            graphics.pose().popPose();
        }
    }

    @SubscribeEvent
    public static void onTooltip(RenderTooltipEvent.Pre event) {
        if (SeparatorItem.isSeparator(event.getItemStack())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouseClick(ScreenEvent.MouseButtonPressed.Pre event) {
        if (isOverSeparator(event.getScreen())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouseRelease(ScreenEvent.MouseButtonReleased.Pre event) {
        if (isOverSeparator(event.getScreen())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onKey(ScreenEvent.KeyPressed.Pre event) {
        if (isOverSeparator(event.getScreen()) && isSlotKey(event.getKeyCode(), event.getScanCode())) {
            event.setCanceled(true);
        }
    }

    private static boolean isSlotKey(int key, int scan) {
        Options options = Minecraft.getInstance().options;
        if (options.keyDrop.matches(key, scan) || options.keySwapOffhand.matches(key, scan) || options.keyPickItem.matches(key, scan)) {
            return true;
        }
        for (KeyMapping hotbar : options.keyHotbarSlots) {
            if (hotbar.matches(key, scan)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isOverSeparator(Screen screen) {
        if (!(screen instanceof CreativeModeInventoryScreen creative)) {
            return false;
        }
        Slot slot = creative.getSlotUnderMouse();
        return slot != null && SeparatorItem.isSeparator(slot.getItem());
    }
}
