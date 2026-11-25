package com.qbits.dora.init;

import com.qbits.dora.Dora;
import com.qbits.dora.entity.DoraBoat;
import com.qbits.dora.entity.DoraChestBoat;
import com.qbits.dora.entity.Manul;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Dora.MODID);

    public static final RegistryObject<EntityType<DoraBoat>> MAPLE_BOAT = boat("maple_boat", () -> ModItems.MAPLE_BOAT.get());
    public static final RegistryObject<EntityType<DoraChestBoat>> MAPLE_CHEST_BOAT = chestBoat("maple_chest_boat", () -> ModItems.MAPLE_CHEST_BOAT.get());
    public static final RegistryObject<EntityType<DoraBoat>> ENGLISH_OAK_BOAT = boat("english_oak_boat", () -> ModItems.ENGLISH_OAK_BOAT.get());
    public static final RegistryObject<EntityType<DoraChestBoat>> ENGLISH_OAK_CHEST_BOAT = chestBoat("english_oak_chest_boat", () -> ModItems.ENGLISH_OAK_CHEST_BOAT.get());

    public static final RegistryObject<EntityType<DoraBoat>> DRAINED_MAPLE_BOAT = boat("drained_maple_boat", () -> ModItems.DRAINED_MAPLE_BOAT.get());
    public static final RegistryObject<EntityType<DoraChestBoat>> DRAINED_MAPLE_CHEST_BOAT = chestBoat("drained_maple_chest_boat", () -> ModItems.DRAINED_MAPLE_CHEST_BOAT.get());

    public static final RegistryObject<EntityType<Manul>> MANUL = ENTITY_TYPES.register("manul", () ->
            EntityType.Builder.of(Manul::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(10).build("manul"));

    private static RegistryObject<EntityType<DoraBoat>> boat(String name, Supplier<Item> drop) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.<DoraBoat>of((type, level) -> new DoraBoat(type, level, drop), MobCategory.MISC)
                .sized(1.375F, 0.5625F).clientTrackingRange(10).build(name));
    }

    private static RegistryObject<EntityType<DoraChestBoat>> chestBoat(String name, Supplier<Item> drop) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.<DoraChestBoat>of((type, level) -> new DoraChestBoat(type, level, drop), MobCategory.MISC)
                .sized(1.375F, 0.5625F).clientTrackingRange(10).build(name));
    }
}
