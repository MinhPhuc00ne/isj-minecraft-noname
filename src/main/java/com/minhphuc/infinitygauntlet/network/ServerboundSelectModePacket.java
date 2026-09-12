package com.minhphuc.infinitygauntlet.network;

import com.minhphuc.infinitygauntlet.item.InfinityGauntletItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundSelectModePacket {
    private final int modeOrdinal;

    public ServerboundSelectModePacket(int modeOrdinal) {
        this.modeOrdinal = modeOrdinal;
    }

    public ServerboundSelectModePacket(FriendlyByteBuf buf) {
        this.modeOrdinal = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.modeOrdinal);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            ItemStack heldStack = player.getMainHandItem();
            if (!(heldStack.getItem() instanceof InfinityGauntletItem)) {
                heldStack = player.getOffhandItem();
            }

            if (heldStack.getItem() instanceof InfinityGauntletItem) {
                heldStack.getOrCreateTag().putInt(InfinityGauntletItem.NBT_MODE, modeOrdinal);
                String modeName = InfinityGauntletItem.getModeName(modeOrdinal);

                player.displayClientMessage(
                    Component.literal("§a[Găng Tay Vô Cực] Đã chọn chế độ: §e" + modeName),
                    true
                );

                player.level().playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.EXPERIENCE_ORB_PICKUP,
                    SoundSource.PLAYERS,
                    0.8F, 1.2F
                );
            }
        });
        context.setPacketHandled(true);
    }
}
