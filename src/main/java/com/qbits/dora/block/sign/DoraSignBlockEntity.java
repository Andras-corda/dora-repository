package com.qbits.dora.block.sign;

import com.qbits.dora.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DoraSignBlockEntity extends SignBlockEntity {
    public DoraSignBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SIGN.get(), pos, state);
    }
}
