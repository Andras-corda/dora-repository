package com.qbits.dora.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.qbits.dora.Dora;
import com.qbits.dora.carving.Carving;
import com.qbits.dora.carving.CarvingGeometry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;

@Mod.EventBusSubscriber(modid = Dora.MODID, value = Dist.CLIENT)
public class CarvingRenderer {
    private static final double GRID_OFFSET = 0.003;
    private static final double HOVER_OFFSET = 0.004;
    private static final double SAP_OFFSET = 0.0035;
    private static final int SAP_COLOR = 0xE8A33A;
    private static final int SAP_HINT_COLOR = 0x2A1608;
    private static final int GRID_ALPHA = 110;
    private static final int HOVER_ALPHA = 170;

    private static final Matrix4f PROJECTION = new Matrix4f();
    private static final Matrix4f VIEW = new Matrix4f();
    private static Vec3 cameraPos = Vec3.ZERO;

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS) {
            return;
        }
        PoseStack poseStack = event.getPoseStack();
        PROJECTION.set(event.getProjectionMatrix());
        VIEW.set(poseStack.last().pose());
        cameraPos = event.getCamera().getPosition();

        CarvingSession session = CarvingSession.current();
        if (session == null) {
            return;
        }
        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();

        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f pose = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        int alpha = (int) (session.progress() * 255);
        renderGrid(buffers.getBuffer(RenderType.lines()), pose, normal, session.pos, session.face, alpha);
        buffers.endBatch(RenderType.lines());
        if (session.sap != null) {
            renderSap(buffers.getBuffer(RenderType.debugQuads()), pose, session, alpha);
            buffers.endBatch(RenderType.debugQuads());
        }
        int[] area = session.selection != null ? session.selection
                : session.hovered != null ? new int[]{session.hovered[0], session.hovered[1], session.hovered[0], session.hovered[1]} : null;
        if (area != null) {
            int color = CarvingColors.of(Minecraft.getInstance().level.getBlockState(session.pos)).cursor();
            renderArea(buffers.getBuffer(RenderType.debugQuads()), pose, session.pos, session.face, area, alpha, color);
            buffers.endBatch(RenderType.debugQuads());
        }

        poseStack.popPose();
    }

    private static void renderGrid(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, BlockPos pos, Direction face, int alpha) {
        int lineAlpha = alpha * GRID_ALPHA / 255;
        for (int i = 0; i <= Carving.SIZE; i++) {
            float at = i / (float) Carving.SIZE;
            line(consumer, pose, normal, pos, face, at, 0, at, 1, lineAlpha);
            line(consumer, pose, normal, pos, face, 0, at, 1, at, lineAlpha);
        }
    }

    private static void line(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, BlockPos pos, Direction face, float u0, float v0, float u1, float v1, int alpha) {
        Vec3 start = CarvingGeometry.toLocal(face, u0, v0, GRID_OFFSET).add(pos.getX(), pos.getY(), pos.getZ());
        Vec3 end = CarvingGeometry.toLocal(face, u1, v1, GRID_OFFSET).add(pos.getX(), pos.getY(), pos.getZ());
        Vec3 direction = end.subtract(start).normalize();
        consumer.vertex(pose, (float) start.x, (float) start.y, (float) start.z).color(0, 0, 0, alpha).normal(normal, (float) direction.x, (float) direction.y, (float) direction.z).endVertex();
        consumer.vertex(pose, (float) end.x, (float) end.y, (float) end.z).color(0, 0, 0, alpha).normal(normal, (float) direction.x, (float) direction.y, (float) direction.z).endVertex();
    }

    private static void renderArea(VertexConsumer consumer, Matrix4f pose, BlockPos pos, Direction face, int[] area, int alpha, int color) {
        int cellAlpha = alpha * HOVER_ALPHA / 255;
        float u0 = Math.min(area[0], area[2]) / (float) Carving.SIZE;
        float v0 = Math.min(area[1], area[3]) / (float) Carving.SIZE;
        float u1 = (Math.max(area[0], area[2]) + 1) / (float) Carving.SIZE;
        float v1 = (Math.max(area[1], area[3]) + 1) / (float) Carving.SIZE;
        for (float[] corner : new float[][]{{u0, v0}, {u1, v0}, {u1, v1}, {u0, v1}}) {
            Vec3 local = CarvingGeometry.toLocal(face, corner[0], corner[1], HOVER_OFFSET);
            consumer.vertex(pose, (float) (pos.getX() + local.x), (float) (pos.getY() + local.y), (float) (pos.getZ() + local.z))
                    .color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, cellAlpha)
                    .endVertex();
        }
    }

    private static void renderSap(VertexConsumer consumer, Matrix4f pose, CarvingSession session, int alpha) {
        SapProgress sap = session.sap;
        for (int index = 0; index < Carving.SIZE * Carving.SIZE; index++) {
            boolean spot = sap.spots.get(index);
            boolean scraped = sap.scraped.get(index);
            int color;
            int cellAlpha;
            if (spot && scraped) {
                color = SAP_COLOR;
                cellAlpha = 235;
            } else if (spot) {
                color = SAP_HINT_COLOR;
                cellAlpha = 80;
            } else if (scraped) {
                color = 0xFFFFFF;
                cellAlpha = 45;
            } else {
                continue;
            }
            int x = index % Carving.SIZE;
            int y = index / Carving.SIZE;
            float u0 = x / (float) Carving.SIZE;
            float v0 = y / (float) Carving.SIZE;
            float u1 = (x + 1) / (float) Carving.SIZE;
            float v1 = (y + 1) / (float) Carving.SIZE;
            int a = alpha * cellAlpha / 255;
            for (float[] corner : new float[][]{{u0, v0}, {u1, v0}, {u1, v1}, {u0, v1}}) {
                Vec3 local = CarvingGeometry.toLocal(session.face, corner[0], corner[1], SAP_OFFSET);
                consumer.vertex(pose, (float) (session.pos.getX() + local.x), (float) (session.pos.getY() + local.y), (float) (session.pos.getZ() + local.z))
                        .color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, a)
                        .endVertex();
            }
        }
    }

    @Nullable
    public static int[] pick(BlockPos pos, Direction face, double screenX, double screenY, int screenWidth, int screenHeight) {
        float ndcX = (float) (2 * screenX / screenWidth - 1);
        float ndcY = (float) (1 - 2 * screenY / screenHeight);
        Matrix4f inverse = new Matrix4f(PROJECTION).mul(VIEW).invert();
        Vector4f near = inverse.transform(new Vector4f(ndcX, ndcY, -1, 1));
        Vector4f far = inverse.transform(new Vector4f(ndcX, ndcY, 1, 1));
        near.div(near.w);
        far.div(far.w);

        Vec3 origin = cameraPos.add(near.x, near.y, near.z);
        Vec3 direction = new Vec3(far.x - near.x, far.y - near.y, far.z - near.z).normalize();
        Direction.Axis axis = face.getAxis();
        double plane = pos.get(axis) + (face.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1 : 0);
        double step = direction.get(axis);
        if (Math.abs(step) < 1.0E-6) {
            return null;
        }
        double distance = (plane - origin.get(axis)) / step;
        if (distance <= 0) {
            return null;
        }
        Vec3 local = origin.add(direction.scale(distance)).subtract(pos.getX(), pos.getY(), pos.getZ());
        return CarvingGeometry.pixel(face, local);
    }
}
