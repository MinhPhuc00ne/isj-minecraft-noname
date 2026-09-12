package com.minhphuc.weapons.content.infinitygauntlet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class TimeStoneAbility {

    public static void executeTimeStone(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        int subMode = gauntlet.hasTag() ? gauntlet.getTag().getInt("TimeSubMode") : 0;
        switch (subMode) {
            case 0 -> executeTimeRewind(level, player, gauntlet);
            case 1 -> executeAgeDecay(level, player, gauntlet);
            case 2 -> executeTimeFreezeDomain(level, player, gauntlet);
        }
    }

    /**
     * Chế độ 1: ⌛ Time Rewind - Tua ngược thời gian, khôi phục 100% sinh lực & trạng thái tối thượng
     */
    private static void executeTimeRewind(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        player.setHealth(player.getMaxHealth());
        player.removeAllEffects();
        player.clearFire();
        player.setAirSupply(player.getMaxAirSupply());

        int duration = 300; // 15 seconds
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration, 9, false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, 4, false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, 2, false, false, true));

        // Vòng quay hạt xanh lục thời gian xoay quanh người chơi
        for (int i = 0; i < 360; i += 15) {
            double rad = Math.toRadians(i);
            double px = player.getX() + Math.cos(rad) * 2.5D;
            double pz = player.getZ() + Math.sin(rad) * 2.5D;
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER, px, player.getY() + 1.0D, pz, 3, 0.1D, 0.3D, 0.1D, 0.05D);
            level.sendParticles(ParticleTypes.END_ROD, px, player.getY() + 0.5D, pz, 1, 0.05D, 0.05D, 0.05D, 0.02D);
        }
        level.sendParticles(ParticleTypes.FLASH, player.getX(), player.getY() + 1.0D, player.getZ(), 1, 0, 0, 0, 0);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 1.5F, 1.4F);

        player.displayClientMessage(
            Component.literal("§a§l[ĐÁ THỜI GIAN - TIME REWIND] §fĐã tua ngược thời gian, khôi phục 100% sinh lực & trạng thái tối thượng! ⌛"),
            true
        );

        player.getCooldowns().addCooldown(gauntlet.getItem(), 30);
    }

    /**
     * Chế độ 2: 🌿 Age Decay & Growth - Tua thời gian: Lão hóa quái vật & Thúc đẩy vạn vật sinh trưởng
     */
    private static void executeAgeDecay(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        BlockPos center = player.blockPosition();
        int radius = 20;

        // Lão hóa quái vật
        AABB area = new AABB(center).inflate(radius);
        List<Mob> mobs = level.getEntitiesOfClass(Mob.class, area, Mob::isAlive);

        for (Mob mob : mobs) {
            mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, -255, false, false, true));
            mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 255, false, false, true));
            mob.addEffect(new MobEffectInstance(MobEffects.WITHER, 400, 4, false, false, true));
            level.sendParticles(ParticleTypes.SMOKE, mob.getX(), mob.getY() + 1.0D, mob.getZ(), 10, 0.3D, 0.5D, 0.3D, 0.05D);
        }

        // Thúc đẩy cây trồng & cây cối sinh trưởng 100%
        int grownCrops = 0;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = center.offset(x, y, z);
                    if (center.distSqr(targetPos) <= radius * radius) {
                        BlockState state = level.getBlockState(targetPos);
                        if (state.getBlock() instanceof CropBlock crop) {
                            level.setBlock(targetPos, crop.getStateForAge(crop.getMaxAge()), 3);
                            grownCrops++;
                        } else if (state.getBlock() instanceof SaplingBlock sapling) {
                            sapling.advanceTree(level, targetPos, state, level.random);
                            grownCrops++;
                        }
                    }
                }
            }
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.COMPOSTER_READY, SoundSource.PLAYERS, 1.5F, 1.0F);

        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1.0D, player.getZ(), 40, 2.0D, 1.0D, 2.0D, 0.05D);

        player.displayClientMessage(
            Component.literal("§a§l[ĐÁ THỜI GIAN - AGE DECAY] §fĐã tua thời gian: lão hóa " + mobs.size() + " quái vật & thúc đẩy " + grownCrops + " cây trồng sinh trưởng! 🌿"),
            true
        );

        player.getCooldowns().addCooldown(gauntlet.getItem(), 30);
    }

    /**
     * Chế độ 3: 🛑 Time Freeze Domain - Đóng băng dòng thời gian của sinh vật trong 20 blocks
     */
    private static void executeTimeFreezeDomain(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        BlockPos center = player.blockPosition();
        int radius = 20;

        AABB area = new AABB(center).inflate(radius);
        List<Mob> mobs = level.getEntitiesOfClass(Mob.class, area, Mob::isAlive);

        for (Mob mob : mobs) {
            mob.setTicksFrozen(400);
            mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, -255, false, false, true));
            mob.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 300, 255, false, false, true));
            level.sendParticles(ParticleTypes.END_ROD, mob.getX(), mob.getY() + 1.0D, mob.getZ(), 10, 0.3D, 0.5D, 0.3D, 0.02D);
        }

        // Người chơi nhận tốc độ di chuyển siêu tốc
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 4, false, false, true));

        // Thiên cầu thời gian đóng băng rực rỡ
        for (int i = 0; i < 360; i += 20) {
            double rad = Math.toRadians(i);
            double px = player.getX() + Math.cos(rad) * 4.0D;
            double pz = player.getZ() + Math.sin(rad) * 4.0D;
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER, px, player.getY() + 1.0D, pz, 5, 0.2D, 0.5D, 0.2D, 0.05D);
        }
        level.sendParticles(ParticleTypes.FLASH, player.getX(), player.getY() + 1.0D, player.getZ(), 1, 0, 0, 0, 0);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 1.5F, 0.5F);

        player.displayClientMessage(
            Component.literal("§a§l[ĐÁ THỜI GIAN - TIME FREEZE DOMAIN] §fĐã đóng băng dòng thời gian của " + mobs.size() + " quái vật trong 20 blocks! 🛑"),
            true
        );

        player.getCooldowns().addCooldown(gauntlet.getItem(), 30);
    }
}
