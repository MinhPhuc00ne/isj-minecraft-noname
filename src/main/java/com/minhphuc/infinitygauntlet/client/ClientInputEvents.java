package com.minhphuc.infinitygauntlet.client;

import com.minhphuc.infinitygauntlet.InfinityGauntletMod;
import com.minhphuc.infinitygauntlet.client.gui.InfinityStoneSelectScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = InfinityGauntletMod.MOD_ID, value = Dist.CLIENT)
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
