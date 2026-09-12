package com.minhphuc.weapons.content.infinitygauntlet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundCycleRealitySubModePacket {

    public ServerboundCycleRealitySubModePacket() {
    }

    public ServerboundCycleRealitySubModePacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
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
                int mainMode = heldStack.hasTag() ? heldStack.getTag().getInt(InfinityGauntletItem.NBT_MODE) : 0;
                if (mainMode == 3) { // Đá Thực Tại (Reality Stone)
                    int currentSubMode = heldStack.hasTag() ? heldStack.getTag().getInt("RealitySubMode") : 0;
                    int nextSubMode = (currentSubMode + 1) % 3;
                    heldStack.getOrCreateTag().putInt("RealitySubMode", nextSubMode);

                    String subModeMessage = switch (nextSubMode) {
                        case 0 -> "§c[ĐÁ THỰC TẠI] §fChế độ phụ: §e1. Cấu Trúc Thực Tại (Normal)";
                        case 1 -> "§c[ĐÁ THỰC TẠI] §fChế độ phụ: §b2. Đóng Băng Thực Tại (Frozen)";
                        case 2 -> "§c[ĐÁ THỰC TẠI] §fChế độ phụ: §a3. Phục Hồi Sự Sống (Life)";
                        default -> "";
                    };

                    player.displayClientMessage(Component.literal(subModeMessage), true);

                    float pitch = 0.8F + (nextSubMode * 0.3F);
                    player.level().playSound(
                        null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.AMETHYST_BLOCK_CHIME,
                        SoundSource.PLAYERS,
                        1.0F, pitch
                    );
                }
            }
        });
        context.setPacketHandled(true);
    }
}
