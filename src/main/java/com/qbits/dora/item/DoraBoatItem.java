package com.qbits.dora.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class DoraBoatItem extends Item {
    private static final Predicate<Entity> COLLIDES = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

    private final Supplier<? extends EntityType<? extends Boat>> type;

    public DoraBoatItem(Supplier<? extends EntityType<? extends Boat>> type, Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        HitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }
        Vec3 view = player.getViewVector(1.0F);
        Vec3 eye = player.getEyePosition();
        for (Entity entity : level.getEntities(player, player.getBoundingBox().expandTowards(view.scale(5.0D)).inflate(1.0D), COLLIDES)) {
            if (entity.getBoundingBox().inflate(entity.getPickRadius()).contains(eye)) {
                return InteractionResultHolder.pass(stack);
            }
        }
        Boat boat = type.get().create(level);
        if (boat == null) {
            return InteractionResultHolder.fail(stack);
        }
        Vec3 at = hit.getLocation();
        boat.setPos(at.x, at.y, at.z);
        boat.xo = at.x;
        boat.yo = at.y;
        boat.zo = at.z;
        boat.setYRot(player.getYRot());
        if (!level.noCollision(boat, boat.getBoundingBox())) {
            return InteractionResultHolder.fail(stack);
        }
        if (!level.isClientSide) {
            level.addFreshEntity(boat);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, at);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
