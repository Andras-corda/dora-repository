package com.qbits.dora.network;

import com.qbits.dora.Dora;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String VERSION = "3";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(Dora.MODID, "main"), () -> VERSION, VERSION::equals, VERSION::equals);

    public static void register() {
        CHANNEL.messageBuilder(CarvePixelPacket.class, 0, NetworkDirection.PLAY_TO_SERVER)
                .encoder(CarvePixelPacket::encode)
                .decoder(CarvePixelPacket::new)
                .consumerMainThread(CarvePixelPacket::handle)
                .add();
        CHANNEL.messageBuilder(SapRevealPacket.class, 1, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SapRevealPacket::encode)
                .decoder(SapRevealPacket::new)
                .consumerMainThread(SapRevealPacket::handle)
                .add();
    }
}
