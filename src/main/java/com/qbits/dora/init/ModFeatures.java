package com.qbits.dora.init;

import com.qbits.dora.Dora;
import com.qbits.dora.worldgen.BigPumpkinFeature;
import com.qbits.dora.worldgen.HangingWisteriaDecorator;
import com.qbits.dora.worldgen.LeafLitterDecorator;
import com.qbits.dora.worldgen.PondFeature;
import com.qbits.dora.worldgen.PumpkinPatchFeature;
import com.qbits.dora.worldgen.RuinedWallFeature;
import com.qbits.dora.worldgen.TemplateTreeConfiguration;
import com.qbits.dora.worldgen.TemplateTreeFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Dora.MODID);

    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, Dora.MODID);

    public static final RegistryObject<TemplateTreeFeature> TEMPLATE_TREE = FEATURES.register("template_tree", () -> new TemplateTreeFeature(TemplateTreeConfiguration.CODEC));
    public static final RegistryObject<BigPumpkinFeature> BIG_PUMPKIN = FEATURES.register("big_pumpkin", () -> new BigPumpkinFeature(BigPumpkinFeature.Config.CODEC));
    public static final RegistryObject<PumpkinPatchFeature> PUMPKIN_PATCH = FEATURES.register("pumpkin_patch", () -> new PumpkinPatchFeature(PumpkinPatchFeature.Config.CODEC));
    public static final RegistryObject<PondFeature> POND = FEATURES.register("pond", () -> new PondFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<RuinedWallFeature> RUINED_WALL = FEATURES.register("ruined_wall", () -> new RuinedWallFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<TreeDecoratorType<LeafLitterDecorator>> LEAF_LITTER = TREE_DECORATORS.register("leaf_litter", () -> new TreeDecoratorType<>(LeafLitterDecorator.CODEC));
    public static final RegistryObject<TreeDecoratorType<HangingWisteriaDecorator>> HANGING_WISTERIA = TREE_DECORATORS.register("hanging_wisteria", () -> new TreeDecoratorType<>(HangingWisteriaDecorator.CODEC));

    public static final ResourceKey<ConfiguredFeature<?, ?>> SUGAR_MAPLE = key("sugar_maple");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WEEPING_WILLOW = key("weeping_willow");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ENGLISH_OAK = key("english_oak");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WISTERIA = key("wisteria");

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Dora.MODID, name));
    }
}
