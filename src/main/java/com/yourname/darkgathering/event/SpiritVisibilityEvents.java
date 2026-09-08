package com.yourname.darkgathering.event;

import com.yourname.darkgathering.DarkGatheringMod;
import com.yourname.darkgathering.capability.IPlayerData;
import com.yourname.darkgathering.capability.PlayerDataProvider;
import com.yourname.darkgathering.entity.EvilSpiritEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DarkGatheringMod.MOD_ID)
public class SpiritVisibilityEvents {

    // Give permanent Night Vision to Onmyoji players
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player != null) {
            event.player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                if (data.getPlayerClass() == IPlayerData.PlayerClass.ONMYOJI) {
                    if (!event.player.hasEffect(MobEffects.NIGHT_VISION) || event.player.getEffect(MobEffects.NIGHT_VISION).getDuration() < 220) {
                        event.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, false, false, true));
                    }
                }
            });
        }
    }

    // Prevent non-Onmyoji players from attacking Evil Spirits unless HP <= 3
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (event.getTarget() instanceof EvilSpiritEntity spirit) {
            Player player = event.getEntity();
            if (player.isCreative()) return; // Creative mode bypasses restriction

            player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                if (data.getPlayerClass() != IPlayerData.PlayerClass.ONMYOJI) {
                    if (spirit.getHealth() > 3.0f && !spirit.isSpawnedViaEgg()) {
                        event.setCanceled(true);
                    }
                }
            });
        }
    }

    // Client-side rendering check for Evil Spirit visibility
    @Mod.EventBusSubscriber(modid = DarkGatheringMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientSpiritVisibility {
        @SubscribeEvent
        public static void onRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
            if (event.getEntity() instanceof EvilSpiritEntity spirit) {
                Minecraft mc = Minecraft.getInstance();
                Player player = mc.player;
                if (player == null) return;

                if (player.isCreative()) return; // Creative mode always sees spirits

                player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                    if (data.getPlayerClass() != IPlayerData.PlayerClass.ONMYOJI) {
                        // If entity HP > 3 and not spawned via spawn egg in creative mode -> cancel render (invisible)
                        if (spirit.getHealth() > 3.0f && !spirit.isSpawnedViaEgg()) {
                            event.setCanceled(true);
                        }
                    }
                });
            }
        }
    }
}
