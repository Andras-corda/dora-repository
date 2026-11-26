package com.qbits.dora.init;

import com.qbits.dora.Dora;
import com.qbits.dora.item.CarvingChiselItem;
import com.qbits.dora.item.DoraBoatItem;
import com.qbits.dora.item.SeparatorItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Dora.MODID);

    // Foods
    public static final RegistryObject<Item> JARRAHDALE_PIE
            = ITEMS.register("jarrahdale_pie", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder()
            .nutrition(8).saturationMod(0.3F)
            .build())));

    public static final RegistryObject<Item> PUMPKIN_ON_A_STICK
            = ITEMS.register("pumpkin_on_a_stick", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder()
            .nutrition(3).saturationMod(0.4F).fast()
            .build())));

    public static final RegistryObject<Item> PUMPKIN_SOUP
            = ITEMS.register("pumpkin_soup", () -> new BowlFoodItem(new Item.Properties()
            .stacksTo(1)
            .food(new FoodProperties.Builder()
            .nutrition(7).saturationMod(0.6F)
            .build())));

    public static final RegistryObject<Item> POISONED_APPLE
            = ITEMS.register("poisoned_apple", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder()
            .nutrition(4).saturationMod(0.3F)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 10 * 20), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 8 * 20), 1.0F)
            .build())));

    public static final RegistryObject<Item> MAPLE_SYRUP
            = ITEMS.register("maple_syrup", () -> new HoneyBottleItem(new Item.Properties()
            .craftRemainder(Items.GLASS_BOTTLE)
            .food(Foods.HONEY_BOTTLE)
            .stacksTo(16)
    ));

    // Carving chisel
    public static final RegistryObject<Item> IRON_CARVING_CHISEL
            = ITEMS.register("iron_carving_chisel", () -> new CarvingChiselItem(Tiers.IRON, 3, new Item.Properties()));

    public static final RegistryObject<Item> GOLDEN_CARVING_CHISEL
            = ITEMS.register("golden_carving_chisel", () -> new CarvingChiselItem(Tiers.GOLD, 2, new Item.Properties()));

    public static final RegistryObject<Item> DIAMOND_CARVING_CHISEL
            = ITEMS.register("diamond_carving_chisel", () -> new CarvingChiselItem(Tiers.DIAMOND, 5, new Item.Properties()));

    // Maple Sign/Boat
    public static final RegistryObject<Item> MAPLE_SIGN
            = ITEMS.register("maple_sign", () -> new SignItem(new Item.Properties()
            .stacksTo(16),
            ModBlocks.MAPLE_SIGN.get(),
            ModBlocks.MAPLE_WALL_SIGN.get()));

    public static final RegistryObject<Item> MAPLE_HANGING_SIGN
            = ITEMS.register("maple_hanging_sign", () -> new HangingSignItem(ModBlocks.MAPLE_HANGING_SIGN.get(),
            ModBlocks.MAPLE_WALL_HANGING_SIGN.get(),
            new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> MAPLE_BOAT
            = ITEMS.register("maple_boat", () -> new DoraBoatItem(ModEntities.MAPLE_BOAT, new Item.Properties()
            .stacksTo(1)));

    public static final RegistryObject<Item> MAPLE_CHEST_BOAT
            = ITEMS.register("maple_chest_boat", () -> new DoraBoatItem(ModEntities.MAPLE_CHEST_BOAT,
            new Item.Properties().stacksTo(1)));

    // English Oak Sign/Boat
    public static final RegistryObject<Item> ENGLISH_OAK_SIGN
            = ITEMS.register("english_oak_sign", () -> new SignItem(new Item.Properties()
            .stacksTo(16),
            ModBlocks.ENGLISH_OAK_SIGN.get(),
            ModBlocks.ENGLISH_OAK_WALL_SIGN.get()));

    public static final RegistryObject<Item> ENGLISH_OAK_HANGING_SIGN
            = ITEMS.register("english_oak_hanging_sign", () -> new HangingSignItem(ModBlocks.ENGLISH_OAK_HANGING_SIGN.get(),
            ModBlocks.ENGLISH_OAK_WALL_HANGING_SIGN.get(),
            new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> ENGLISH_OAK_BOAT
            = ITEMS.register("english_oak_boat", () -> new DoraBoatItem(ModEntities.ENGLISH_OAK_BOAT,
            new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> ENGLISH_OAK_CHEST_BOAT
            = ITEMS.register("english_oak_chest_boat", () -> new DoraBoatItem(ModEntities.ENGLISH_OAK_CHEST_BOAT,
            new Item.Properties().stacksTo(1)));

    // Wisteria Sign/Boat
    public static final RegistryObject<Item> WISTERIA_SIGN
            = ITEMS.register("wisteria_sign", () -> new SignItem(new Item.Properties()
            .stacksTo(16),
            ModBlocks.WISTERIA_SIGN.get(),
            ModBlocks.WISTERIA_WALL_SIGN.get()));

    public static final RegistryObject<Item> WISTERIA_HANGING_SIGN
            = ITEMS.register("wisteria_hanging_sign", () -> new HangingSignItem(ModBlocks.WISTERIA_HANGING_SIGN.get(),
            ModBlocks.WISTERIA_WALL_HANGING_SIGN.get(),
            new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> WISTERIA_BOAT
            = ITEMS.register("wisteria_boat", () -> new DoraBoatItem(ModEntities.WISTERIA_BOAT, new Item.Properties()
            .stacksTo(1)));

    public static final RegistryObject<Item> WISTERIA_CHEST_BOAT
            = ITEMS.register("wisteria_chest_boat", () -> new DoraBoatItem(ModEntities.WISTERIA_CHEST_BOAT, new Item.Properties()
            .stacksTo(1)));

    // Drained Maple Sign/Boat
    public static final RegistryObject<Item> DRAINED_MAPLE_SIGN
            = ITEMS.register("drained_maple_sign", () -> new SignItem(new Item.Properties().stacksTo(16),
            ModBlocks.DRAINED_MAPLE_SIGN.get(),
            ModBlocks.DRAINED_MAPLE_WALL_SIGN.get()));

    public static final RegistryObject<Item> DRAINED_MAPLE_HANGING_SIGN
            = ITEMS.register("drained_maple_hanging_sign", () -> new HangingSignItem(ModBlocks.DRAINED_MAPLE_HANGING_SIGN.get(),
            ModBlocks.DRAINED_MAPLE_WALL_HANGING_SIGN.get(),
            new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> DRAINED_MAPLE_BOAT
            = ITEMS.register("drained_maple_boat", () -> new DoraBoatItem(ModEntities.DRAINED_MAPLE_BOAT, new Item.Properties()
            .stacksTo(1)));

    public static final RegistryObject<Item> DRAINED_MAPLE_CHEST_BOAT
            = ITEMS.register("drained_maple_chest_boat", () -> new DoraBoatItem(ModEntities.DRAINED_MAPLE_CHEST_BOAT, new Item.Properties()
            .stacksTo(1)));

    // Pumpkin seeds
    public static final RegistryObject<Item> LUMINA_SEEDS
            = ITEMS.register("lumina_seeds", () -> new ItemNameBlockItem(ModBlocks.LUMINA_STEM.get(),
            new Item.Properties()));

    public static final RegistryObject<Item> JARRAHDALE_SEEDS
            = ITEMS.register("jarrahdale_seeds", () -> new ItemNameBlockItem(ModBlocks.JARRAHDALE_STEM.get(),
            new Item.Properties()));

    public static final RegistryObject<Item> WARTY_GOBLIN_SEEDS
            = ITEMS.register("warty_goblin_seeds", () -> new ItemNameBlockItem(ModBlocks.WARTY_GOBLIN_STEM.get(),
            new Item.Properties()));

    // Pallas cat
    public static final RegistryObject<Item> MANUL_SPAWN_EGG
            = ITEMS.register("manul_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.MANUL,
            0x9C8F82,
            0x4E4239,
            new Item.Properties()));

    // Separateur
    public static final RegistryObject<Item> SEPARATOR
            = ITEMS.register("separator", () -> new SeparatorItem(new Item.Properties()));
}
