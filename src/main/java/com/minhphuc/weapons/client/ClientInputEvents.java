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
}
