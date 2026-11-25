package com.qbits.dora.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.qbits.dora.Dora;
import com.qbits.dora.entity.Manul;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.List;

public class ManulModel extends EntityModel<Manul> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Dora.MODID, "manul"), "main");
    private static final float HALF_PI = (float) Math.PI / 2;
    private static final float WALK = 0.6662F;

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart backLeftLeg;
    private final ModelPart backRightLeg;
    private final ModelPart tail;
    private final ModelPart tailTip;
    private final List<ModelPart> parts;

    public ManulModel(ModelPart root) {
        head = root.getChild("head");
        body = root.getChild("body");
        frontLeftLeg = root.getChild("front_left_leg");
        frontRightLeg = root.getChild("front_right_leg");
        backLeftLeg = root.getChild("back_left_leg");
        backRightLeg = root.getChild("back_right_leg");
        tail = root.getChild("tail");
        tailTip = tail.getChild("tail2");
        parts = List.of(head, body, frontLeftLeg, frontRightLeg, backLeftLeg, backRightLeg, tail);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(1, 25).addBox(-3.5F, -2.0F, -3.0F, 7.0F, 6.0F, 5.0F)
                .texOffs(27, 25).addBox(3.5F, -1.0F, -3.0F, 1.0F, 5.0F, 5.0F)
                .texOffs(33, 1).addBox(-4.5F, -1.0F, -3.0F, 1.0F, 5.0F, 5.0F)
                .texOffs(40, 24).addBox(-1.5F, 0.98F, -4.0F, 3.0F, 2.0F, 1.0F), PartPose.offset(0.0F, 15.0F, -9.0F));
        head.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(40, 30).addBox(0.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(2.0F, -2.5F, -1.0F, 0.0F, -HALF_PI, 0.0F));
        head.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(40, 27).addBox(-1.5F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-1.5F, -2.5F, 0.5F, 0.0F, -HALF_PI, 0.0F));

        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 3.0F, -9.0F, 8.0F, 16.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 12.0F, -10.0F, HALF_PI, 0.0F, 0.0F));

        root.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(32, 11).addBox(-1.0F, 0.0F, 0.0F, 3.0F, 4.0F, 3.0F), PartPose.offset(1.1F, 20.1F, -5.0F));
        root.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(26, 35).addBox(-2.0F, 0.0F, 0.0F, 3.0F, 4.0F, 3.0F), PartPose.offset(-1.1F, 20.1F, -5.0F));
        root.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(0, 36).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 4.0F, 3.0F), PartPose.offset(1.1F, 20.0F, 5.0F));
        root.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(12, 36).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 4.0F, 3.0F), PartPose.offset(-1.1F, 20.0F, 5.0F));

        PartDefinition tail = root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(38, 35).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 15.5F, 8.0F, HALF_PI, 0.0F, 0.0F));
        tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(32, 18).addBox(-1.5F, -4.0F, -1.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(0.0F, 8.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(Manul entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        head.xRot = headPitch * Mth.DEG_TO_RAD;

        float stride = entity.isSprinting() ? 1.0F : 1.4F;
        float phase = limbSwing * WALK;
        backLeftLeg.xRot = Mth.cos(phase) * stride * limbSwingAmount;
        backRightLeg.xRot = Mth.cos(phase + Mth.PI) * stride * limbSwingAmount;
        frontLeftLeg.xRot = Mth.cos(phase + Mth.PI) * stride * limbSwingAmount;
        frontRightLeg.xRot = Mth.cos(phase) * stride * limbSwingAmount;

        float idle = Mth.sin(ageInTicks * 0.08F) * 0.12F;
        float walk = Mth.cos(phase) * 0.35F * limbSwingAmount;
        tail.xRot = HALF_PI - 0.25F * limbSwingAmount;
        tail.yRot = idle + walk;
        tailTip.zRot = -(idle + walk) * 0.8F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
        for (ModelPart part : parts) {
            part.render(poseStack, consumer, light, overlay, red, green, blue, alpha);
        }
    }
}
