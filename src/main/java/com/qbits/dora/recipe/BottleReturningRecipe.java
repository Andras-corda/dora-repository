package com.qbits.dora.recipe;

import com.google.gson.JsonObject;
import com.qbits.dora.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.Nullable;

public class BottleReturningRecipe extends ShapelessRecipe {
    public BottleReturningRecipe(ShapelessRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getResultItem(RegistryAccess.EMPTY), recipe.getIngredients());
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remaining = super.getRemainingItems(container);
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION)) {
                remaining.set(i, new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        return remaining;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BOTTLE_RETURNING.get();
    }

    public static class Serializer implements RecipeSerializer<BottleReturningRecipe> {
        @Override
        public BottleReturningRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new BottleReturningRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromJson(id, json));
        }

        @Override
        public @Nullable BottleReturningRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ShapelessRecipe recipe = RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(id, buf);
            return recipe == null ? null : new BottleReturningRecipe(recipe);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BottleReturningRecipe recipe) {
            RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buf, recipe);
        }
    }
}
