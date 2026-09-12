package com.minhphuc.weapons.content.raphael;

import com.minhphuc.weapons.WeaponsMod;
import com.minhphuc.weapons.network.ModMessages;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WeaponsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RaphaelEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;

        if (event.player instanceof ServerPlayer player) {
            if (!RaphaelBookItem.isPlayerCarryingActiveRaphael(player)) return;

            ResourceLocation biomeLoc = player.level().getBiome(player.blockPosition()).unwrapKey().map(k -> k.location()).orElse(null);
            if (biomeLoc == null) return;

            String biomePath = biomeLoc.toString();
            String lastBiome = player.getPersistentData().getString("RaphaelLastBiome");

            if (!biomePath.equals(lastBiome)) {
                player.getPersistentData().putString("RaphaelLastBiome", biomePath);

                String prettyName = formatBiomeName(biomeLoc.getPath());

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.5F, 1.3F);

                player.displayClientMessage(
                    Component.literal("§e§l[THÔNG TUỆ VƯƠNG RAPHAEL] §fBáo cáo Chủ Nhân: Ngài đã tiến vào khu vực sinh thái §a" + prettyName + "§f! ✨"),
                    true
                );

                ModMessages.sendToPlayer(
                    new ClientboundRaphaelHudTriggerPacket(1, "§e§lTHÔNG TUỆ VƯƠNG RAPHAEL", "§fKhu Vực Sinh Thái: §a" + prettyName),
                    player
                );
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            if (!RaphaelBookItem.isPlayerCarryingActiveRaphael(player)) return;

            LivingEntity target = event.getEntity();
            if (target == null || !target.isAlive() || target == player) return;

            float maxHealth = target.getMaxHealth();
            float healthAfter = target.getHealth() - event.getAmount();

            if (maxHealth > 0 && (healthAfter / maxHealth) <= 0.50F && healthAfter > 0) {
                if (!target.getPersistentData().getBoolean("RaphaelAnalyzed")) {
                    target.getPersistentData().putBoolean("RaphaelAnalyzed", true);

                    String mobName = target.getDisplayName().getString();
                    String hpStr = String.format("%.1f", Math.max(0, healthAfter)) + " / " + (int) maxHealth;

                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.5F, 1.0F);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.8F, 1.5F);

                    player.displayClientMessage(
                        Component.literal("§e§l[RAPHAEL - PHÂN TÍCH TẢI XONG] §fMục tiêu: §c" + mobName + " §f| HP: §a" + hpStr + " §f| Tỷ lệ thắng sinh vật: §c§l0.0% §f(Thất bại là tất yếu!)"),
                        true
                    );

                    ModMessages.sendToPlayer(
                        new ClientboundRaphaelHudTriggerPacket(2, mobName, hpStr),
                        player
                    );
                }
            }
        }
    }

    private static String formatBiomeName(String rawPath) {
        if (rawPath == null || rawPath.isEmpty()) return "Unknown Biome";
        String[] words = rawPath.replace('_', ' ').split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty()) {
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }
}
