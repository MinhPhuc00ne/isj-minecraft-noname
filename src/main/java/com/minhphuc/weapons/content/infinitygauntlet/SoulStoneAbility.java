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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SoulStoneAbility {

    public static void executeSoulStone(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        int subMode = gauntlet.hasTag() ? gauntlet.getTag().getInt("SoulSubMode") : 0;
        switch (subMode) {
            case 0 -> executeSoulHarvest(level, player, gauntlet);
            case 1 -> executeSoulPuppet(level, player, gauntlet);
            case 2 -> executeSoulExtraction(level, player, gauntlet);
            case 3 -> executeSoulResurrection(level, player, gauntlet);
        }
    }

    /**
     * Chế độ 1: 🔥 Soul Harvest - Gặt hái linh hồn quái vật, hồi 100% máu & ban giáp ảo tối thượng
     */
    private static void executeSoulHarvest(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        BlockPos center = player.blockPosition();
        int radius = 15;

        AABB area = new AABB(center).inflate(radius);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area, e -> e != player && e.isAlive());

        int harvestedCount = 0;
        for (LivingEntity entity : entities) {
            entity.hurt(level.damageSources().genericKill(), 100000.0F);
            if (entity.isAlive()) {
                entity.discard();
            }
            harvestedCount++;

            // Hạt linh hồn cam bay về hướng người chơi
            level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, entity.getX(), entity.getY() + 1.0D, entity.getZ(), 15, 0.3D, 0.5D, 0.3D, 0.05D);
            level.sendParticles(ParticleTypes.SOUL, entity.getX(), entity.getY() + 1.0D, entity.getZ(), 10, 0.2D, 0.4D, 0.2D, 0.03D);
        }

        // Hồi 100% máu & cộng giáp ảo tối thượng + sức mạnh X
        player.setHealth(player.getMaxHealth());
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 9, false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 9, false, false, true));

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 1.5F, 0.8F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.8F, 1.2F);

        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, player.getX(), player.getY() + 1.0D, player.getZ(), 40, 1.5D, 1.0D, 1.5D, 0.05D);

        player.displayClientMessage(
            Component.literal("§6§l[ĐÁ LINH HỒN - SOUL HARVEST] §fĐã gặt hái linh hồn của " + harvestedCount + " sinh vật, hồi 100% máu & ban giáp ảo tối thượng! 🔥"),
            true
        );

        player.getCooldowns().addCooldown(gauntlet.getItem(), 30);
    }

    /**
     * Chế độ 2: 👻 Soul Puppet - Chiêu hồn quái vật thành Binh Đoàn Hồn Ma Phụ Tá
     */
    private static void executeSoulPuppet(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        BlockPos center = player.blockPosition();
        int radius = 20;

        AABB area = new AABB(center).inflate(radius);
        List<Mob> mobs = level.getEntitiesOfClass(Mob.class, area, Mob::isAlive);

        for (Mob mob : mobs) {
            mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1200, 0, false, false, true));
            mob.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1200, 0, false, false, true));

            if (mob.getTarget() == player) {
                mob.setTarget(null);
            }

            List<Mob> enemies = level.getEntitiesOfClass(Mob.class, mob.getBoundingBox().inflate(15.0D), e -> e != mob && e.isAlive());
            if (!enemies.isEmpty()) {
                mob.setTarget(enemies.get(0));
            }

            level.sendParticles(ParticleTypes.SOUL, mob.getX(), mob.getY() + 1.0D, mob.getZ(), 10, 0.3D, 0.5D, 0.3D, 0.05D);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SOUL_SAND_BREAK, SoundSource.PLAYERS, 1.5F, 1.0F);

        player.displayClientMessage(
            Component.literal("§6§l[ĐÁ LINH HỒN - SOUL PUPPET] §fĐã chiêu hồn " + mobs.size() + " sinh vật thành Binh Đoàn Hồn Ma Phụ Tá! 👻"),
            true
        );

        player.getCooldowns().addCooldown(gauntlet.getItem(), 30);
    }

    /**
     * Chế độ 3: 💥 Soul Extraction - Nhắm mục tiêu & Tách linh hồn thiêu rụi mục tiêu tức thì
     */
    private static void executeSoulExtraction(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        Vec3 start = player.getEyePosition(1.0F);
        Vec3 look = player.getLookAngle();
        Vec3 end = start.add(look.scale(30.0D));

        List<LivingEntity> targets = level.getEntitiesOfClass(
            LivingEntity.class,
            new AABB(start, end).inflate(3.0D),
            e -> e != player && e.isAlive()
        );

        if (targets.isEmpty()) {
            player.displayClientMessage(
                Component.literal("§6[ĐÁ LINH HỒN - SOUL EXTRACTION] Không tìm thấy mục tiêu sinh vật phía trước!"),
                true
            );
            return;
        }

        LivingEntity target = targets.get(0);
        target.hurt(level.damageSources().genericKill(), 100000.0F);
        if (target.isAlive()) {
            target.discard();
        }

        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, target.getX(), target.getY() + 1.0D, target.getZ(), 50, 0.5D, 0.8D, 0.5D, 0.1D);
        level.sendParticles(ParticleTypes.FLASH, target.getX(), target.getY() + 1.0D, target.getZ(), 1, 0, 0, 0, 0);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WITHER_DEATH, SoundSource.PLAYERS, 1.0F, 1.2F);

        player.displayClientMessage(
            Component.literal("§6§l[ĐÁ LINH HỒN - SOUL EXTRACTION] Đã tách và thiêu rụi linh hồn của " + target.getName().getString() + "! 💥"),
            true
        );

        player.getCooldowns().addCooldown(gauntlet.getItem(), 20);
    }

    /**
     * Chế độ 4: 🧟 Kỹ Năng: Tử Linh Phục Sinh - Hồi sinh đúng sinh vật tương ứng từ vật phẩm rớt ra
     */
    private static void executeSoulResurrection(ServerLevel level, ServerPlayer player, ItemStack gauntlet) {
        BlockPos center = player.blockPosition();
        int radius = 10;

        AABB area = new AABB(center).inflate(radius);
        List<net.minecraft.world.entity.item.ItemEntity> itemEntities = level.getEntitiesOfClass(
            net.minecraft.world.entity.item.ItemEntity.class,
            area,
            e -> e.isAlive()
        );

        if (itemEntities.isEmpty()) {
            player.displayClientMessage(
                Component.literal("§6[ĐÁ LINH HỒN - TỬ LINH PHỤC SINH] Không tìm thấy vật phẩm/sinh vật tử trận trong 10 blocks!"),
                true
            );
            return;
        }

        int resurrectedCount = 0;
        for (net.minecraft.world.entity.item.ItemEntity itemEntity : itemEntities) {
            Vec3 pos = itemEntity.position();
            net.minecraft.world.item.Item item = itemEntity.getItem().getItem();
            net.minecraft.world.entity.EntityType<?> entityType = getResurrectedEntityType(item);

            itemEntity.discard(); // Tiêu thụ vật phẩm rớt ra

            net.minecraft.world.entity.Entity spawnedEntity = entityType.create(level);
            if (spawnedEntity instanceof LivingEntity living) {
                living.moveTo(pos.x, pos.y, pos.z, level.random.nextFloat() * 360.0F, 0.0F);
                living.setHealth(living.getMaxHealth());
                living.setCustomName(Component.literal("§6Tử Linh Phục Sinh §7(" + living.getDisplayName().getString() + ")"));
                living.setCustomNameVisible(true);
                living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1200, 0, false, false, true));
                living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 2, false, false, true));

                if (living instanceof Mob mob) {
                    List<Mob> enemies = level.getEntitiesOfClass(Mob.class, mob.getBoundingBox().inflate(15.0D), e -> e != mob && !(e.getCustomName() != null && e.getCustomName().getString().contains("Tử Linh Phục Sinh")));
                    if (!enemies.isEmpty()) {
                        mob.setTarget(enemies.get(0));
                    }
                }

                level.addFreshEntity(living);
                resurrectedCount++;

                // Hiệu ứng hồi sinh rực rỡ
                level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, pos.x, pos.y + 1.0D, pos.z, 25, 0.3D, 0.5D, 0.3D, 0.1D);
                level.sendParticles(ParticleTypes.SOUL, pos.x, pos.y + 1.0D, pos.z, 15, 0.2D, 0.4D, 0.2D, 0.05D);
            }
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.5F, 1.0F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS, 1.0F, 1.2F);

        player.displayClientMessage(
            Component.literal("§6§l[ĐÁ LINH HỒN] §fKỹ năng: Tử linh phục sinh! Đã hồi sinh " + resurrectedCount + " sinh vật từ vật phẩm rớt ra! 🧟✨"),
            true
        );

        player.getCooldowns().addCooldown(gauntlet.getItem(), 20);
    }

    private static net.minecraft.world.entity.EntityType<?> getResurrectedEntityType(net.minecraft.world.item.Item item) {
        if (item == net.minecraft.world.item.Items.CHICKEN || item == net.minecraft.world.item.Items.COOKED_CHICKEN || item == net.minecraft.world.item.Items.FEATHER) {
            return net.minecraft.world.entity.EntityType.CHICKEN;
        }
        if (item == net.minecraft.world.item.Items.PORKCHOP || item == net.minecraft.world.item.Items.COOKED_PORKCHOP) {
            return net.minecraft.world.entity.EntityType.PIG;
        }
        if (item == net.minecraft.world.item.Items.BEEF || item == net.minecraft.world.item.Items.COOKED_BEEF || item == net.minecraft.world.item.Items.LEATHER) {
            return net.minecraft.world.entity.EntityType.COW;
        }
        if (item == net.minecraft.world.item.Items.MUTTON || item == net.minecraft.world.item.Items.COOKED_MUTTON || item.toString().contains("wool")) {
            return net.minecraft.world.entity.EntityType.SHEEP;
        }
        if (item == net.minecraft.world.item.Items.ROTTEN_FLESH) {
            return net.minecraft.world.entity.EntityType.ZOMBIE;
        }
        if (item == net.minecraft.world.item.Items.BONE || item == net.minecraft.world.item.Items.ARROW) {
            return net.minecraft.world.entity.EntityType.SKELETON;
        }
        if (item == net.minecraft.world.item.Items.SPIDER_EYE || item == net.minecraft.world.item.Items.STRING) {
            return net.minecraft.world.entity.EntityType.SPIDER;
        }
        if (item == net.minecraft.world.item.Items.GUNPOWDER) {
            return net.minecraft.world.entity.EntityType.CREEPER;
        }
        if (item == net.minecraft.world.item.Items.ENDER_PEARL) {
            return net.minecraft.world.entity.EntityType.ENDERMAN;
        }
        if (item == net.minecraft.world.item.Items.BLAZE_ROD || item == net.minecraft.world.item.Items.BLAZE_POWDER) {
            return net.minecraft.world.entity.EntityType.BLAZE;
        }
        if (item == net.minecraft.world.item.Items.SLIME_BALL) {
            return net.minecraft.world.entity.EntityType.SLIME;
        }
        if (item == net.minecraft.world.item.Items.MAGMA_CREAM) {
            return net.minecraft.world.entity.EntityType.MAGMA_CUBE;
        }
        if (item == net.minecraft.world.item.Items.PHANTOM_MEMBRANE) {
            return net.minecraft.world.entity.EntityType.PHANTOM;
        }
        if (item == net.minecraft.world.item.Items.GHAST_TEAR) {
            return net.minecraft.world.entity.EntityType.GHAST;
        }
        if (item == net.minecraft.world.item.Items.RABBIT || item == net.minecraft.world.item.Items.COOKED_RABBIT || item == net.minecraft.world.item.Items.RABBIT_FOOT || item == net.minecraft.world.item.Items.RABBIT_HIDE) {
            return net.minecraft.world.entity.EntityType.RABBIT;
        }
        if (item == net.minecraft.world.item.Items.COD || item == net.minecraft.world.item.Items.COOKED_COD) {
            return net.minecraft.world.entity.EntityType.COD;
        }
        if (item == net.minecraft.world.item.Items.SALMON || item == net.minecraft.world.item.Items.COOKED_SALMON) {
            return net.minecraft.world.entity.EntityType.SALMON;
        }
        if (item == net.minecraft.world.item.Items.INK_SAC || item == net.minecraft.world.item.Items.GLOW_INK_SAC) {
            return net.minecraft.world.entity.EntityType.SQUID;
        }
        if (item == net.minecraft.world.item.Items.EMERALD) {
            return net.minecraft.world.entity.EntityType.VILLAGER;
        }
        if (item == net.minecraft.world.item.Items.IRON_INGOT) {
            return net.minecraft.world.entity.EntityType.IRON_GOLEM;
        }
        return net.minecraft.world.entity.EntityType.ZOMBIE;
    }
}
