package com.qbits.dora.init;

import com.qbits.dora.Dora;
import com.qbits.dora.entity.DoraBoat;
import com.qbits.dora.entity.DoraChestBoat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Dora.MODID);

    public static final RegistryObject<EntityType<DoraBoat>> MAPLE_BOAT = ENTITY_TYPES.register("maple_boat", () ->
            EntityType.Builder.<DoraBoat>of(DoraBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("maple_boat"));
    public static final RegistryObject<EntityType<DoraChestBoat>> MAPLE_CHEST_BOAT = ENTITY_TYPES.register("maple_chest_boat", () ->
            EntityType.Builder.<DoraChestBoat>of(DoraChestBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("maple_chest_boat"));
}
