package com.minhphuc.weapons.content.raphael;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundRaphaelHudTriggerPacket {
    private final int hudType; // 1 = Biome, 2 = Appraisal
    private final String title;
    private final String detail;

    public ClientboundRaphaelHudTriggerPacket(int hudType, String title, String detail) {
        this.hudType = hudType;
        this.title = title;
        this.detail = detail;
    }

    public ClientboundRaphaelHudTriggerPacket(FriendlyByteBuf buf) {
        this.hudType = buf.readInt();
        this.title = buf.readUtf(32767);
        this.detail = buf.readUtf(32767);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(hudType);
        buf.writeUtf(title);
        buf.writeUtf(detail);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                RaphaelHudOverlay.triggerHudAnimation(hudType, title, detail);
            });
        });
        context.setPacketHandled(true);
    }
}
