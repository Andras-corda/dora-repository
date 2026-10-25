package net.qbits.dora.client.renderer;

import net.qbits.dora.entity.PalacecatEntity;
import net.qbits.dora.client.model.ModelCustomModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PalacecatRenderer extends MobRenderer<PalacecatEntity, ModelCustomModel<PalacecatEntity>> {
	private final ResourceLocation entityTexture = new ResourceLocation("dora:textures/entities/texture.png");

	public PalacecatRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelCustomModel<PalacecatEntity>(context.bakeLayer(ModelCustomModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(PalacecatEntity entity) {
		return entityTexture;
	}
}