package com.minhphuc.weapons.content.infinitygauntlet;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class InfinityGauntletItem extends Item {
    public static final String NBT_MODE = "SelectedMode";

    public InfinityGauntletItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    public static String getModeName(int modeOrdinal) {
        return switch (modeOrdinal) {
            case 1 -> "§dĐá Sức Mạnh (Power Stone)";
            case 2 -> "§9Đá Không Gian (Space Stone)";
            case 3 -> "§cĐá Thực Tại (Reality Stone)";
            case 4 -> "§6Đá Linh Hồn (Soul Stone)";
            case 5 -> "§aĐá Thời Gian (Time Stone)";
            case 6 -> "§eĐá Tâm Trí (Mind Stone)";
            case 7 -> "§6§lSỨC MẠNH 6 VIÊN ĐÁ (BÚNG TAY / SNAP)";
            default -> "§7Chưa chọn (Nhấn PgUp)";
        };
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide() && entity instanceof Player player) {
            boolean isHolding = player.getMainHandItem() == stack || player.getOffhandItem() == stack;

            if (isHolding) {
                // 1. Instant Health Recovery when health is missing
                if (player.getHealth() < player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }

                // Extinguish fire and refill air supply underwater
                player.clearFire();
                player.setAirSupply(player.getMaxAirSupply());

                // 2. Grant optimal beneficial potion effects seamlessly
                int duration = 240; // 12 seconds buffer
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, 4, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration, 4, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, duration, 0, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, duration, 0, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, duration, 0, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, 9, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, duration, 4, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, 1, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.SATURATION, duration, 4, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, 4, false, false, true));
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int mode = stack.hasTag() ? stack.getTag().getInt(NBT_MODE) : 0;

        if (mode == 0) {
            if (!level.isClientSide()) {
                player.displayClientMessage(
                    Component.literal("§c[Găng Tay Vô Cực] Vui lòng nhấn nút PgUp để chọn viên đá hoặc chế độ 6 viên trước!"),
                    true
                );
                level.playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.VILLAGER_NO,
                    SoundSource.PLAYERS,
                    1.0F, 1.0F
                );
            }
            return InteractionResultHolder.fail(stack);
        }

        if (mode == 1) {
            // Power Stone Mode
            if (!level.isClientSide() && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
                PowerStoneAbility.executePowerStone(serverLevel, serverPlayer, stack);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        if (mode == 7) {
            // SNAP (6 Stones Mode)
            if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
                ServerPlayer serverPlayer = (ServerPlayer) player;

                // Sound effects for snap
                serverLevel.playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.WITHER_SPAWN,
                    SoundSource.PLAYERS,
                    1.0F, 0.8F
                );
                serverLevel.playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.LIGHTNING_BOLT_THUNDER,
                    SoundSource.PLAYERS,
                    1.0F, 1.0F
                );

                serverPlayer.displayClientMessage(
                    Component.literal("§d§l[SNAP] BẠN ĐÃ BÚNG TAY THỰC HIỆN SỨC MẠNH VÔ CỰC!"),
                    true
                );

                // Async spatial scan & batch destruction offloaded to worker pool for multi-core optimization
                AABB boundingBox = player.getBoundingBox().inflate(100.0D);

                CompletableFuture.supplyAsync(() -> {
                    List<Entity> targetEntities = serverLevel.getEntities((Entity) null, boundingBox, entity -> {
                        if (entity == player) return false;
                        return (entity instanceof LivingEntity && entity.isAlive()) || (entity instanceof ItemEntity);
                    });
                    return targetEntities;
                }).thenAcceptAsync(entities -> {
                    serverLevel.getServer().execute(() -> {
                        int killedMobs = 0;
                        int removedItems = 0;

                        for (Entity entity : entities) {
                            if (!entity.isAlive() && !(entity instanceof ItemEntity)) continue;

                            if (entity instanceof LivingEntity living && entity != player) {
                                living.hurt(serverLevel.damageSources().genericKill(), 100000.0F);
                                if (living.isAlive()) {
                                    living.discard();
                                }
                                killedMobs++;
                            } else if (entity instanceof ItemEntity itemEntity) {
                                itemEntity.discard();
                                removedItems++;
                            }
                        }

                        // Spawn snap visual particle effects around player
                        for (int i = 0; i < 360; i += 10) {
                            double rad = Math.toRadians(i);
                            double px = player.getX() + Math.cos(rad) * 3.0D;
                            double pz = player.getZ() + Math.sin(rad) * 3.0D;
                            serverLevel.sendParticles(
                                ParticleTypes.END_ROD,
                                px, player.getY() + 1.0D, pz,
                                2, 0.1D, 0.5D, 0.1D, 0.05D
                            );
                            serverLevel.sendParticles(
                                ParticleTypes.DRAGON_BREATH,
                                px, player.getY() + 0.5D, pz,
                                3, 0.2D, 0.2D, 0.2D, 0.02D
                            );
                        }

                        serverLevel.sendParticles(
                            ParticleTypes.FLASH,
                            player.getX(), player.getY() + 1.5D, player.getZ(),
                            1, 0, 0, 0, 0
                        );

                        serverPlayer.sendSystemMessage(
                            Component.literal("§a§l[SNAP SUCCESS] Đã quét sạch " + killedMobs + " sinh vật và xóa " + removedItems + " vật phẩm trong phạm vi 100 blocks!")
                        );
                    });
                });

                player.getCooldowns().addCooldown(this, 40); // 2 second cooldown
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        // Individual stone actions can be added here in future
        if (!level.isClientSide()) {
            player.displayClientMessage(
                Component.literal("§e[Găng Tay Vô Cực] Đã kích hoạt " + getModeName(mode) + "! (Tính năng viên đá lẻ sẽ cập nhật sau)"),
                true
            );
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int mode = stack.hasTag() ? stack.getTag().getInt(NBT_MODE) : 0;
        tooltip.add(Component.literal("§7Chế độ hiện tại: " + getModeName(mode)));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.literal("§e⚡ Sức Mạnh Thường Trực Khi Cầm:"));
        tooltip.add(Component.literal("§7- §aTự động hồi 100% máu lập tức"));
        tooltip.add(Component.literal("§7- §aKháng 100% sát thương từ sinh vật, ngã, lửa, lava & ngạt nước"));
        tooltip.add(Component.literal("§7- §aFull hiệu ứng tích cực (Sức mạnh X, Hấp thụ, Tốc độ, Nhìn đêm, Nhanh nhẹn...)"));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.literal("§7- Hướng dẫn: Nhấn phím §e[PgUp] §7để chọn chức năng."));
        tooltip.add(Component.literal("§7- §d🔮 Đá Sức Mạnh (Power Stone)§7: Bắn laze hủy diệt. Nếu bị giam cầm trong không gian kín, chuột phải phát xung năng lượng giải thoát."));
        tooltip.add(Component.literal("§7- §6Sức mạnh 6 viên đá (Snap)§7: Chuột phải để tiêu diệt tất cả sinh vật trong 100 blocks!"));
    }
}
