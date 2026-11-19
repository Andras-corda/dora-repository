package com.qbits.dora.network;

import com.qbits.dora.block.CarvablePumpkinBlock;
import com.qbits.dora.carving.Carving;
import com.qbits.dora.carving.CarvingBlockEntity;
import com.qbits.dora.init.ModBlocks;
import com.qbits.dora.init.ModTags;
import com.qbits.dora.item.CarvingChiselItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record CarvePixelPacket(BlockPos pos, Direction face, int x0, int y0, int x1, int y1, boolean carve) {
    private static final double MAX_DISTANCE_SQR = 8 * 8;

    public CarvePixelPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readEnum(Direction.class), buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeEnum(face);
        buf.writeByte(x0);
        buf.writeByte(y0);
        buf.writeByte(x1);
        buf.writeByte(y1);
        buf.writeBoolean(carve);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        context.get().setPacketHandled(true);
        if (player == null || Math.max(Math.max(x0, y0), Math.max(x1, y1)) >= Carving.SIZE) {
            return;
        }
        ServerLevel level = player.serverLevel();
        if (!level.isLoaded(pos) || !level.getBlockState(pos).is(ModTags.CARVABLE)) {
            return;
        }
        if (player.distanceToSqr(Vec3.atCenterOf(pos)) > MAX_DISTANCE_SQR) {
            return;
        }
        if (!(player.getMainHandItem().getItem() instanceof CarvingChiselItem) && !(player.getOffhandItem().getItem() instanceof CarvingChiselItem)) {
            return;
        }
        if (level.getBlockState(pos).is(Blocks.PUMPKIN)) {
            if (!carve) {
                return;
            }
            level.setBlock(pos, ModBlocks.CARVED_PUMPKIN.get().defaultBlockState(), Block.UPDATE_ALL);
        }
        if (!(level.getBlockEntity(pos) instanceof CarvingBlockEntity entity)) {
            return;
        }
        Carving carving = entity.getCarving();
        if (!carving.fill(face, x0, y0, x1, y1, carve)) {
            return;
        }
        if (carving.isEmpty()) {
            onEmptied(level, entity.getBlockState());
        }
        entity.sync();
    }

    private void onEmptied(ServerLevel level, BlockState state) {
        if (state.getValue(CarvablePumpkinBlock.LIT)) {
            Block.popResource(level, pos, new ItemStack(Items.TORCH));
            CarvablePumpkinBlock.extinguish(level, pos, state);
        }
        if (state.is(ModBlocks.CARVED_PUMPKIN.get())) {
            level.setBlock(pos, Blocks.PUMPKIN.defaultBlockState(), Block.UPDATE_ALL);
        }
    }
}
