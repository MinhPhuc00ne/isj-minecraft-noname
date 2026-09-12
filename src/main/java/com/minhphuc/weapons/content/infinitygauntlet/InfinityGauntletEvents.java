package com.minhphuc.weapons.content.infinitygauntlet;

import com.minhphuc.weapons.WeaponsMod;
import com.minhphuc.weapons.ai.GeminiAIService;
import com.minhphuc.weapons.config.AIGeminiConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ServerChatEvent;
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
        } else {
            net.minecraft.world.entity.LivingEntity living = event.getEntity();
            // Tượng Băng bị tấn công -> Phá vỡ tượng băng & Tiêu diệt sinh vật lập tức
            if (living != null && living.getPersistentData().getBoolean("InfinityGauntletFrozen")) {
                event.setCanceled(true);
                living.getPersistentData().remove("InfinityGauntletFrozen");

                if (living.level() instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 2.0F, 0.8F);
                    serverLevel.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 2.0F, 1.2F);

                    serverLevel.sendParticles(
                        new net.minecraft.core.particles.BlockParticleOption(ParticleTypes.BLOCK, net.minecraft.world.level.block.Blocks.PACKED_ICE.defaultBlockState()),
                        living.getX(), living.getY() + 1.0D, living.getZ(),
                        40, 0.4D, 0.6D, 0.4D, 0.1D
                    );
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, living.getX(), living.getY() + 1.0D, living.getZ(), 20, 0.3D, 0.5D, 0.3D, 0.05D);

                    living.hurt(serverLevel.damageSources().genericKill(), 100000.0F);
                    if (living.isAlive()) {
                        living.discard();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (player == null || player.level().isClientSide()) return;

        ItemStack heldStack = player.getMainHandItem();
        if (!(heldStack.getItem() instanceof InfinityGauntletItem)) {
            heldStack = player.getOffhandItem();
        }

        if (heldStack.getItem() instanceof InfinityGauntletItem) {
            int mainMode = heldStack.hasTag() ? heldStack.getTag().getInt(InfinityGauntletItem.NBT_MODE) : 0;
            if (mainMode == 3) { // Đá Thực Tại (Reality Stone)
                int currentSubMode = heldStack.hasTag() ? heldStack.getTag().getInt("RealitySubMode") : 0;
                int nextSubMode = (currentSubMode + 1) % 3;
                heldStack.getOrCreateTag().putInt("RealitySubMode", nextSubMode);

                String subModeMessage = switch (nextSubMode) {
                    case 0 -> "§c[ĐÁ THỰC TẠI] §fChế độ phụ: §e1. Cấu Trúc Thực Tại (Normal)";
                    case 1 -> "§c[ĐÁ THỰC TẠI] §fChế độ phụ: §b2. Đóng Băng Thực Tại (Frozen)";
                    case 2 -> "§c[ĐÁ THỰC TẠI] §fChế độ phụ: §a3. Phục Hồi Sự Sống (Life)";
                    default -> "";
                };

                player.displayClientMessage(Component.literal(subModeMessage), true);

                float pitch = 0.8F + (nextSubMode * 0.3F);
                player.level().playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.PLAYERS,
                    1.0F, pitch
                );

                event.setCanceled(true);
            } else if (mainMode == 4) { // Đá Linh Hồn (Soul Stone)
                int currentSubMode = heldStack.hasTag() ? heldStack.getTag().getInt("SoulSubMode") : 0;
                int nextSubMode = (currentSubMode + 1) % 4;
                heldStack.getOrCreateTag().putInt("SoulSubMode", nextSubMode);

                String subModeMessage = switch (nextSubMode) {
                    case 0 -> "§6[ĐÁ LINH HỒN] §fChế độ phụ: §e1. 🔥 Soul Harvest (Gặt Hái Linh Hồn)";
                    case 1 -> "§6[ĐÁ LINH HỒN] §fChế độ phụ: §b2. 👻 Soul Puppet (Chiêu Hồn Phụ Tá)";
                    case 2 -> "§6[ĐÁ LINH HỒN] §fChế độ phụ: §c3. 💥 Soul Extraction (Tách & Thiêu Rụi Linh Hồn)";
                    case 3 -> "§6[ĐÁ LINH HỒN] §fChế độ phụ: §a4. 🧟 Kỹ Năng: Tử Linh Phục Sinh";
                    default -> "";
                };

                player.displayClientMessage(Component.literal(subModeMessage), true);

                float pitch = 0.8F + (nextSubMode * 0.3F);
                player.level().playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.SOUL_ESCAPE,
                    SoundSource.PLAYERS,
                    1.0F, pitch
                );

                event.setCanceled(true);
            } else if (mainMode == 5) { // Đá Thời Gian (Time Stone)
                int currentSubMode = heldStack.hasTag() ? heldStack.getTag().getInt("TimeSubMode") : 0;
                int nextSubMode = (currentSubMode + 1) % 3;
                heldStack.getOrCreateTag().putInt("TimeSubMode", nextSubMode);

                String subModeMessage = switch (nextSubMode) {
                    case 0 -> "§a[ĐÁ THỜI GIAN] §fChế độ phụ: §e1. ⌛ Time Rewind (Tua Ngược Thời Gian)";
                    case 1 -> "§a[ĐÁ THỜI GIAN] §fChế độ phụ: §b2. 🌿 Age Decay & Growth (Lão Hóa & Sinh Trưởng)";
                    case 2 -> "§a[ĐÁ THỜI GIAN] §fChế độ phụ: §c3. 🛑 Time Freeze Domain (Đóng Băng Thời Gian)";
                    default -> "";
                };

                player.displayClientMessage(Component.literal(subModeMessage), true);

                float pitch = 0.9F + (nextSubMode * 0.3F);
                player.level().playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BEACON_POWER_SELECT,
                    SoundSource.PLAYERS,
                    1.0F, pitch
                );

                event.setCanceled(true);
            } else if (mainMode == 6) { // Đá Tâm Trí (Mind Stone)
                int currentSubMode = heldStack.hasTag() ? heldStack.getTag().getInt("MindSubMode") : 0;
                int nextSubMode = (currentSubMode + 1) % 3;
                heldStack.getOrCreateTag().putInt("MindSubMode", nextSubMode);

                String subModeMessage = switch (nextSubMode) {
                    case 0 -> "§e[ĐÁ TÂM TRÍ] §fChế độ phụ: §61. 👑 Vương Quyền Chi Phối (Hypnosis)";
                    case 1 -> "§e[ĐÁ TÂM TRÍ] §fChế độ phụ: §b2. 🌀 Telekinesis (Thao Túng Vật Lý)";
                    case 2 -> "§e[ĐÁ TÂM TRÍ] §fChế độ phụ: §c3. ⚡ Mind Beam Laser (Vision)";
                    default -> "";
                };

                player.displayClientMessage(Component.literal(subModeMessage), true);

                float pitch = 0.9F + (nextSubMode * 0.3F);
                player.level().playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.EXPERIENCE_ORB_PICKUP,
                    SoundSource.PLAYERS,
                    1.0F, pitch
                );

                event.setCanceled(true);
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

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        if (player == null) return;

        String rawText = event.getRawText().trim();
        if (rawText.isEmpty()) return;

        ItemStack heldStack = player.getMainHandItem();
        if (!(heldStack.getItem() instanceof InfinityGauntletItem)) {
            heldStack = player.getOffhandItem();
        }

        if (!(heldStack.getItem() instanceof InfinityGauntletItem)) return;

        int mode = heldStack.hasTag() ? heldStack.getTag().getInt(InfinityGauntletItem.NBT_MODE) : 0;
        if (mode != 7) return; // Chỉ hoạt động ở Chế độ 6 viên đá (Mode 7)

        // Hủy tin nhắn chat công khai
        event.setCanceled(true);

        String prompt = rawText;
        if (prompt.isEmpty()) {
            player.sendSystemMessage(Component.literal("§c[Găng Tay Vô Cực - AI] Vui lòng nhập lệnh sau dấu // (Ví dụ: //xóa sổ các sinh vật, //cho trời mưa)"));
            return;
        }

        if (!AIGeminiConfig.isApiKeyValid()) {
            player.sendSystemMessage(Component.literal("§c[Găng Tay Vô Cực - AI] §fChưa có API Key! Hãy dán API Key vào file:\n§e" + AIGeminiConfig.getConfigAbsolutePath()));
            return;
        }

        // Thông báo cho người chơi AI đang xử lý
        player.sendSystemMessage(Component.literal("§d§l[GEMINI AI] §fĐang lắng nghe và biến mệnh lệnh §e\"" + prompt + "\" §fthành thực tại..."));

        ServerLevel level = (ServerLevel) player.level();
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.2F);

        // Gọi API bất đồng bộ
        GeminiAIService.processCommandAsync(level, player, prompt).thenAcceptAsync(response -> {
            level.getServer().execute(() -> {
                if (!response.isSuccess()) {
                    player.sendSystemMessage(Component.literal("§c[GEMINI AI ERROR] " + response.getError()));
                    return;
                }

                // Phát âm thanh & hạt sấm sét chói lọi khi AI thực thi
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.8F, 1.0F);
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.8F, 1.0F);

                for (int i = 0; i < 360; i += 20) {
                    double rad = Math.toRadians(i);
                    double px = player.getX() + Math.cos(rad) * 2.0D;
                    double pz = player.getZ() + Math.sin(rad) * 2.0D;
                    level.sendParticles(ParticleTypes.END_ROD, px, player.getY() + 1.0D, pz, 2, 0.1D, 0.3D, 0.1D, 0.05D);
                    level.sendParticles(ParticleTypes.DRAGON_BREATH, px, player.getY() + 0.5D, pz, 2, 0.1D, 0.1D, 0.1D, 0.02D);
                }

                // Thực thi các lệnh Minecraft do AI đề xuất với quyền tối cao (OP level 4)
                int executedCount = 0;
                for (String command : response.getCommands()) {
                    try {
                        level.getServer().getCommands().performPrefixedCommand(
                                player.createCommandSourceStack().withPermission(4).withSuppressedOutput(),
                                command
                        );
                        executedCount++;
                    } catch (Exception e) {
                        WeaponsMod.LOGGER.error("Failed to execute AI command: {}", command, e);
                    }
                }

                // Hiển thị lời đáp của Gemini AI
                player.sendSystemMessage(Component.literal("§d§l[GEMINI AI] §a" + response.getReply() + " §7(Đã thực thi " + executedCount + " lệnh)"));
            });
        });
    }

    private static boolean isHoldingGauntlet(Player player) {
        return player.getMainHandItem().getItem() instanceof InfinityGauntletItem
                || player.getOffhandItem().getItem() instanceof InfinityGauntletItem;
    }
}
