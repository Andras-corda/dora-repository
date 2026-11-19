package com.qbits.dora.init;

import com.qbits.dora.Dora;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Dora.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.dora.main"))
            .icon(() -> new ItemStack(Items.BOOK))
            .displayItems((params, output) -> ModItems.ITEMS.getEntries().stream()
                    .map(RegistryObject::get)
                    .filter(item -> item != ModBlocks.CARVED_PUMPKIN.get().asItem())
                    .forEach(output::accept))
            .build());
}
