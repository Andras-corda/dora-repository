package com.qbits.dora.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SeparatorItem extends Item {
    public static final int ROW = 9;
    private static final String NAME = "Separator";
    private static final String PART = "Part";
    private static final String FILLER = "Filler";

    public SeparatorItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static boolean isSeparator(ItemStack stack) {
        return stack.getItem() instanceof SeparatorItem;
    }

    @Nullable
    public static String name(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return isSeparator(stack) && tag != null && tag.contains(NAME) && tag.getInt(PART) == 0 ? tag.getString(NAME) : null;
    }

    public static class Builder {
        private final CreativeModeTab.Output output;
        private final Item item;
        private int count;
        private int fillers;

        public Builder(CreativeModeTab.Output output, Item item) {
            this.output = output;
            this.item = item;
        }

        public void accept(ItemStack stack) {
            output.accept(stack);
            count++;
        }

        public void separator(String name) {
            while (count % ROW != 0) {
                CompoundTag tag = new CompoundTag();
                tag.putInt(FILLER, fillers++);
                hidden(tag);
            }
            for (int part = 0; part < ROW; part++) {
                CompoundTag tag = new CompoundTag();
                tag.putString(NAME, name);
                tag.putInt(PART, part);
                hidden(tag);
            }
        }

        private void hidden(CompoundTag tag) {
            ItemStack stack = new ItemStack(item);
            stack.setTag(tag);
            output.accept(stack, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            count++;
        }
    }
}
