package com.minhphuc.weapons.content.infinitygauntlet;

import com.minhphuc.weapons.network.ModMessages;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class InfinityStoneSelectScreen extends Screen {

    public InfinityStoneSelectScreen() {
        super(Component.literal("Infinity Stone Select"));
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = 220;
        int buttonHeight = 20;
        int centerX = this.width / 2 - buttonWidth / 2;
        int startY = this.height / 2 - 90;

        // Button 7: SNAP MODE (ALL 6 STONES) - Prominent top button
        this.addRenderableWidget(
            Button.builder(Component.literal("§6§l✦ SỨC MẠNH 6 VIÊN ĐÁ (BÚNG TAY / SNAP) ✦"), button -> {
                selectMode(7);
            }).bounds(centerX, startY, buttonWidth, 24).build()
        );

        int currentY = startY + 32;

        // Stone 1: Power Stone
        this.addRenderableWidget(
            Button.builder(Component.literal("§d🔮 Đá Sức Mạnh (Power Stone)"), button -> {
                selectMode(1);
            }).bounds(centerX, currentY, buttonWidth, buttonHeight).build()
        );
        currentY += 24;

        // Stone 2: Space Stone
        this.addRenderableWidget(
            Button.builder(Component.literal("§9🌌 Đá Không Gian (Space Stone)"), button -> {
                selectMode(2);
            }).bounds(centerX, currentY, buttonWidth, buttonHeight).build()
        );
        currentY += 24;

        // Stone 3: Reality Stone
        this.addRenderableWidget(
            Button.builder(Component.literal("§c🔴 Đá Thực Tại (Reality Stone)"), button -> {
                selectMode(3);
            }).bounds(centerX, currentY, buttonWidth, buttonHeight).build()
        );
        currentY += 24;

        // Stone 4: Soul Stone
        this.addRenderableWidget(
            Button.builder(Component.literal("§6🔥 Đá Linh Hồn (Soul Stone)"), button -> {
                selectMode(4);
            }).bounds(centerX, currentY, buttonWidth, buttonHeight).build()
        );
        currentY += 24;

        // Stone 5: Time Stone
        this.addRenderableWidget(
            Button.builder(Component.literal("§a⌛ Đá Thời Gian (Time Stone)"), button -> {
                selectMode(5);
            }).bounds(centerX, currentY, buttonWidth, buttonHeight).build()
        );
        currentY += 24;

        // Stone 6: Mind Stone
        this.addRenderableWidget(
            Button.builder(Component.literal("§e🧠 Đá Tâm Trí (Mind Stone)"), button -> {
                selectMode(6);
            }).bounds(centerX, currentY, buttonWidth, buttonHeight).build()
        );
    }

    private void selectMode(int modeOrdinal) {
        ModMessages.sendToServer(new ServerboundSelectModePacket(modeOrdinal));
        this.onClose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Dark semi-transparent background fill overlay
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0xD0000000, 0xEE110826);

        // Header Title
        guiGraphics.drawCenteredString(
            this.font,
            "§6§lGĂNG TAY VÔ CỰC - CHỌN SỨC MẠNH",
            this.width / 2,
            this.height / 2 - 110,
            0xFFFFFF
        );

        guiGraphics.drawCenteredString(
            this.font,
            "§7Nhấp vào chế độ mong muốn để kích hoạt phím chuột phải",
            this.width / 2,
            this.height / 2 + 105,
            0xAAAAAA
        );

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
