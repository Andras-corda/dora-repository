package com.qbits.dora.brewing;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class PotionModifierRecipe implements IBrewingRecipe {
    private final Supplier<? extends Item> ingredient;
    private final String marker;
    private final Predicate<MobEffectInstance> affects;
    private final UnaryOperator<MobEffectInstance> modifier;

    public PotionModifierRecipe(Supplier<? extends Item> ingredient, String marker, Predicate<MobEffectInstance> affects, UnaryOperator<MobEffectInstance> modifier) {
        this.ingredient = ingredient;
        this.marker = marker;
        this.affects = affects;
        this.modifier = modifier;
    }

    public static PotionModifierRecipe doubleDuration(Supplier<? extends Item> ingredient) {
        return new PotionModifierRecipe(ingredient, "dora:doubled_duration",
                effect -> !effect.getEffect().isInstantenous(),
                effect -> new MobEffectInstance(effect.getEffect(), effect.getDuration() * 2, effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon()));
    }

    public static PotionModifierRecipe doubleLevel(Supplier<? extends Item> ingredient, Predicate<MobEffectInstance> affects) {
        return new PotionModifierRecipe(ingredient, "dora:doubled_level", affects,
                effect -> new MobEffectInstance(effect.getEffect(), effect.getDuration(), (effect.getAmplifier() + 1) * 2 - 1, effect.isAmbient(), effect.isVisible(), effect.showIcon()));
    }

    @Override
    public boolean isInput(ItemStack input) {
        if (!input.is(Items.POTION) && !input.is(Items.SPLASH_POTION) && !input.is(Items.LINGERING_POTION)) {
            return false;
        }
        if (input.hasTag() && input.getTag().getBoolean(marker)) {
            return false;
        }
        return PotionUtils.getMobEffects(input).stream().anyMatch(affects);
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return stack.is(ingredient.get());
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack stack) {
        if (!isInput(input) || !isIngredient(stack)) {
            return ItemStack.EMPTY;
        }
        List<MobEffectInstance> effects = PotionUtils.getMobEffects(input).stream()
                .map(effect -> affects.test(effect) ? modifier.apply(effect) : effect)
                .toList();
        Component name = input.getHoverName().copy().withStyle(style -> style.withItalic(false));
        int color = PotionUtils.getColor(input);

        ItemStack output = input.copy();
        output.setCount(1);
        PotionUtils.setPotion(output, Potions.EMPTY);
        output.removeTagKey(PotionUtils.TAG_CUSTOM_POTION_EFFECTS);
        PotionUtils.setCustomEffects(output, effects);
        output.getOrCreateTag().putInt(PotionUtils.TAG_CUSTOM_POTION_COLOR, color);
        output.getOrCreateTag().putBoolean(marker, true);
        output.setHoverName(name);
        return output;
    }
}
