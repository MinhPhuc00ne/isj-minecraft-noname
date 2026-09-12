package com.minhphuc.weapons.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final String KEY_CATEGORY_WEAPONS = "key.category.weapons";
    public static final String KEY_SELECT_STONE = "key.weapons.select_stone";

    public static final KeyMapping SELECT_STONE_KEY = new KeyMapping(
            KEY_SELECT_STONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_PAGE_UP,
            KEY_CATEGORY_WEAPONS
    );

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(SELECT_STONE_KEY);
    }
}
