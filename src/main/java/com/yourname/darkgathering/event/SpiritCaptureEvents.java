package com.yourname.darkgathering.event;

import com.yourname.darkgathering.DarkGatheringMod;
import com.yourname.darkgathering.entity.EvilSpiritEntity;
import com.yourname.darkgathering.entity.GraduateSpiritEntity;
import com.yourname.darkgathering.entity.ModEntities;
import com.yourname.darkgathering.item.ModItems;
import com.yourname.darkgathering.item.PlushDollItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DarkGatheringMod.MOD_ID)
public class SpiritCaptureEvents {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        ItemStack heldItem = player.getItemInHand(event.getHand());

        if (heldItem.getItem() instanceof PlushDollItem dollItem && !PlushDollItem.isCaptured(heldItem)) {
            if (event.getTarget() instanceof LivingEntity targetEntity) {
                boolean isEvilSpirit = targetEntity instanceof EvilSpiritEntity;
                boolean isGraduate = targetEntity instanceof GraduateSpiritEntity;

                if (isEvilSpirit || isGraduate) {
                    float currentHp = targetEntity.getHealth();
                    float maxHp = targetEntity.getMaxHealth();
                    float hpPercent = currentHp / maxHp;

                    // Creative mode or HP < 30% allows capture
                    if (player.isCreative() || hpPercent <= 0.30f) {
                        if (!player.level().isClientSide) {
                            // Store Spirit Data into Doll NBT
                            CompoundTag nbt = heldItem.getOrCreateTag();
                            String spiritType = targetEntity.getEncodeId();
                            if (spiritType == null || spiritType.isEmpty()) spiritType = "darkgathering:evil_spirit";

                            nbt.putString("SpiritType", spiritType);
                            nbt.putString("SpiritName", targetEntity.getDisplayName().getString());
                            nbt.putFloat("CapturedHP", currentHp);
                            nbt.putFloat("MaxHP", maxHp);
                            nbt.putBoolean("IsSealed", false);
                            nbt.putInt("EscapeTimer", 0);

                            // Despawn entity
                            targetEntity.discard();

                            player.sendSystemMessage(Component.literal("§a[+] Đã thu phục " + targetEntity.getDisplayName().getString() + " vào búp bê!"));
                        }
                        event.setCanceled(true);
                    } else {
                        if (!player.level().isClientSide) {
                            player.sendSystemMessage(Component.literal("§c[!] Ác Linh chưa đủ yếu để thu phục! Cần đánh xuống dưới 30% HP (hiện tại: " + (int)(hpPercent * 100) + "%)."));
                        }
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide && event.player instanceof ServerPlayer serverPlayer) {
            // Check inventory items every 30s (600 ticks)
            if (serverPlayer.tickCount % 600 == 0) {
                for (int i = 0; i < serverPlayer.getInventory().getContainerSize(); i++) {
                    ItemStack stack = serverPlayer.getInventory().getItem(i);
                    if (stack.getItem() instanceof PlushDollItem && PlushDollItem.isCaptured(stack) && !PlushDollItem.isSealed(stack)) {
                        CompoundTag nbt = stack.getTag();
                        if (nbt != null) {
                            float currentHp = nbt.getFloat("CapturedHP");
                            float maxHp = nbt.getFloat("MaxHP");

                            // If HP < 30%, recovers +2% HP every 30s
                            if ((currentHp / maxHp) < 0.30f) {
                                currentHp += maxHp * 0.02f;
                                nbt.putFloat("CapturedHP", currentHp);
                            }

                            // If HP >= 50%, Spirit breaks free!
                            if ((currentHp / maxHp) >= 0.50f) {
                                // Replace stack with Broken Doll
                                serverPlayer.getInventory().setItem(i, new ItemStack(ModItems.BROKEN_DOLL.get()));

                                // Spawn aggressive Evil Spirit at player position
                                if (serverPlayer.level() instanceof ServerLevel serverLevel) {
                                    EvilSpiritEntity spirit = new EvilSpiritEntity(ModEntities.EVIL_SPIRIT.get(), serverLevel);
                                    spirit.setPos(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ());
                                    serverLevel.addFreshEntity(spirit);
                                }

                                serverPlayer.sendSystemMessage(Component.literal("§c[☠] Ác Linh trong búp bê đã hồi phục trên 50% HP, phá hủy búp bê và thoát ra ngoài!"));
                            }
                        }
                    }
                }
            }
        }
    }
}
