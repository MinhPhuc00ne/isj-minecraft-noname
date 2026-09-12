package com.minhphuc.weapons.content.infinitygauntlet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PowerStoneAbility {

    public static void executePowerStone(ServerLevel level, ServerPlayer player, ItemStack stack) {
        if (isTrappedInEnclosedSpace(level, player)) {
            triggerEnergyBurst(level, player, stack);
        } else {
            firePowerLaser(level, player, stack);
        }
    }

    /**
     * Kiểm tra người chơi có đang bị giam cầm trong không gian kín hay bị ngạt thở không
     */
    public static boolean isTrappedInEnclosedSpace(ServerLevel level, ServerPlayer player) {
        BlockPos feetPos = player.blockPosition();
        BlockPos headPos = feetPos.above();

        // 1. Kiểm tra nếu chân hoặc đầu bị nằm trong khối rắn (ngạt thở)
        BlockState feetState = level.getBlockState(feetPos);
        BlockState headState = level.getBlockState(headPos);

        if (isSolid(level, feetPos, feetState) || isSolid(level, headPos, headState)) {
            return true;
        }

        // 2. Kiểm tra nếu cả 6 hướng xung quanh chân & đầu bị bao bọc hoàn toàn bởi các khối rắn
        BlockPos[] surrounding = new BlockPos[] {
            feetPos.north(), feetPos.south(), feetPos.east(), feetPos.west(), feetPos.below(),
            headPos.north(), headPos.south(), headPos.east(), headPos.west(), headPos.above()
        };

        int solidCount = 0;
        for (BlockPos pos : surrounding) {
            if (isSolid(level, pos, level.getBlockState(pos))) {
                solidCount++;
            }
        }

        // Nếu có ít nhất 8/10 vị trí xung quanh là khối rắn -> bị giam cầm trong không gian kín
        return solidCount >= 8;
    }

    private static boolean isSolid(ServerLevel level, BlockPos pos, BlockState state) {
        return !state.isAir() && state.blocksMotion();
    }

    /**
     * Bộc phát xung năng lượng giải thoát khi bị giam cầm
     */
    private static void triggerEnergyBurst(ServerLevel level, ServerPlayer player, ItemStack stack) {
        // Âm thanh nổ năng lượng rực rỡ
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 2.0F, 0.8F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 1.0F, 1.2F);

        BlockPos center = player.blockPosition();
        int radius = 4;

        // Phá hủy toàn bộ các khối xung quanh (trừ Bedrock & Barrier)
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distSq = x * x + y * y + z * z;
                    if (distSq <= radius * radius) {
                        BlockPos targetPos = center.offset(x, y, z);
                        BlockState state = level.getBlockState(targetPos);

                        if (canBreakBlock(level, targetPos, state)) {
                            level.destroyBlock(targetPos, false);
                        }
                    }
                }
            }
        }

        // Tiêu diệt quái vật xung quanh
        AABB blastArea = player.getBoundingBox().inflate(6.0D);
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, blastArea, e -> e != player && e.isAlive());
        for (LivingEntity entity : nearbyEntities) {
            entity.hurt(level.damageSources().genericKill(), 100000.0F);
        }

        // Hiệu ứng hạt xung nổ tím rực rỡ
        for (int i = 0; i < 360; i += 15) {
            double rad = Math.toRadians(i);
            double px = player.getX() + Math.cos(rad) * 3.5D;
            double pz = player.getZ() + Math.sin(rad) * 3.5D;
            level.sendParticles(ParticleTypes.DRAGON_BREATH, px, player.getY() + 1.0D, pz, 5, 0.2D, 0.5D, 0.2D, 0.05D);
            level.sendParticles(ParticleTypes.EXPLOSION, px, player.getY() + 0.5D, pz, 1, 0D, 0D, 0D, 0D);
        }
        level.sendParticles(ParticleTypes.SONIC_BOOM, player.getX(), player.getY() + 1.0D, player.getZ(), 1, 0, 0, 0, 0);

        player.displayClientMessage(
            Component.literal("§d§l[POWER STONE] PHÁT XUNG NĂNG LƯỢNG PHÁ HỦY KHÔNG GIAN KÍN GIÚP BẠN THOÁT RA!"),
            true
        );

        player.getCooldowns().addCooldown(stack.getItem(), 20); // Cooldown 1 giây
    }

    /**
     * Bắn chùm tia laze hủy diệt theo hướng nhìn
     */
    private static void firePowerLaser(ServerLevel level, ServerPlayer player, ItemStack stack) {
        // Âm thanh chùm laze năng lượng
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.5F, 1.6F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 1.0F, 0.8F);

        Vec3 startVec = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getLookAngle();
        double maxDistance = 80.0D;
        double step = 0.5D;

        Set<BlockPos> brokenBlocks = new HashSet<>();
        Set<LivingEntity> killedEntities = new HashSet<>();

        for (double d = 0; d <= maxDistance; d += step) {
            Vec3 currentVec = startVec.add(lookVec.scale(d));

            // Spawn chùm hạt laze tím rực rỡ
            level.sendParticles(ParticleTypes.DRAGON_BREATH, currentVec.x, currentVec.y, currentVec.z, 2, 0.08D, 0.08D, 0.08D, 0.01D);
            level.sendParticles(ParticleTypes.PORTAL, currentVec.x, currentVec.y, currentVec.z, 1, 0.04D, 0.04D, 0.04D, 0.02D);

            if ((int) (d * 2) % 10 == 0) {
                level.sendParticles(ParticleTypes.FLASH, currentVec.x, currentVec.y, currentVec.z, 1, 0, 0, 0, 0);
            }

            // Phá khối trên đường bắn
            BlockPos blockPos = BlockPos.containing(currentVec);
            if (!brokenBlocks.contains(blockPos)) {
                BlockState state = level.getBlockState(blockPos);
                if (canBreakBlock(level, blockPos, state)) {
                    level.destroyBlock(blockPos, false);
                    brokenBlocks.add(blockPos);
                }
            }

            // Gây sát thương vô hạn cho sinh vật trúng laze
            AABB hitBox = new AABB(
                currentVec.x - 1.2D, currentVec.y - 1.2D, currentVec.z - 1.2D,
                currentVec.x + 1.2D, currentVec.y + 1.2D, currentVec.z + 1.2D
            );
            List<LivingEntity> hitEntities = level.getEntitiesOfClass(LivingEntity.class, hitBox, e -> e != player && e.isAlive());

            for (LivingEntity entity : hitEntities) {
                if (!killedEntities.contains(entity)) {
                    entity.hurt(level.damageSources().genericKill(), 100000.0F);
                    killedEntities.add(entity);
                }
            }
        }

        player.displayClientMessage(
            Component.literal("§d[POWER STONE] Bắn chùm laze hủy diệt! (Đã phá " + brokenBlocks.size() + " khối & tiêu diệt " + killedEntities.size() + " sinh vật)"),
            true
        );

        player.getCooldowns().addCooldown(stack.getItem(), 10); // Cooldown 0.5 giây
    }

    /**
     * Kiểm tra khối có thể bị phá bởi Đá Sức Mạnh hay không (Không phá Bedrock hoặc Barrier)
     */
    private static boolean canBreakBlock(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.isAir()) return false;
        if (state.is(Blocks.BEDROCK) || state.is(Blocks.BARRIER)) return false;
        return state.getDestroySpeed(level, pos) >= 0;
    }
}
