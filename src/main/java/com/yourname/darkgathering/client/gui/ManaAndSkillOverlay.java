package com.yourname.darkgathering.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.yourname.darkgathering.DarkGatheringMod;
import com.yourname.darkgathering.capability.IPlayerData;
import com.yourname.darkgathering.capability.PlayerDataProvider;
import com.yourname.darkgathering.skill.Skill;
import com.yourname.darkgathering.skill.SkillRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ManaAndSkillOverlay {
    public static final IGuiOverlay HUD_MANA = (gui, guiGraphics, partialTick, width, height) -> {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        mc.player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
            if (data.getPlayerClass() == IPlayerData.PlayerClass.NONE) return;

            float currentMana = data.getCurrentMana();
            float maxMana = data.getMaxMana();

            int barWidth = 100;
            int barHeight = 8;
            int x = 10;
            int y = height - 25;

            // Draw Mana Bar background
            guiGraphics.fill(x - 1, y - 1, x + barWidth + 1, y + barHeight + 1, 0xFF000000);
            guiGraphics.fill(x, y, x + barWidth, y + barHeight, 0xFF333333);

            // Draw Mana Bar fill (Cyan blue)
            int fillWidth = (int) ((currentMana / maxMana) * barWidth);
            guiGraphics.fill(x, y, x + fillWidth, y + barHeight, 0xFF00AACC);

            // Draw Mana text
            String text = String.format("Mana: %.0f / %.0f", currentMana, maxMana);
            guiGraphics.drawString(mc.font, text, x + 2, y - 10, 0x00E5FF, true);

            // Render 9 Skill Slots if Skill Hotbar is Active
            if (data.isSkillHotbarActive()) {
                int hotbarWidth = 9 * 20;
                int hotbarX = (width - hotbarWidth) / 2;
                int hotbarY = height - 50;

                guiGraphics.drawCenteredString(mc.font, "§b[SKILL HOTBAR ACTIVE - PRESS 1-9]", width / 2, hotbarY - 12, 0x00E5FF);

                for (int i = 0; i < 9; i++) {
                    int slotX = hotbarX + (i * 20);

                    // Slot frame
                    guiGraphics.fill(slotX, hotbarY, slotX + 18, hotbarY + 18, 0xAA000000);
                    guiGraphics.fill(slotX + 1, hotbarY + 1, slotX + 17, hotbarY + 17, 0x5500AACC);

                    String skillId = data.getSkillSlot(i);
                    Skill s = SkillRegistry.getSkill(skillId);
                    if (s != null) {
                        int cd = data.getCooldown(skillId);
                        if (cd > 0) {
                            String cdText = String.valueOf(cd / 20);
                            guiGraphics.drawCenteredString(mc.font, cdText, slotX + 9, hotbarY + 5, 0xFF5555);
                        } else {
                            String label = s.getName().substring(0, Math.min(2, s.getName().length()));
                            guiGraphics.drawCenteredString(mc.font, label, slotX + 9, hotbarY + 5, 0xFFFFFF);
                        }
                    }

                    // Key number
                    guiGraphics.drawString(mc.font, String.valueOf(i + 1), slotX + 2, hotbarY + 2, 0xFFFF55, false);
                }
            }
        });
    };
}
