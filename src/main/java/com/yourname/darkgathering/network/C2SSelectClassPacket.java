package com.yourname.darkgathering.network;

import com.yourname.darkgathering.capability.IPlayerData;
import com.yourname.darkgathering.capability.PlayerDataProvider;
import com.yourname.darkgathering.item.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SSelectClassPacket {
    private final IPlayerData.PlayerClass selectedClass;

    public C2SSelectClassPacket(IPlayerData.PlayerClass selectedClass) {
        this.selectedClass = selectedClass;
    }

    public C2SSelectClassPacket(FriendlyByteBuf buf) {
        this.selectedClass = buf.readEnum(IPlayerData.PlayerClass.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(selectedClass);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                    data.setPlayerClass(selectedClass);

                    if (selectedClass == IPlayerData.PlayerClass.ONMYOJI) {
                        player.sendSystemMessage(Component.translatable("message.darkgathering.class_selected_onmyoji"));

                        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                            ItemStack itemStack = player.getInventory().getItem(i);
                            if (itemStack.is(ModItems.DARK_GATHERING_BOOK.get())) {
                                itemStack.setHoverName(Component.literal("Book of " + player.getScoreboardName()));
                            }
                        }
                    } else if (selectedClass == IPlayerData.PlayerClass.EVIL_SPIRIT) {
                        player.sendSystemMessage(Component.translatable("message.darkgathering.class_selected_evil_spirit"));
                    } else {
                        player.sendSystemMessage(Component.translatable("message.darkgathering.class_selected_none"));
                    }

                    PacketHandler.sendToPlayer(new S2CSyncPlayerDataPacket(data), player);
                });
            }
        });
        context.setPacketHandled(true);
    }
}
