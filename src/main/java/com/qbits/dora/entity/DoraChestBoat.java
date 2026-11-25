package com.qbits.dora.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class DoraChestBoat extends ChestBoat {
    private final Supplier<? extends Item> drop;

    public DoraChestBoat(EntityType<? extends Boat> type, Level level, Supplier<? extends Item> drop) {
        super(type, level);
        this.drop = drop;
    }

    @Override
    public Item getDropItem() {
        return drop.get();
    }
}
