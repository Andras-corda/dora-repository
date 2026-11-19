package com.qbits.dora.sap;

import com.qbits.dora.carving.Carving;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

import java.util.BitSet;

public class SapPattern {
    public static BitSet generate(BlockPos pos, Direction face) {
        RandomSource random = RandomSource.create(pos.asLong() * 31 + face.ordinal());
        BitSet spots = new BitSet(Carving.SIZE * Carving.SIZE);
        int blobs = 2 + random.nextInt(3);
        for (int i = 0; i < blobs; i++) {
            int cx = 2 + random.nextInt(Carving.SIZE - 4);
            int cy = 2 + random.nextInt(Carving.SIZE - 4);
            float rx = 1.5F + random.nextInt(2);
            float ry = 1.5F + random.nextInt(3);
            for (int y = (int) (cy - ry); y <= cy + ry; y++) {
                for (int x = (int) (cx - rx); x <= cx + rx; x++) {
                    float dx = (x - cx) / rx;
                    float dy = (y - cy) / ry;
                    if (x >= 0 && y >= 0 && x < Carving.SIZE && y < Carving.SIZE && dx * dx + dy * dy <= 1) {
                        spots.set(y * Carving.SIZE + x);
                    }
                }
            }
        }
        return spots;
    }
}
