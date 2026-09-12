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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class RealityStoneAbility {

    public static void executeRealityStone(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        // Nhìn lên trời (pitch < -45°) -> Đổi thời tiết
        if (player.getXRot() < -45.0F) {
            toggleWeather(level, player, gauntlet);
            return;
        }

        int subMode = gauntlet.hasTag() ? gauntlet.getTag().getInt("RealitySubMode") : 0;
        switch (subMode) {
            case 0 -> executeNormalMode(level, player, gauntlet);
            case 1 -> executeFrozenMode(level, player, gauntlet);
            case 2 -> executeLifeMode(level, player, gauntlet);
        }
    }

    /**
     * Điều khiển thời tiết khi chuột phải nhìn lên trời
     */
    public static void toggleWeather(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        if (level.isThundering()) {
            level.setWeatherParameters(6000, 0, false, false);
            player.displayClientMessage(Component.literal("§e[ĐÁ THỰC TẠI] Đã biến mưa bão thành TRỜI NẮNG! ☀️"), true);
        } else if (level.isRaining()) {
            level.setWeatherParameters(0, 6000, true, true);
            player.displayClientMessage(Component.literal("§c[ĐÁ THỰC TẠI] Đã biến mưa thành MƯA BÃO SẤM SÉT! ⚡"), true);
        } else {
            level.setWeatherParameters(0, 6000, true, false);
            player.displayClientMessage(Component.literal("§9[ĐÁ THỰC TẠI] Đã biến trời nắng thành TRỜI MƯA! 🌧️"), true);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.0F, 1.0F);
        level.sendParticles(ParticleTypes.FLASH, player.getX(), player.getY() + 2.0D, player.getZ(), 1, 0, 0, 0, 0);

        player.getCooldowns().addCooldown(gauntlet.getItem(), 20);
    }

    /**
     * Chế độ 1: Normal - Biến đổi cấu trúc vật liệu theo khối ở Tay Trái (Offhand)
     */
    private static void executeNormalMode(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        ItemStack offhand = player.getOffhandItem();
        if (!(offhand.getItem() instanceof BlockItem blockItem)) {
            player.displayClientMessage(
                Component.literal("§c[ĐÁ THỰC TẠI - NORMAL] Hãy cầm 1 khối vật liệu ở tay trái (Offhand) để biến đổi thực tại!"),
                true
            );
            return;
        }

        BlockState targetState = blockItem.getBlock().defaultBlockState();
        BlockPos center = player.blockPosition();
        int radius = 10;
        int count = 0;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = center.offset(x, y, z);
                    if (center.distSqr(targetPos) <= radius * radius) {
                        BlockState state = level.getBlockState(targetPos);
                        if (canTransformInNormalMode(level, targetPos, state)) {
                            level.setBlock(targetPos, targetState, 3);
                            count++;
                        }
                    }
                }
            }
        }

        // Hiệu ứng
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.5F, 1.2F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

        level.sendParticles(ParticleTypes.DRAGON_BREATH, player.getX(), player.getY() + 1.0D, player.getZ(), 30, 1.5D, 1.5D, 1.5D, 0.05D);
        level.sendParticles(ParticleTypes.FLASH, player.getX(), player.getY() + 1.0D, player.getZ(), 1, 0, 0, 0, 0);

        player.displayClientMessage(
            Component.literal("§c[ĐÁ THỰC TẠI - NORMAL] Đã biến đổi " + count + " khối xung quanh thành " + targetState.getBlock().getName().getString() + "!"),
            true
        );

        player.getCooldowns().addCooldown(gauntlet.getItem(), 20);
    }

    private static boolean canTransformInNormalMode(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.isAir() || state.is(Blocks.BEDROCK) || state.is(Blocks.BARRIER)) return false;
        Block block = state.getBlock();

        // Không biến đổi kính, cửa, bàn chế tạo, rương, lò nung, đe, bàn phù phép...
        if (block instanceof GlassBlock || block instanceof StainedGlassBlock || block instanceof IronBarsBlock) return false;
        if (block instanceof DoorBlock || block instanceof TrapDoorBlock) return false;
        if (block instanceof CraftingTableBlock || block instanceof AbstractChestBlock || block instanceof FurnaceBlock || block instanceof AnvilBlock || block instanceof EnchantmentTableBlock) return false;
        if (level.getBlockEntity(pos) != null) return false;

        return true;
    }

    /**
     * Chế độ 2: Frozen - Đóng băng địa hình xung quanh trong bán kính 15 blocks (Giữ sinh vật sống bình thường)
     */
    private static void executeFrozenMode(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        BlockPos center = player.blockPosition();
        int radius = 15;
        int countBlocks = 0;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = center.offset(x, y, z);
                    if (center.distSqr(targetPos) <= radius * radius) {
                        BlockState state = level.getBlockState(targetPos);
                        if (state.is(Blocks.WATER)) {
                            level.setBlock(targetPos, Blocks.ICE.defaultBlockState(), 3);
                            countBlocks++;
                        } else if (canFreezeBlock(level, targetPos, state)) {
                            level.setBlock(targetPos, Blocks.PACKED_ICE.defaultBlockState(), 3);
                            countBlocks++;
                        }
                    }
                }
            }
        }

        // Hạt tuyết đóng băng bao quanh khu vực
        level.sendParticles(ParticleTypes.SNOWFLAKE, player.getX(), player.getY() + 1.0D, player.getZ(), 40, 2.0D, 1.0D, 2.0D, 0.05D);
        level.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1.0D, player.getZ(), 10, 1.0D, 0.5D, 1.0D, 0.02D);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.5F, 0.8F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_HURT_FREEZE, SoundSource.PLAYERS, 1.5F, 1.0F);

        player.displayClientMessage(
            Component.literal("§b[ĐÁ THỰC TẠI - FROZEN] Đã đóng băng " + countBlocks + " khối địa hình trong phạm vi 15 blocks! ❄️"),
            true
        );

        player.getCooldowns().addCooldown(gauntlet.getItem(), 20);
    }

    private static boolean canFreezeBlock(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.isAir() || state.is(Blocks.BEDROCK) || state.is(Blocks.BARRIER)) return false;
        Block block = state.getBlock();
        if (block instanceof AbstractChestBlock || block instanceof CraftingTableBlock) return false;
        if (level.getBlockEntity(pos) != null) return false;
        return state.getDestroySpeed(level, pos) >= 0;
    }

    /**
     * Chế độ 3: Life - Phục hồi Làng & Mỏ cổ, dọn dẹp mạng nhện, hồi sinh Dân Làng/Người Sắt & hồi 100% máu
     */
    private static void executeLifeMode(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        BlockPos center = player.blockPosition();
        int radius = 25;
        int cleanedCobwebs = 0;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = center.offset(x, y, z);
                    if (center.distSqr(targetPos) <= radius * radius) {
                        BlockState state = level.getBlockState(targetPos);
                        if (state.is(Blocks.COBWEB)) {
                            level.removeBlock(targetPos, false);
                            cleanedCobwebs++;
                        } else if (state.is(Blocks.MOSSY_COBBLESTONE)) {
                            level.setBlock(targetPos, Blocks.COBBLESTONE.defaultBlockState(), 3);
                        } else if (state.is(Blocks.MOSSY_STONE_BRICKS)) {
                            level.setBlock(targetPos, Blocks.STONE_BRICKS.defaultBlockState(), 3);
                        } else if (state.is(Blocks.INFESTED_COBBLESTONE)) {
                            level.setBlock(targetPos, Blocks.COBBLESTONE.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }

        // Triệu hồi Dân Làng & Người Sắt
        for (int i = 0; i < 3; i++) {
            EntityType.VILLAGER.spawn(level, center.offset(i * 2 - 2, 1, 2), MobSpawnType.EVENT);
        }
        EntityType.IRON_GOLEM.spawn(level, center.offset(0, 1, -3), MobSpawnType.EVENT);

        // Hồi 100% máu & xóa hiệu ứng xấu cho tất cả sinh vật thân thiện/dân làng trong 25 blocks
        AABB area = new AABB(center).inflate(radius);
        List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAlive);
        int healedCount = 0;

        for (LivingEntity entity : nearby) {
            if (entity instanceof Villager || entity instanceof Animal || entity instanceof IronGolem || entity instanceof Player) {
                entity.setHealth(entity.getMaxHealth());
                entity.removeAllEffects();
                healedCount++;
                level.sendParticles(ParticleTypes.HAPPY_VILLAGER, entity.getX(), entity.getY() + 1.0D, entity.getZ(), 10, 0.3D, 0.5D, 0.3D, 0.05D);
            }
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.2F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.VILLAGER_YES, SoundSource.PLAYERS, 1.0F, 1.0F);

        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1.0D, player.getZ(), 30, 2.0D, 1.0D, 2.0D, 0.05D);
        level.sendParticles(ParticleTypes.HEART, player.getX(), player.getY() + 1.5D, player.getZ(), 15, 1.5D, 1.0D, 1.5D, 0.05D);

        player.displayClientMessage(
            Component.literal("§a[ĐÁ THỰC TẠI - LIFE] Khôi phục sự sống: Dọn " + cleanedCobwebs + " mạng nhện, gọi Dân Làng/Người Sắt, hồi 100% máu cho " + healedCount + " sinh vật! 🌿"),
            true
        );

        player.getCooldowns().addCooldown(gauntlet.getItem(), 40);
    }
}
