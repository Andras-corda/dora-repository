package com.qbits.dora.network;

import com.qbits.dora.block.StrippedMapleLogBlock;
import com.qbits.dora.item.CarvingChiselItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SapRevealPacket(BlockPos pos, Direction face) {
    private static final double MAX_DISTANCE_SQR = 8 * 8;

    public SapRevealPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readEnum(Direction.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeEnum(face);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        context.get().setPacketHandled(true);
        if (player == null) {
            return;
        }
        ServerLevel level = player.serverLevel();
        if (!level.isLoaded(pos) || player.distanceToSqr(Vec3.atCenterOf(pos)) > MAX_DISTANCE_SQR) {
            return;
        }
        if (!(player.getMainHandItem().getItem() instanceof CarvingChiselItem) && !(player.getOffhandItem().getItem() instanceof CarvingChiselItem)) {
            return;
        }
        BlockState state = level.getBlockState(pos);
        if (!StrippedMapleLogBlock.canTap(state, face)) {
            return;
        }
        int sap = 1 + level.random.nextInt(StrippedMapleLogBlock.MAX_SAP);
        level.setBlock(pos, state.setValue(StrippedMapleLogBlock.SAP, sap), Block.UPDATE_ALL);
        level.playSound(null, pos, SoundEvents.HONEY_BLOCK_SLIDE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
