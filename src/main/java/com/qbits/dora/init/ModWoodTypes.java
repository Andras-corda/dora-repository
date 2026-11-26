package com.qbits.dora.init;

import com.qbits.dora.Dora;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {
    // Autumn
    public static final WoodType MAPLE
            = WoodType.register(new WoodType(Dora.MODID + ":maple", BlockSetType.OAK));

    public static final WoodType DRAINED_MAPLE
            = WoodType.register(new WoodType(Dora.MODID + ":drained_maple", BlockSetType.OAK));

    public static final WoodType ENGLISH_OAK
            = WoodType.register(new WoodType(Dora.MODID + ":english_oak", BlockSetType.OAK));

    //    public static final WoodType WEEPING_WILLOW
    //            = WoodType.register(new WoodType(Dora.MODID + ":weeping_willow", BlockSetType.OAK));

    // String
    public static final WoodType WISTERIA
            = WoodType.register(new WoodType(Dora.MODID + ":wisteria", BlockSetType.OAK));
}
