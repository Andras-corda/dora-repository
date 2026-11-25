package com.qbits.dora.loot;

import com.qbits.dora.Dora;
import com.qbits.dora.init.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Dora.MODID)
public class LootEvents {
    private static final int WITCH_DROP_ENTRIES = 7;

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (!event.getName().equals(EntityType.WITCH.getDefaultLootTable())) {
            return;
        }
        event.getTable().addPool(LootPool.lootPool()
                .name(Dora.MODID + ":poisoned_apple")
                .setRolls(UniformGenerator.between(1.0F, 3.0F))
                .add(LootItem.lootTableItem(ModItems.POISONED_APPLE.get())
                        .setWeight(1)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                .add(EmptyLootItem.emptyItem().setWeight(WITCH_DROP_ENTRIES - 1))
                .build());
    }
}
