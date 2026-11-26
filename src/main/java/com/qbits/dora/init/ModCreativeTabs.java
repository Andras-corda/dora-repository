package com.qbits.dora.init;

import com.qbits.dora.Dora;
import com.qbits.dora.item.SeparatorItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Dora.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.dora.main"))
            .icon(() -> new ItemStack(Items.BOOK))
            .displayItems((params, output) -> fill(new SeparatorItem.Builder(output, ModItems.SEPARATOR.get())))
            .build());

    private static void fill(SeparatorItem.Builder builder) {
        List<RegistryObject<Item>> remaining = new ArrayList<>(ModItems.ITEMS.getEntries());
        remaining.removeIf(entry -> entry.get() == ModItems.SEPARATOR.get() || entry.get() == ModBlocks.CARVED_PUMPKIN.get().asItem());

        builder.separator("autumnal");
        take(builder, remaining, name -> name.contains("maple") && !name.equals("maple_syrup"));
        take(builder, remaining, name -> name.contains("english_oak"));
        take(builder, remaining, name -> name.contains("willow"));
        take(builder, remaining, name -> !isPumpkin(name) && !isSpring(name));

        builder.separator("pumpkin_field");
        take(builder, remaining, ModCreativeTabs::isPumpkin);

        builder.separator("spring");
        take(builder, remaining, ModCreativeTabs::isSpring);
    }

    private static boolean isPumpkin(String name) {
        return name.equals("cobweb_threads") || name.equals("big_pumpkin") || name.contains("lumina") || name.contains("warty_goblin") || name.contains("jarrahdale") && !name.endsWith("_pie");
    }

    private static boolean isSpring(String name) {
        return name.contains("wisteria");
    }

    private static void take(SeparatorItem.Builder builder, List<RegistryObject<Item>> remaining, Predicate<String> filter) {
        List<RegistryObject<Item>> taken = remaining.stream().filter(entry -> filter.test(entry.getId().getPath())).toList();
        taken.forEach(entry -> builder.accept(new ItemStack(entry.get())));
        remaining.removeAll(taken);
    }
}
