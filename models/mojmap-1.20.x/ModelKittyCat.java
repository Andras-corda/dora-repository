// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelKittyCat<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "kittycat"), "main");
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart front_left_leg;
	private final ModelPart front_right_leg;
	private final ModelPart back_left_leg;
	private final ModelPart back_right_leg;
	private final ModelPart tail;
	private final ModelPart tail2;

	public ModelKittyCat(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.front_left_leg = root.getChild("front_left_leg");
		this.front_right_leg = root.getChild("front_right_leg");
		this.back_left_leg = root.getChild("back_left_leg");
		this.back_right_leg = root.getChild("back_right_leg");
		this.tail = root.getChild("tail");
		this.tail2 = root.getChild("tail2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 20)
						.addBox(-3.5F, -2.0F, -3.0F, 7.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(28, 16)
						.addBox(-1.5F, 0.98F, -4.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 15.0F, -9.0F));

		PartDefinition head_r1 = head.addOrReplaceChild("head_r1",
				CubeListBuilder.create().texOffs(32, 34).addBox(-1.0F, -1.5F, -1.0F, 3.0F, 3.0F, 2.0F,
						new CubeDeformation(-0.001F)),
				PartPose.offsetAndRotation(2.0F, -2.5F, 1.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition head_r2 = head.addOrReplaceChild("head_r2",
				CubeListBuilder.create().texOffs(32, 29).addBox(-2.0F, -1.5F, -1.0F, 3.0F, 3.0F, 2.0F,
						new CubeDeformation(-0.001F)),
				PartPose.offsetAndRotation(-2.0F, -2.5F, 1.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 3.0F, -8.0F, 8.0F, 14.0F, 6.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 12.0F, -10.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition front_left_leg = partdefinition.addOrReplaceChild("front_left_leg", CubeListBuilder.create()
				.texOffs(24, 29).addBox(0.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(1.1F, 14.1F, -5.0F));

		PartDefinition front_right_leg = partdefinition.addOrReplaceChild("front_right_leg", CubeListBuilder.create()
				.texOffs(0, 31).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-1.1F, 14.1F, -5.0F));

		PartDefinition back_left_leg = partdefinition.addOrReplaceChild("back_left_leg", CubeListBuilder.create()
				.texOffs(24, 20).addBox(0.0F, 0.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(1.1F, 18.0F, 5.0F));

		PartDefinition back_right_leg = partdefinition.addOrReplaceChild("back_right_leg", CubeListBuilder.create()
				.texOffs(28, 0).addBox(-3.0F, 0.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-1.1F, 18.0F, 5.0F));

		PartDefinition tail = partdefinition
				.addOrReplaceChild("tail",
						CubeListBuilder.create().texOffs(28, 9).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 4.0F, 3.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 15.5F, 8.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition tail2 = partdefinition.addOrReplaceChild("tail2",
				CubeListBuilder.create().texOffs(8, 31).addBox(-1.5F, -5.0F, -2.0F, 3.0F, 4.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 15.5F, 16.0F, 1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		front_left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		front_right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		back_left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		back_right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}