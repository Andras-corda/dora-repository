package com.qbits.dora.entity;

import com.qbits.dora.init.ModEntities;
import com.qbits.dora.init.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class DoraBoat extends Boat {
    public DoraBoat(EntityType<? extends Boat> type, Level level) {
        super(type, level);
    }

    public DoraBoat(Level level, double x, double y, double z) {
        this(ModEntities.MAPLE_BOAT.get(), level);
        setPos(x, y, z);
        xo = x;
        yo = y;
        zo = z;
    }

    @Override
    public Item getDropItem() {
        return ModItems.MAPLE_BOAT.get();
    }
}
