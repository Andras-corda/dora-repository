package com.qbits.dora.client;

import com.qbits.dora.Dora;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Method;

@Mod.EventBusSubscriber(modid = Dora.MODID, value = Dist.CLIENT)
public class CarvingCamera {
    private static final double DISTANCE = 1.3;
    private static final double MIN_DISTANCE = 0.35;
    private static final double WALL_MARGIN = 0.2;
    private static final Method SET_POSITION = ObfuscationReflectionHelper.findMethod(Camera.class, "m_90581_", Vec3.class);

    @SubscribeEvent
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        CarvingSession session = CarvingSession.current();
        Level level = Minecraft.getInstance().level;
        if (session == null || level == null) {
            return;
        }
        float t = session.progress();
        Direction face = session.face;
        Vec3 normal = Vec3.atLowerCornerOf(face.getNormal());
        Vec3 faceCenter = Vec3.atCenterOf(session.pos).add(normal.scale(0.5));
        Vec3 target = faceCenter.add(normal.scale(clearDistance(level, faceCenter, normal)));
        float targetYaw = face.getAxis().isVertical() ? 180F : face.getOpposite().toYRot();
        float targetPitch = face == Direction.UP ? 90F : face == Direction.DOWN ? -90F : 0F;

        Camera camera = event.getCamera();
        Vec3 from = camera.getPosition();
        try {
            SET_POSITION.invoke(camera, from.add(target.subtract(from).scale(t)));
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
        event.setYaw(Mth.rotLerp(t, event.getYaw(), targetYaw));
        event.setPitch(Mth.lerp(t, event.getPitch(), targetPitch));
        event.setRoll(Mth.lerp(t, event.getRoll(), 0F));
    }

    private static double clearDistance(Level level, Vec3 faceCenter, Vec3 normal) {
        Vec3 start = faceCenter.add(normal.scale(0.01));
        Vec3 end = faceCenter.add(normal.scale(DISTANCE + WALL_MARGIN));
        BlockHitResult hit = level.clip(new ClipContext(start, end, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, Minecraft.getInstance().player));
        if (hit.getType() == HitResult.Type.MISS) {
            return DISTANCE;
        }
        return Math.max(MIN_DISTANCE, hit.getLocation().distanceTo(faceCenter) - WALL_MARGIN);
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (CarvingSession.current() != null && event.getEntity() == Minecraft.getInstance().player) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (CarvingSession.current() != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        if (CarvingSession.current() != null && event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) {
            event.setCanceled(true);
        }
    }
}
