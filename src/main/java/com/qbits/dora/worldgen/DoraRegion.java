package com.qbits.dora.worldgen;

import com.mojang.datafixers.util.Pair;
import com.qbits.dora.init.ModBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class DoraRegion extends Region {
    private static final Climate.Parameter FULL = Climate.Parameter.span(-1.0F, 1.0F);

    public DoraRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
            builder.replaceBiome(Biomes.SUNFLOWER_PLAINS, ModBiomes.PUMPKIN_FIELD);
            builder.replaceBiome(Biomes.FOREST, ModBiomes.AUTUMNAL_FOREST);
            builder.replaceBiome(Biomes.FLOWER_FOREST, ModBiomes.MAPLE_FOREST);
            builder.replaceBiome(Biomes.OLD_GROWTH_BIRCH_FOREST, ModBiomes.BLOOMING_SANCTUARY);
        });
        this.addBiome(mapper, Climate.parameters(
                FULL,
                Climate.Parameter.span(-1.0F, -0.7F),
                FULL,
                FULL,
                Climate.Parameter.span(0.2F, 0.5F),
                FULL,
                0.0F
        ), ModBiomes.ANT_HILL);
    }
}
