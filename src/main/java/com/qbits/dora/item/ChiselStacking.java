package com.qbits.dora.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ChiselStacking {
    private static final String DAMAGE = "Damage";

    public static boolean isStackable(ItemStack stack) {
        if (stack.getItem() instanceof CarvingChiselItem) {
            return stack.getMaxStackSize() > 1;
        }
        return stack.getMaxStackSize() > 1 && (!stack.isDamageableItem() || !stack.isDamaged());
    }

    public static boolean isSameItemSameTags(ItemStack first, ItemStack second) {
        if (!first.is(second.getItem())) {
            return false;
        }
        if (first.isEmpty() && second.isEmpty()) {
            return true;
        }
        boolean sameTags = first.getItem() instanceof CarvingChiselItem
                ? Objects.equals(withoutDamage(first.getTag()), withoutDamage(second.getTag()))
                : Objects.equals(first.getTag(), second.getTag());
        return sameTags && first.areCapsCompatible(second);
    }

    private static CompoundTag withoutDamage(CompoundTag tag) {
        if (tag == null || !tag.contains(DAMAGE)) {
            return tag;
        }
        CompoundTag copy = tag.copy();
        copy.remove(DAMAGE);
        return copy.isEmpty() ? null : copy;
    }
}
