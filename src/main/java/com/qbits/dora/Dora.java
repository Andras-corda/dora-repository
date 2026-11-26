package com.qbits.dora;

import com.mojang.logging.LogUtils;
import com.qbits.dora.block.WoodFlammability;
import com.qbits.dora.brewing.PotionModifierRecipe;
import com.qbits.dora.init.ModBlockEntities;
import com.qbits.dora.init.ModFeatures;
import com.qbits.dora.init.ModBlocks;
import com.qbits.dora.init.ModCreativeTabs;
import com.qbits.dora.init.ModEntities;
import com.qbits.dora.init.ModItems;
import com.qbits.dora.init.ModParticles;
import com.qbits.dora.init.ModRecipes;
import com.qbits.dora.network.ModNetwork;
import com.qbits.dora.worldgen.DoraRegion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import terrablender.api.Regions;

@Mod(Dora.MODID)
public class Dora {
    public static final String MODID = "dora";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Dora(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModCreativeTabs.TABS.register(bus);
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        ModFeatures.FEATURES.register(bus);
        ModParticles.PARTICLES.register(bus);
        ModRecipes.SERIALIZERS.register(bus);
        ModFeatures.TREE_DECORATORS.register(bus);
        ModEntities.ENTITY_TYPES.register(bus);
        ModNetwork.register();
        bus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            WoodFlammability.register();
            ComposterBlock.COMPOSTABLES.put(ModItems.LUMINA_SEEDS.get(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(ModItems.JARRAHDALE_SEEDS.get(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(ModItems.WARTY_GOBLIN_SEEDS.get(), 0.3F);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.MAPLE_SAPLING.getId(), ModBlocks.POTTED_MAPLE_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.WILLOW_SAPLING.getId(), ModBlocks.POTTED_WILLOW_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.ENGLISH_OAK_SAPLING.getId(), ModBlocks.POTTED_ENGLISH_OAK_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.WISTERIA_SAPLING.getId(), ModBlocks.POTTED_WISTERIA_SAPLING);
            Regions.register(new DoraRegion(ResourceLocation.fromNamespaceAndPath(MODID, "overworld"), 2));
            BrewingRecipeRegistry.addRecipe(PotionModifierRecipe.doubleDuration(ModItems.MAPLE_SYRUP));
            BrewingRecipeRegistry.addRecipe(PotionModifierRecipe.doubleLevel(() -> ModBlocks.WARTY_GOBLIN.get().asItem(), effect -> effect.getEffect() == MobEffects.POISON));
        });
    }
}
