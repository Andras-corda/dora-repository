package com.qbits.dora.client;

import com.mojang.datafixers.util.Pair;
import com.qbits.dora.Dora;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

public class DoraBoatRenderer extends BoatRenderer {
    private final Pair<ResourceLocation, ListModel<Boat>> model;

    public DoraBoatRenderer(EntityRendererProvider.Context context, String wood, boolean chest) {
        super(context, chest);
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(Dora.MODID, "textures/entity/" + (chest ? "chest_boat" : "boat") + "/" + wood + ".png");
        ListModel<Boat> boatModel = chest
                ? new ChestBoatModel(context.bakeLayer(ModelLayers.createChestBoatModelName(Boat.Type.OAK)))
                : new BoatModel(context.bakeLayer(ModelLayers.createBoatModelName(Boat.Type.OAK)));
        this.model = Pair.of(texture, boatModel);
    }

    @Override
    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        return model;
    }
}
