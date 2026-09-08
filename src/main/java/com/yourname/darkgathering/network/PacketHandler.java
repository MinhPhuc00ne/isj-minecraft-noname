package com.yourname.darkgathering.network;

import com.yourname.darkgathering.DarkGatheringMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(DarkGatheringMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE.messageBuilder(C2SSelectClassPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SSelectClassPacket::new)
                .encoder(C2SSelectClassPacket::toBytes)
                .consumerMainThread(C2SSelectClassPacket::handle)
                .add();

        INSTANCE.messageBuilder(C2SSyncSkillSlotsPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SSyncSkillSlotsPacket::new)
                .encoder(C2SSyncSkillSlotsPacket::toBytes)
                .consumerMainThread(C2SSyncSkillSlotsPacket::handle)
                .add();

        INSTANCE.messageBuilder(C2SCastSkillPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SCastSkillPacket::new)
                .encoder(C2SCastSkillPacket::toBytes)
                .consumerMainThread(C2SCastSkillPacket::handle)
                .add();

        INSTANCE.messageBuilder(S2CSyncPlayerDataPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CSyncPlayerDataPacket::new)
                .encoder(S2CSyncPlayerDataPacket::toBytes)
                .consumerMainThread(S2CSyncPlayerDataPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
