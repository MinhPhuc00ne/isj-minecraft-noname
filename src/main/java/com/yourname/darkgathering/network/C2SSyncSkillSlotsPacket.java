package com.yourname.darkgathering.network;

import com.yourname.darkgathering.capability.PlayerDataProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SSyncSkillSlotsPacket {
    private final String[] skillSlots;

    public C2SSyncSkillSlotsPacket(String[] skillSlots) {
        this.skillSlots = new String[9];
        for (int i = 0; i < 9; i++) {
            this.skillSlots[i] = (i < skillSlots.length && skillSlots[i] != null) ? skillSlots[i] : "";
        }
    }

    public C2SSyncSkillSlotsPacket(FriendlyByteBuf buf) {
        this.skillSlots = new String[9];
        for (int i = 0; i < 9; i++) {
            this.skillSlots[i] = buf.readUtf();
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        for (int i = 0; i < 9; i++) {
            buf.writeUtf(skillSlots[i] != null ? skillSlots[i] : "");
        }
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                    for (int i = 0; i < 9; i++) {
                        data.setSkillSlot(i, skillSlots[i]);
                    }
                    PacketHandler.sendToPlayer(new S2CSyncPlayerDataPacket(data), player);
                });
            }
        });
        context.setPacketHandled(true);
    }
}
