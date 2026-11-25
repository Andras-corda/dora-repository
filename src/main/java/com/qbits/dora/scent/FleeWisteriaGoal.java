package com.qbits.dora.scent;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FleeWisteriaGoal extends Goal {
    private static final int CHECK_INTERVAL = 10;
    private static final int ATTEMPTS = 16;
    private static final double SPEED = 1.2;

    private final PathfinderMob mob;
    @Nullable
    private Vec3 escape;

    public FleeWisteriaGoal(PathfinderMob mob) {
        this.mob = mob;
        setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (mob.tickCount % CHECK_INTERVAL != 0 || !WisteriaScent.isScented(mob.level(), mob.blockPosition())) {
            return false;
        }
        escape = findEscape();
        return escape != null;
    }

    @Override
    public boolean canContinueToUse() {
        return !mob.getNavigation().isDone() && WisteriaScent.isScented(mob.level(), mob.blockPosition());
    }

    @Override
    public void start() {
        mob.setTarget(null);
        if (escape != null) {
            mob.getNavigation().moveTo(escape.x, escape.y, escape.z, SPEED);
        }
    }

    @Override
    public void tick() {
        if (mob.getTarget() != null) {
            mob.setTarget(null);
        }
    }

    @Override
    public void stop() {
        escape = null;
    }

    @Nullable
    private Vec3 findEscape() {
        Vec3 fallback = null;
        for (int i = 0; i < ATTEMPTS; i++) {
            Vec3 candidate = DefaultRandomPos.getPos(mob, 24, 7);
            if (candidate == null) {
                continue;
            }
            if (!WisteriaScent.isScented(mob.level(), BlockPos.containing(candidate))) {
                return candidate;
            }
            if (fallback == null || candidate.distanceToSqr(mob.position()) > fallback.distanceToSqr(mob.position())) {
                fallback = candidate;
            }
        }
        return fallback;
    }
}
