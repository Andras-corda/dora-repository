package com.qbits.dora.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class DoraBoat extends Boat {
    private final Supplier<? extends Item> drop;

    public DoraBoat(EntityType<? extends Boat> type, Level level, Supplier<? extends Item> drop) {
        super(type, level);
        this.drop = drop;
    }

    @Override
    public Item getDropItem() {
        return drop.get();
    }
}
