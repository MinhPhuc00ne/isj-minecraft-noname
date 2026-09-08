package com.yourname.darkgathering.event;

import com.yourname.darkgathering.DarkGatheringMod;
import com.yourname.darkgathering.capability.IPlayerData;
import com.yourname.darkgathering.capability.PlayerData;
import com.yourname.darkgathering.capability.PlayerDataProvider;
import com.yourname.darkgathering.item.ModItems;
import com.yourname.darkgathering.network.PacketHandler;
import com.yourname.darkgathering.network.S2CSyncPlayerDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DarkGatheringMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerDataProvider.PLAYER_DATA).isPresent()) {
                event.addCapability(new ResourceLocation(DarkGatheringMod.MOD_ID, "player_data"), new PlayerDataProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(oldData -> {
                event.getEntity().getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(newData -> {
                    newData.deserializeNBT(oldData.serializeNBT());
                });
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide && player instanceof ServerPlayer serverPlayer) {

            // Give Dark Gathering Book if player does not have one
            boolean hasBook = false;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (player.getInventory().getItem(i).is(ModItems.DARK_GATHERING_BOOK.get())) {
                    hasBook = true;
                    break;
                }
            }

            if (!hasBook) {
                player.getInventory().add(new ItemStack(ModItems.DARK_GATHERING_BOOK.get()));
            }

            // Sync Capability data to client
            player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                PacketHandler.sendToPlayer(new S2CSyncPlayerDataPacket(data), serverPlayer);
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide && event.player instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                // Update Max Mana based on XP level: Base 100 + (XP Level * 10)
                float calculatedMax = 100.0f + (serverPlayer.experienceLevel * 10.0f);
                if (data.getMaxMana() < calculatedMax) {
                    data.setMaxMana(calculatedMax);
                }

                // Passive Mana Regeneration: +0.5 MP per second (tick / 20)
                if (data.getCurrentMana() < data.getMaxMana()) {
                    data.setCurrentMana(data.getCurrentMana() + 0.025f);
                }

                // Tick skill cooldowns
                data.tickCooldowns();

                // Periodic Sync every 20 ticks
                if (serverPlayer.tickCount % 20 == 0) {
                    PacketHandler.sendToPlayer(new S2CSyncPlayerDataPacket(data), serverPlayer);
                }
            });
        }
    }
}
