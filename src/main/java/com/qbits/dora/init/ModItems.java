package com.qbits.dora.init;

import com.qbits.dora.Dora;
import com.qbits.dora.item.CarvingChiselItem;
import com.qbits.dora.item.DoraBoatItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Dora.MODID);

    public static final RegistryObject<Item> ROASTED_LUMINA = ITEMS.register("roasted_lumina", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .nutrition(5).saturationMod(0.6F)
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 15 * 20), 1.0F)
            .build())));

    public static final RegistryObject<Item> GOLDEN_LUMINA = ITEMS.register("golden_lumina", () -> new Item(new Item.Properties().rarity(Rarity.RARE).food(new FoodProperties.Builder()
            .nutrition(6).saturationMod(1.2F).alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 30 * 20), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 5 * 20), 1.0F)
            .build())));

    public static final RegistryObject<Item> JARRAHDALE_PIE = ITEMS.register("jarrahdale_pie", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.3F)
            .build())));

    public static final RegistryObject<Item> PUMPKIN_ON_A_STICK = ITEMS.register("pumpkin_on_a_stick", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .nutrition(3).saturationMod(0.4F).fast()
            .build())));

    public static final RegistryObject<Item> PUMPKIN_SOUP = ITEMS.register("pumpkin_soup", () -> new BowlFoodItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder()
            .nutrition(7).saturationMod(0.6F)
            .build())));

    public static final RegistryObject<Item> MAPLE_SYRUP = ITEMS.register("maple_syrup", () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> IRON_CARVING_CHISEL = ITEMS.register("iron_carving_chisel", () -> new CarvingChiselItem(Tiers.IRON, 3, new Item.Properties()));
    public static final RegistryObject<Item> GOLDEN_CARVING_CHISEL = ITEMS.register("golden_carving_chisel", () -> new CarvingChiselItem(Tiers.GOLD, 2, new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_CARVING_CHISEL = ITEMS.register("diamond_carving_chisel", () -> new CarvingChiselItem(Tiers.DIAMOND, 5, new Item.Properties()));

    public static final RegistryObject<Item> MAPLE_SIGN = ITEMS.register("maple_sign", () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.MAPLE_SIGN.get(), ModBlocks.MAPLE_WALL_SIGN.get()));
    public static final RegistryObject<Item> MAPLE_HANGING_SIGN = ITEMS.register("maple_hanging_sign", () -> new HangingSignItem(ModBlocks.MAPLE_HANGING_SIGN.get(), ModBlocks.MAPLE_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> MAPLE_BOAT = ITEMS.register("maple_boat", () -> new DoraBoatItem(false, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MAPLE_CHEST_BOAT = ITEMS.register("maple_chest_boat", () -> new DoraBoatItem(true, new Item.Properties().stacksTo(1)));
}
