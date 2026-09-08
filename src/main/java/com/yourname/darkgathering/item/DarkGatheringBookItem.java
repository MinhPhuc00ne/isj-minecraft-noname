package com.yourname.darkgathering.item;

import com.yourname.darkgathering.DarkGatheringMod;
import com.yourname.darkgathering.capability.IPlayerData;
import com.yourname.darkgathering.capability.PlayerDataProvider;
import com.yourname.darkgathering.client.gui.BookSkillScreen;
import com.yourname.darkgathering.client.gui.ClassSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class DarkGatheringBookItem extends Item {
    public DarkGatheringBookItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(net.minecraft.world.level.Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                if (data.getPlayerClass() == IPlayerData.PlayerClass.NONE) {
                    Minecraft.getInstance().setScreen(new ClassSelectionScreen());
                } else {
                    Minecraft.getInstance().setScreen(new BookSkillScreen());
                }
            });
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            return stack.getHoverName();
        }
        return Component.translatable("item.darkgathering.dark_gathering_book");
    }
}
