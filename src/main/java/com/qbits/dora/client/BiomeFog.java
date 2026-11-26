package com.qbits.dora.client;

import com.qbits.dora.Dora;
import com.qbits.dora.init.ModBiomes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Dora.MODID, value = Dist.CLIENT)
public class BiomeFog {
    private static final float DAY_NEAR = 0.2F;
    private static final float DAY_FAR = 0.85F;
    private static final float NIGHT_NEAR = 0.05F;
    private static final float NIGHT_FAR = 0.55F;
    private static final float NIGHT_RED = 0.14F;
    private static final float NIGHT_GREEN = 0.18F;
    private static final float NIGHT_BLUE = 0.26F;
    private static final float NIGHT_TINT = 0.6F;
    private static final float SPEED = 0.01F;
    private static float blend;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (event.phase != TickEvent.Phase.END || minecraft.level == null || minecraft.player == null || minecraft.isPaused()) {
            return;
        }
        boolean inside = minecraft.level.getBiome(minecraft.player.blockPosition()).is(ModBiomes.PUMPKIN_FIELD);
        blend = Mth.approach(blend, inside ? 1.0F : 0.0F, SPEED);
    }

    private static boolean active() {
        Minecraft minecraft = Minecraft.getInstance();
        return blend > 0.0F && minecraft.level != null && minecraft.player != null
                && !minecraft.player.hasEffect(MobEffects.BLINDNESS) && !minecraft.player.hasEffect(MobEffects.DARKNESS);
    }

    private static float night(float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        float day = Mth.clamp(Mth.cos(minecraft.level.getTimeOfDay(partialTick) * Mth.TWO_PI) * 2.0F + 0.5F, 0.0F, 1.0F);
        return 1.0F - day;
    }

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        if (!active() || event.getMode() != FogRenderer.FogMode.FOG_TERRAIN || event.getType() != FogType.NONE) {
            return;
        }
        float night = night((float) event.getPartialTick());
        float far = event.getFarPlaneDistance();
        float near = Mth.lerp(night, DAY_NEAR, NIGHT_NEAR);
        float end = Mth.lerp(night, DAY_FAR, NIGHT_FAR);
        event.setNearPlaneDistance(Mth.lerp(blend, event.getNearPlaneDistance(), far * near));
        event.setFarPlaneDistance(Mth.lerp(blend, far, far * end));
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (!active() || event.getCamera().getFluidInCamera() != FogType.NONE) {
            return;
        }
        float tint = blend * night((float) event.getPartialTick()) * NIGHT_TINT;
        event.setRed(Mth.lerp(tint, event.getRed(), NIGHT_RED));
        event.setGreen(Mth.lerp(tint, event.getGreen(), NIGHT_GREEN));
        event.setBlue(Mth.lerp(tint, event.getBlue(), NIGHT_BLUE));
    }
}
