package com.yourname.darkgathering.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrokenDollItem extends Item {
    public BrokenDollItem(Properties properties) {
        super(properties.stacksTo(16));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.literal("§c[Búp bê bị hỏng - Cần Kim Khâu & Sợi Chỉ để sửa]"));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
