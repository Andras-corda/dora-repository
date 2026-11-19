package com.qbits.dora.carving;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CarvingGeometry {
    public static Vec3 toLocal(Direction face, double u, double v, double offset) {
        return switch (face) {
            case NORTH -> new Vec3(1 - u, 1 - v, -offset);
            case SOUTH -> new Vec3(u, 1 - v, 1 + offset);
            case WEST -> new Vec3(-offset, 1 - v, u);
            case EAST -> new Vec3(1 + offset, 1 - v, 1 - u);
            case UP -> new Vec3(u, 1 + offset, v);
            case DOWN -> new Vec3(u, -offset, 1 - v);
        };
    }

    public static double[] toUv(Direction face, Vec3 local) {
        return switch (face) {
            case NORTH -> new double[]{1 - local.x, 1 - local.y};
            case SOUTH -> new double[]{local.x, 1 - local.y};
            case WEST -> new double[]{local.z, 1 - local.y};
            case EAST -> new double[]{1 - local.z, 1 - local.y};
            case UP -> new double[]{local.x, local.z};
            case DOWN -> new double[]{local.x, 1 - local.z};
        };
    }

    @Nullable
    public static int[] pixel(Direction face, Vec3 local) {
        double[] uv = toUv(face, local);
        if (uv[0] < 0 || uv[0] >= 1 || uv[1] < 0 || uv[1] >= 1) {
            return null;
        }
        return new int[]{(int) (uv[0] * Carving.SIZE), (int) (uv[1] * Carving.SIZE)};
    }
}
