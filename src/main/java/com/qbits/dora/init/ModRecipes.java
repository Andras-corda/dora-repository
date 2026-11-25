package com.qbits.dora.init;

import com.qbits.dora.Dora;
import com.qbits.dora.recipe.BottleReturningRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Dora.MODID);

    public static final RegistryObject<RecipeSerializer<BottleReturningRecipe>> BOTTLE_RETURNING = SERIALIZERS.register("bottle_returning", BottleReturningRecipe.Serializer::new);
}
