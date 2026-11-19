package com.qbits.dora.client;

import com.qbits.dora.block.CarvablePumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CarvingColors {
    private static final Palette UNLIT = new Palette(0x2D0003, 0x441300, 0x441300, 0xFFFFFF);
    private static final Palette LIT = new Palette(0xFFC264, 0xFFE156, 0xFEFFA1, 0x441300);

    public static Palette of(BlockState state) {
        return state.hasProperty(CarvablePumpkinBlock.LIT) && state.getValue(CarvablePumpkinBlock.LIT) ? LIT : UNLIT;
    }

    public record Palette(int shadow, int edge, int core, int cursor) {
    }
}
