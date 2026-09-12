package com.minhphuc.weapons.client;

import com.minhphuc.weapons.WeaponsMod;
import com.minhphuc.weapons.content.infinitygauntlet.InfinityStoneSelectScreen;
import com.minhphuc.weapons.init.ModKeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WeaponsMod.MOD_ID, value = Dist.CLIENT)
public class ClientInputEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (ModKeyBindings.SELECT_STONE_KEY.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.screen == null) {
                mc.setScreen(new InfinityStoneSelectScreen());
            }
        }
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty event) {
        net.minecraft.world.entity.player.Player player = event.getEntity();
        if (player == null) return;

        net.minecraft.world.item.ItemStack heldStack = player.getMainHandItem();
        if (!(heldStack.getItem() instanceof com.minhphuc.weapons.content.infinitygauntlet.InfinityGauntletItem)) {
            heldStack = player.getOffhandItem();
        }

        if (heldStack.getItem() instanceof com.minhphuc.weapons.content.infinitygauntlet.InfinityGauntletItem) {
            int mainMode = heldStack.hasTag() ? heldStack.getTag().getInt(com.minhphuc.weapons.content.infinitygauntlet.InfinityGauntletItem.NBT_MODE) : 0;
            if (mainMode == 3) {
                com.minhphuc.weapons.network.ModMessages.sendToServer(new com.minhphuc.weapons.content.infinitygauntlet.ServerboundCycleRealitySubModePacket());
            }
        }
    }
}
