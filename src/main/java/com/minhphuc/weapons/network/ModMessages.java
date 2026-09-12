package com.minhphuc.weapons.network;

import com.minhphuc.weapons.WeaponsMod;
import com.minhphuc.weapons.content.infinitygauntlet.ServerboundSelectModePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(WeaponsMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ServerboundSelectModePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundSelectModePacket::new)
                .encoder(ServerboundSelectModePacket::encode)
                .consumerMainThread(ServerboundSelectModePacket::handle)
                .add();

        net.messageBuilder(com.minhphuc.weapons.content.infinitygauntlet.ServerboundCycleRealitySubModePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(com.minhphuc.weapons.content.infinitygauntlet.ServerboundCycleRealitySubModePacket::new)
                .encoder(com.minhphuc.weapons.content.infinitygauntlet.ServerboundCycleRealitySubModePacket::encode)
                .consumerMainThread(com.minhphuc.weapons.content.infinitygauntlet.ServerboundCycleRealitySubModePacket::handle)
                .add();

        net.messageBuilder(com.minhphuc.weapons.content.raphael.ClientboundRaphaelHudTriggerPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(com.minhphuc.weapons.content.raphael.ClientboundRaphaelHudTriggerPacket::new)
                .encoder(com.minhphuc.weapons.content.raphael.ClientboundRaphaelHudTriggerPacket::encode)
                .consumerMainThread(com.minhphuc.weapons.content.raphael.ClientboundRaphaelHudTriggerPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
