package com.qbits.dora.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;

public class ClientHooks {
    public static void openCarving(BlockPos pos, Direction face, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new CarvingScreen(pos, face, hand));
    }
}
