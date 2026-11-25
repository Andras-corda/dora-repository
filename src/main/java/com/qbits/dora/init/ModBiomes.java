package com.qbits.dora.init;

import com.qbits.dora.Dora;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {
    public static final ResourceKey<Biome> PUMPKIN_FIELD = key("pumpkin_field");
    public static final ResourceKey<Biome> AUTUMNAL_FOREST = key("autumnal_forest");
    public static final ResourceKey<Biome> MAPLE_FOREST = key("maple_forest");
    public static final ResourceKey<Biome> BLOOMING_SANCTUARY = key("blooming_sanctuary");
    public static final ResourceKey<Biome> ANT_HILL = key("ant_hill");

    private static ResourceKey<Biome> key(String name) {
        return ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Dora.MODID, name));
    }
}
