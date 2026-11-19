package com.qbits.dora.carving;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class Carving {
    public static final int SIZE = 16;

    private final EnumMap<Direction, BitSet> faces = new EnumMap<>(Direction.class);
    private final EnumMap<Direction, BitSet> shadows = new EnumMap<>(Direction.class);
    private final EnumMap<Direction, BitSet> cores = new EnumMap<>(Direction.class);

    public boolean isCarved(Direction face, int x, int y) {
        BitSet bits = faces.get(face);
        return bits != null && bits.get(y * SIZE + x);
    }

    public boolean carve(Direction face, int x, int y) {
        BitSet bits = faces.computeIfAbsent(face, f -> new BitSet(SIZE * SIZE));
        int index = y * SIZE + x;
        if (bits.get(index)) {
            return false;
        }
        bits.set(index);
        invalidate(face);
        return true;
    }

    public boolean erase(Direction face, int x, int y) {
        BitSet bits = faces.get(face);
        int index = y * SIZE + x;
        if (bits == null || !bits.get(index)) {
            return false;
        }
        bits.clear(index);
        invalidate(face);
        if (bits.isEmpty()) {
            faces.remove(face);
        }
        return true;
    }

    public boolean fill(Direction face, int x0, int y0, int x1, int y1, boolean carve) {
        boolean changed = false;
        for (int y = Math.min(y0, y1); y <= Math.max(y0, y1); y++) {
            for (int x = Math.min(x0, x1); x <= Math.max(x0, x1); x++) {
                changed |= carve ? carve(face, x, y) : erase(face, x, y);
            }
        }
        return changed;
    }

    public boolean isEmpty() {
        return faces.isEmpty();
    }

    public Map<Direction, BitSet> faces() {
        return Collections.unmodifiableMap(faces);
    }

    public BitSet shadows(Direction face) {
        return shadows.computeIfAbsent(face, f -> findShadows(bits(f)));
    }

    public BitSet cores(Direction face) {
        return cores.computeIfAbsent(face, f -> findCores(bits(f), shadows(f)));
    }

    private BitSet bits(Direction face) {
        return faces.getOrDefault(face, new BitSet());
    }

    private void invalidate(Direction face) {
        shadows.remove(face);
        cores.remove(face);
    }

    private static BitSet findShadows(BitSet bits) {
        BitSet result = new BitSet(SIZE * SIZE);
        for (int index = bits.nextSetBit(0); index >= 0; index = bits.nextSetBit(index + 1)) {
            int x = index % SIZE;
            int y = index / SIZE;
            if (!isSet(bits, x, y - 1) || !isSet(bits, x - 1, y - 1)) {
                result.set(index);
            }
        }
        return result;
    }

    private static BitSet findCores(BitSet bits, BitSet shadows) {
        BitSet result = new BitSet(SIZE * SIZE);
        for (int index = bits.nextSetBit(0); index >= 0; index = bits.nextSetBit(index + 1)) {
            int x = index % SIZE;
            int y = index / SIZE;
            if (!shadows.get(index)
                    && isSet(bits, x, y - 1) && !isSet(shadows, x, y - 1)
                    && isSet(bits, x - 1, y) && !isSet(shadows, x - 1, y)) {
                result.set(index);
            }
        }
        return result;
    }

    private static boolean isSet(BitSet bits, int x, int y) {
        return x >= 0 && y >= 0 && x < SIZE && y < SIZE && bits.get(y * SIZE + x);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        faces.forEach((face, bits) -> tag.putLongArray(face.getName(), bits.toLongArray()));
        return tag;
    }

    public static Carving load(CompoundTag tag) {
        Carving carving = new Carving();
        for (Direction face : Direction.values()) {
            if (tag.contains(face.getName())) {
                BitSet bits = BitSet.valueOf(tag.getLongArray(face.getName()));
                if (!bits.isEmpty()) {
                    carving.faces.put(face, bits);
                }
            }
        }
        return carving;
    }
}
