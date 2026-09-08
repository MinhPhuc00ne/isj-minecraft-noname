package com.yourname.darkgathering.network;

import com.yourname.darkgathering.capability.PlayerDataProvider;
import com.yourname.darkgathering.skill.Skill;
import com.yourname.darkgathering.skill.SkillRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SCastSkillPacket {
    private final int slotIndex;

    public C2SCastSkillPacket(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    public C2SCastSkillPacket(FriendlyByteBuf buf) {
        this.slotIndex = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(slotIndex);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                    String skillId = data.getSkillSlot(slotIndex);
                    if (skillId.isEmpty()) return;

                    Skill skill = SkillRegistry.getSkill(skillId);
                    if (skill == null) return;

                    int cooldown = data.getCooldown(skillId);
                    if (cooldown > 0) {
                        player.sendSystemMessage(Component.translatable("message.darkgathering.skill_cooldown", String.format("%.1f", cooldown / 20.0f)));
                        return;
                    }

                    if (data.getCurrentMana() < skill.getManaCost()) {
                        player.sendSystemMessage(Component.translatable("message.darkgathering.not_enough_mana"));
                        return;
                    }

                    boolean executed = skill.execute(player, data);
                    if (executed) {
                        data.setCurrentMana(data.getCurrentMana() - skill.getManaCost());
                        data.setCooldown(skillId, skill.getCooldownTicks());
                        PacketHandler.sendToPlayer(new S2CSyncPlayerDataPacket(data), player);
                    }
                });
            }
        });
        context.setPacketHandled(true);
    }
}
