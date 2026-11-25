package com.qbits.dora.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.qbits.dora.Dora;
import com.qbits.dora.entity.Manul;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ManulRenderer extends MobRenderer<Manul, ManulModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Dora.MODID, "textures/entity/manul.png");

    public ManulRenderer(EntityRendererProvider.Context context) {
        super(context, new ManulModel(context.bakeLayer(ManulModel.LAYER)), 0.4F);
    }

    @Override
    protected void scale(Manul entity, PoseStack poseStack, float partialTick) {
        if (entity.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Manul entity) {
        return TEXTURE;
    }
}
