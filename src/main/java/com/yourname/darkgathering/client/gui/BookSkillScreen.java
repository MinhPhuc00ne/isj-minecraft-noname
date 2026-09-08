package com.yourname.darkgathering.client.gui;

import com.yourname.darkgathering.capability.IPlayerData;
import com.yourname.darkgathering.capability.PlayerDataProvider;
import com.yourname.darkgathering.network.C2SSyncSkillSlotsPacket;
import com.yourname.darkgathering.network.PacketHandler;
import com.yourname.darkgathering.skill.Skill;
import com.yourname.darkgathering.skill.SkillRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class BookSkillScreen extends Screen {
    private final String[] currentSkillSlots = new String[9];
    private String selectedSkillId = "";
    private final List<Skill> availableSkills = new ArrayList<>();

    public BookSkillScreen() {
        super(Component.translatable("gui.darkgathering.book_skills.title"));
    }

    @Override
    protected void init() {
        super.init();
        availableSkills.clear();
        availableSkills.addAll(SkillRegistry.getAllSkills());

        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                for (int i = 0; i < 9; i++) {
                    currentSkillSlots[i] = data.getSkillSlot(i);
                }
            });
        }

        int startX = this.width / 2 - 190;
        int startY = 40;

        // Render available skill buttons
        for (int i = 0; i < availableSkills.size(); i++) {
            Skill skill = availableSkills.get(i);
            int y = startY + (i * 24);
            this.addRenderableWidget(Button.builder(
                    Component.literal(skill.getName() + " (" + (int)skill.getManaCost() + " MP)"),
                    b -> this.selectedSkillId = skill.getId()
            ).bounds(startX, y, 160, 20).build());
        }

        // Render 9 Skill Slots buttons at bottom
        int slotStartX = this.width / 2 - (9 * 22) / 2;
        int slotY = this.height - 40;
        for (int i = 0; i < 9; i++) {
            final int slotIndex = i;
            this.addRenderableWidget(Button.builder(
                    Component.literal(String.valueOf(i + 1)),
                    b -> {
                        if (!selectedSkillId.isEmpty()) {
                            currentSkillSlots[slotIndex] = selectedSkillId;
                            saveSkillSlots();
                        } else {
                            currentSkillSlots[slotIndex] = "";
                            saveSkillSlots();
                        }
                    }
            ).bounds(slotStartX + (i * 22), slotY, 20, 20).build());
        }
    }

    private void saveSkillSlots() {
        PacketHandler.sendToServer(new C2SSyncSkillSlotsPacket(currentSkillSlots));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFD700);

        int startX = this.width / 2 - 190;
        guiGraphics.drawString(this.font, "§eAvailable Skills (Click to Select):", startX, 28, 0xFFFFFF);

        int slotStartX = this.width / 2 - (9 * 22) / 2;
        guiGraphics.drawCenteredString(this.font, "§eHotbar 9 Skill Slots (Click slot to assign/clear selected skill):", this.width / 2, this.height - 55, 0xFFFFFF);

        // Display current skill assigned under slots
        for (int i = 0; i < 9; i++) {
            String skillId = currentSkillSlots[i];
            Skill s = SkillRegistry.getSkill(skillId);
            String name = (s != null) ? s.getName().substring(0, Math.min(4, s.getName().length())) : "-";
            guiGraphics.drawCenteredString(this.font, name, slotStartX + (i * 22) + 10, this.height - 18, 0xAAAAAA);
        }

        if (!selectedSkillId.isEmpty()) {
            Skill s = SkillRegistry.getSkill(selectedSkillId);
            if (s != null) {
                guiGraphics.drawString(this.font, "Selected: §a" + s.getName(), this.width / 2 + 10, 40, 0xFFFFFF);
                guiGraphics.drawString(this.font, "Mana: §b" + (int)s.getManaCost(), this.width / 2 + 10, 55, 0xFFFFFF);
                guiGraphics.drawString(this.font, "Cooldown: §e" + (s.getCooldownTicks() / 20) + "s", this.width / 2 + 10, 70, 0xFFFFFF);
                guiGraphics.drawWordWrap(this.font, Component.literal(s.getDescription()), this.width / 2 + 10, 90, 160, 0xCCCCCC);
            }
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
