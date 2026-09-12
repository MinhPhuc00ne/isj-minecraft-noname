package com.minhphuc.weapons.content.raphael;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class RaphaelBookItem extends Item {

    public RaphaelBookItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    public static boolean isRaphaelActive(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean("RaphaelActive");
    }

    public static boolean isPlayerCarryingActiveRaphael(Player player) {
        for (ItemStack item : player.getInventory().items) {
            if (item.getItem() instanceof RaphaelBookItem && isRaphaelActive(item)) {
                return true;
            }
        }
        ItemStack offhand = player.getOffhandItem();
        return offhand.getItem() instanceof RaphaelBookItem && isRaphaelActive(offhand);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean currentActive = isRaphaelActive(stack);
        boolean newActive = !currentActive;

        stack.getOrCreateTag().putBoolean("RaphaelActive", newActive);

        if (!level.isClientSide()) {
            if (newActive) {
                player.displayClientMessage(
                    Component.literal("§e§l[THÔNG TUỆ VƯƠNG RAPHAEL] §fĐã kích hoạt! Raphael bắt đầu phân tích và đồng hành cùng Chủ Nhân. ✨"),
                    true
                );
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.5F, 1.2F);
            } else {
                player.displayClientMessage(
                    Component.literal("§7[THÔNG TUỆ VƯƠNG RAPHAEL] Đã tạm dừng trạng thái phân tích của Raphael."),
                    true
                );
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.PLAYERS, 1.0F, 0.8F);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        boolean active = isRaphaelActive(stack);
        tooltip.add(Component.literal("§7Trạng thái: " + (active ? "§a§lĐANG BẬT (ACTIVE)" : "§c§lTẮT (INACTIVE)")));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.literal("§e🧠 Sách Ma Thuật Thông Tuệ Vương Raphael (TenSura):"));
        tooltip.add(Component.literal("§7- §eChuột phải§7: Bật/Tắt chế độ đồng hành & phân tích của Raphael."));
        tooltip.add(Component.literal("§7- §aTự động nhận diện Biome§7: Xuất hiện ma trận ma thuật màu vàng kim & báo cáo vùng sinh thái khi sang khu vực mới."));
        tooltip.add(Component.literal("§7- §cThẩm định sinh vật (Mob Appraisal)§7: Đánh quái dưới 50% HP sẽ quét ma trận ma thuật toàn màn hình & công bố tỷ lệ thắng sinh vật là §c§l0.0%§7!"));
    }
}
