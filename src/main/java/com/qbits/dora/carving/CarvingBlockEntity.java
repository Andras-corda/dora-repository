package com.qbits.dora.carving;

import com.qbits.dora.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CarvingBlockEntity extends BlockEntity {
    public static final String TAG = "Carving";

    private Carving carving = new Carving();

    public CarvingBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CARVING.get(), pos, state);
    }

    public Carving getCarving() {
        return carving;
    }

    public void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        carving = tag.contains(TAG) ? Carving.load(tag.getCompound(TAG)) : new Carving();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!carving.isEmpty()) {
            tag.put(TAG, carving.save());
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
