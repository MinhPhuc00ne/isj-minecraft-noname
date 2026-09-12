package com.minhphuc.weapons.content.infinitygauntlet;

import com.minhphuc.weapons.WeaponsMod;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WeaponsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InfinityGauntletEvents {

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (isHoldingGauntlet(player)) {
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (isHoldingGauntlet(player)) {
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (isHoldingGauntlet(player)) {
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (isHoldingGauntlet(player)) {
                event.setDistance(0.0F);
                event.setDamageMultiplier(0.0F);
                event.setCanceled(true);
            }
        }
    }

    private static boolean isHoldingGauntlet(Player player) {
        return player.getMainHandItem().getItem() instanceof InfinityGauntletItem
                || player.getOffhandItem().getItem() instanceof InfinityGauntletItem;
    }
}
