package com.qbits.dora.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.List;

public record TemplateTreeConfiguration(List<ResourceLocation> templates, List<TreeDecorator> decorators) implements FeatureConfiguration {
    public static final Codec<TemplateTreeConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.listOf().fieldOf("templates").forGetter(TemplateTreeConfiguration::templates),
            TreeDecorator.CODEC.listOf().optionalFieldOf("decorators", List.of()).forGetter(TemplateTreeConfiguration::decorators)
    ).apply(instance, TemplateTreeConfiguration::new));
}
