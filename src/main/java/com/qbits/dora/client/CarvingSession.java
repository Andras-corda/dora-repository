package com.qbits.dora.client;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class CarvingSession {
    private static final float TRANSITION_MS = 300F;

    @Nullable
    private static CarvingSession current;

    public final BlockPos pos;
    public final Direction face;
    @Nullable
    public int[] hovered;
    @Nullable
    public int[] selection;
    @Nullable
    public SapProgress sap;
    private final long openedAt = Util.getMillis();
    private long closedAt = -1;
    private float progressAtClose;

    private CarvingSession(BlockPos pos, Direction face) {
        this.pos = pos;
        this.face = face;
    }

    public static void start(BlockPos pos, Direction face) {
        current = new CarvingSession(pos, face);
    }

    public static void stop() {
        if (current != null && current.isOpen()) {
            current.progressAtClose = current.progress();
            current.closedAt = Util.getMillis();
        }
    }

    @Nullable
    public static CarvingSession current() {
        if (current != null && !current.isOpen() && current.progress() <= 0) {
            current = null;
        }
        return current;
    }

    @Nullable
    public static CarvingSession open() {
        CarvingSession session = current();
        return session != null && session.isOpen() ? session : null;
    }

    public boolean isOpen() {
        return closedAt < 0;
    }

    public float progress() {
        long now = Util.getMillis();
        float linear = isOpen()
                ? Mth.clamp((now - openedAt) / TRANSITION_MS, 0, 1)
                : progressAtClose * Mth.clamp(1 - (now - closedAt) / TRANSITION_MS, 0, 1);
        return linear * linear * (3 - 2 * linear);
    }
}
