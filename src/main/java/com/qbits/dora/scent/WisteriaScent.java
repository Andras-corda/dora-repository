package com.qbits.dora.scent;

import com.qbits.dora.Dora;
import com.qbits.dora.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Dora.MODID)
public class WisteriaScent {
    public static boolean isScented(Level level, BlockPos pos) {
        return level.isLoaded(pos) && level.getBiome(pos).is(ModTags.WISTERIA_SCENT);
    }

    public static boolean isRepelled(Entity entity) {
        return entity instanceof LivingEntity living && living.getMobType() == MobType.UNDEAD;
    }

    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        MobSpawnType type = event.getSpawnType();
        boolean natural = type == MobSpawnType.NATURAL || type == MobSpawnType.CHUNK_GENERATION
                || type == MobSpawnType.PATROL || type == MobSpawnType.REINFORCEMENT;
        if (natural && isRepelled(event.getEntity()) && isScented(event.getEntity().level(), event.getEntity().blockPosition())) {
            event.setSpawnCancelled(true);
        }
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide && event.getEntity() instanceof PathfinderMob mob && isRepelled(mob)) {
            mob.goalSelector.addGoal(0, new FleeWisteriaGoal(mob));
        }
    }

    @SubscribeEvent
    public static void onChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity mob = event.getEntity();
        LivingEntity target = event.getNewTarget();
        if (target == null || mob.level().isClientSide || !isRepelled(mob)) {
            return;
        }
        if (isScented(mob.level(), mob.blockPosition()) || isScented(mob.level(), target.blockPosition())) {
            event.setCanceled(true);
        }
    }
}
