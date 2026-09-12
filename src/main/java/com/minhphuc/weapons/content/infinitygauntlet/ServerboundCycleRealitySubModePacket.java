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
                } else if (mainMode == 4) { // Đá Linh Hồn (Soul Stone)
                    int currentSubMode = heldStack.hasTag() ? heldStack.getTag().getInt("SoulSubMode") : 0;
                    int nextSubMode = (currentSubMode + 1) % 3;
                    heldStack.getOrCreateTag().putInt("SoulSubMode", nextSubMode);

                    String subModeMessage = switch (nextSubMode) {
                        case 0 -> "§6[ĐÁ LINH HỒN] §fChế độ phụ: §e1. 🔥 Soul Harvest (Gặt Hái Linh Hồn)";
                        case 1 -> "§6[ĐÁ LINH HỒN] §fChế độ phụ: §b2. 👻 Soul Puppet (Chiêu Hồn Phụ Tá)";
                        case 2 -> "§6[ĐÁ LINH HỒN] §fChế độ phụ: §c3. 💥 Soul Extraction (Tách & Thiêu Rụi Linh Hồn)";
                        default -> "";
                    };

                    player.displayClientMessage(Component.literal(subModeMessage), true);

                    float pitch = 0.8F + (nextSubMode * 0.3F);
                    player.level().playSound(
                        null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.SOUL_ESCAPE,
                        SoundSource.PLAYERS,
                        1.0F, pitch
                    );
                } else if (mainMode == 5) { // Đá Thời Gian (Time Stone)
                    int currentSubMode = heldStack.hasTag() ? heldStack.getTag().getInt("TimeSubMode") : 0;
                    int nextSubMode = (currentSubMode + 1) % 3;
                    heldStack.getOrCreateTag().putInt("TimeSubMode", nextSubMode);

                    String subModeMessage = switch (nextSubMode) {
                        case 0 -> "§a[ĐÁ THỜI GIAN] §fChế độ phụ: §e1. ⌛ Time Rewind (Tua Ngược Thời Gian)";
                        case 1 -> "§a[ĐÁ THỜI GIAN] §fChế độ phụ: §b2. 🌿 Age Decay & Growth (Lão Hóa & Sinh Trưởng)";
                        case 2 -> "§a[ĐÁ THỜI GIAN] §fChế độ phụ: §c3. 🛑 Time Freeze Domain (Đóng Băng Thời Gian)";
                        default -> "";
                    };

                    player.displayClientMessage(Component.literal(subModeMessage), true);

                    float pitch = 0.9F + (nextSubMode * 0.3F);
                    player.level().playSound(
                        null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.BEACON_POWER_SELECT,
                        SoundSource.PLAYERS,
                        1.0F, pitch
                    );
                } else if (mainMode == 6) { // Đá Tâm Trí (Mind Stone)
                    int currentSubMode = heldStack.hasTag() ? heldStack.getTag().getInt("MindSubMode") : 0;
                    int nextSubMode = (currentSubMode + 1) % 3;
                    heldStack.getOrCreateTag().putInt("MindSubMode", nextSubMode);

                    String subModeMessage = switch (nextSubMode) {
                        case 0 -> "§e[ĐÁ TÂM TRÍ] §fChế độ phụ: §61. 👑 Vương Quyền Chi Phối (Hypnosis)";
                        case 1 -> "§e[ĐÁ TÂM TRÍ] §fChế độ phụ: §b2. 🌀 Telekinesis (Thao Túng Vật Lý)";
                        case 2 -> "§e[ĐÁ TÂM TRÍ] §fChế độ phụ: §c3. ⚡ Mind Beam Laser (Vision)";
                        default -> "";
                    };

                    player.displayClientMessage(Component.literal(subModeMessage), true);

                    float pitch = 0.9F + (nextSubMode * 0.3F);
                    player.level().playSound(
                        null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.EXPERIENCE_ORB_PICKUP,
                        SoundSource.PLAYERS,
                        1.0F, pitch
                    );
                }
            }
        });
        context.setPacketHandled(true);
    }
}
