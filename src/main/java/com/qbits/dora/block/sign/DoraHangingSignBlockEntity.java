package com.qbits.dora.block.sign;

import com.qbits.dora.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DoraHangingSignBlockEntity extends HangingSignBlockEntity {
    public DoraHangingSignBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HANGING_SIGN.get(), pos, state);
    }
}
