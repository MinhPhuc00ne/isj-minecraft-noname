package com.yourname.darkgathering.item;

import com.yourname.darkgathering.entity.SummonRitualEntity;
import com.yourname.darkgathering.entity.ModEntities;
import net.minecraft.core.BlockPos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlushDollItem extends Item {
    public enum DollType {
        BEAR("Bear Plush Doll", "Búp Bê Gấu"),
        BUNNY("Bunny Plush Doll", "Búp Bê Thỏ"),
        GHOST("Ghost Plush Doll", "Búp Bê Ma"),
        CAT("Cat Plush Doll", "Búp Bê Mèo"),
        PUPPY("Puppy Plush Doll", "Búp Bê Chó"),
        FOX("Fox Plush Doll", "Búp Bê Cáo"),
        DEMON("Demon Plush Doll", "Búp Bê Quỷ");

        private final String englishName;
        private final String vietnameseName;

        DollType(String englishName, String vietnameseName) {
            this.englishName = englishName;
            this.vietnameseName = vietnameseName;
        }

        public String getEnglishName() { return englishName; }
        public String getVietnameseName() { return vietnameseName; }
    }

    private final DollType dollType;

    public PlushDollItem(DollType dollType, Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.RARE));
        this.dollType = dollType;
    }

    public DollType getDollType() {
        return dollType;
    }

    public static boolean isCaptured(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.contains("SpiritType") && !nbt.getString("SpiritType").isEmpty();
    }

    public static boolean isSealed(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.getBoolean("IsSealed");
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        if (isCaptured(stack) && isSealed(stack)) {
            if (!level.isClientSide) {
                BlockPos clickedPos = context.getClickedPos().relative(context.getClickedFace());

                // Spawn Ritual Entity to start 6s summoning ritual
                SummonRitualEntity ritual = new SummonRitualEntity(ModEntities.SUMMON_RITUAL.get(), level);
                ritual.setPos(clickedPos.getX() + 0.5D, clickedPos.getY(), clickedPos.getZ() + 0.5D);

                CompoundTag tag = stack.getTag();
                if (tag != null) {
                    ritual.setSpiritType(tag.getString("SpiritType"));
                    if (player != null) ritual.setOwnerUUID(player.getUUID());
                }

                level.addFreshEntity(ritual);

                if (player != null && !player.isCreative()) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.contains("SpiritType") && !nbt.getString("SpiritType").isEmpty()) {
            String spiritName = nbt.getString("SpiritName");
            float hp = nbt.getFloat("CapturedHP");
            float maxHp = nbt.getFloat("MaxHP");
            boolean sealed = nbt.getBoolean("IsSealed");

            tooltipComponents.add(Component.literal("§d[Sinh Viên Tốt Nghiệp]: §f" + (spiritName.isEmpty() ? "Ác Linh" : spiritName)));
            tooltipComponents.add(Component.literal(String.format("§a[HP]: %.1f / %.1f (%.0f%%)", hp, maxHp, (hp / maxHp) * 100.0f)));

            if (sealed) {
                tooltipComponents.add(Component.literal("§6[ĐÃ PHONG ẤN - Sẵn sàng triệu hồi]"));
            } else {
                tooltipComponents.add(Component.literal("§c[CHƯA PHONG ẤN - Ác linh có thể thoát ra!]"));
            }
        } else {
            tooltipComponents.add(Component.literal("§7[Búp bê rỗng - Dùng để thu phục Ác Linh]"));
        }
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
