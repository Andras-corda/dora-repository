package com.qbits.dora.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.qbits.dora.Dora;
import com.qbits.dora.block.CarvablePumpkinBlock;
import com.qbits.dora.carving.Carving;
import com.qbits.dora.carving.CarvingBlockEntity;
import com.qbits.dora.carving.CarvingGeometry;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.BitSet;

public class CarvingBlockEntityRenderer implements BlockEntityRenderer<CarvingBlockEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Dora.MODID, "textures/block/carving.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);
    private static final double OFFSET = 0.002;

    public CarvingBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CarvingBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
        Carving carving = entity.getCarving();
        Level level = entity.getLevel();
        if (carving.isEmpty() || level == null) {
            return;
        }
        BlockState state = entity.getBlockState();
        boolean lit = state.getValue(CarvablePumpkinBlock.LIT);
        CarvingColors.Palette palette = CarvingColors.of(state);
        VertexConsumer consumer = buffers.getBuffer(RENDER_TYPE);
        Matrix4f pose = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        BlockPos pos = entity.getBlockPos();

        carving.faces().forEach((face, bits) -> {
            int light = lit ? LightTexture.FULL_BRIGHT : LevelRenderer.getLightColor(level, pos.relative(face));
            renderFace(consumer, pose, normal, face, bits, carving.shadows(face), carving.cores(face), palette, light);
        });
    }

    private static void renderFace(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, Direction face, BitSet bits, BitSet shadows, BitSet cores, CarvingColors.Palette palette, int light) {
        float nx = face.getStepX();
        float ny = face.getStepY();
        float nz = face.getStepZ();
        for (int index = bits.nextSetBit(0); index >= 0; index = bits.nextSetBit(index + 1)) {
            int color = shadows.get(index) ? palette.shadow() : cores.get(index) ? palette.core() : palette.edge();
            float u0 = (index % Carving.SIZE) / (float) Carving.SIZE;
            float v0 = (index / Carving.SIZE) / (float) Carving.SIZE;
            float u1 = u0 + 1F / Carving.SIZE;
            float v1 = v0 + 1F / Carving.SIZE;
            vertex(consumer, pose, normal, face, u0, v0, light, color, nx, ny, nz);
            vertex(consumer, pose, normal, face, u1, v0, light, color, nx, ny, nz);
            vertex(consumer, pose, normal, face, u1, v1, light, color, nx, ny, nz);
            vertex(consumer, pose, normal, face, u0, v1, light, color, nx, ny, nz);
        }
    }

    private static void vertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, Direction face, float u, float v, int light, int color, float nx, float ny, float nz) {
        Vec3 local = CarvingGeometry.toLocal(face, u, v, OFFSET);
        consumer.vertex(pose, (float) local.x, (float) local.y, (float) local.z)
                .color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, nx, ny, nz)
                .endVertex();
    }
}
