package com.yourname.darkgathering.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.yourname.darkgathering.capability.PlayerDataProvider;
import com.yourname.darkgathering.network.C2SCastSkillPacket;
import com.yourname.darkgathering.network.PacketHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "darkgathering", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBindings {
    public static final String KEY_CATEGORY_DARKGATHERING = "key.darkgathering.category";
    public static final String KEY_TOGGLE_SKILL_HOTBAR = "key.darkgathering.toggle_skill_hotbar";

    public static final KeyMapping TOGGLE_SKILL_HOTBAR = new KeyMapping(
            KEY_TOGGLE_SKILL_HOTBAR,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            KEY_CATEGORY_DARKGATHERING
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_SKILL_HOTBAR);
    }

    @Mod.EventBusSubscriber(modid = "darkgathering", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientInputHandler {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.screen != null) return;

            if (TOGGLE_SKILL_HOTBAR.consumeClick()) {
                mc.player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                    boolean newState = !data.isSkillHotbarActive();
                    data.setSkillHotbarActive(newState);
                    mc.player.displayClientMessage(
                            net.minecraft.network.chat.Component.literal(
                                    newState ? "§b[Dark Gathering] switched to Skill Hotbar" : "§a[Dark Gathering] switched to Item Hotbar"
                            ),
                            true
                    );
                });
            }

            if (event.getAction() == GLFW.GLFW_PRESS) {
                mc.player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                    if (data.isSkillHotbarActive()) {
                        int key = event.getKey();
                        if (key >= GLFW.GLFW_KEY_1 && key <= GLFW.GLFW_KEY_9) {
                            int slotIndex = key - GLFW.GLFW_KEY_1;
                            PacketHandler.sendToServer(new C2SCastSkillPacket(slotIndex));
                        }
                    }
                });
            }
        }
    }
}
