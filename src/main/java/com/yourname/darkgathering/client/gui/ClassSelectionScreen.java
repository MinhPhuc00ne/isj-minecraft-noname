package com.yourname.darkgathering.client.gui;

import com.yourname.darkgathering.capability.IPlayerData;
import com.yourname.darkgathering.network.C2SSelectClassPacket;
import com.yourname.darkgathering.network.PacketHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClassSelectionScreen extends Screen {
    public ClassSelectionScreen() {
        super(Component.translatable("gui.darkgathering.class_selection.title"));
    }

    @Override
    protected void init() {
        super.init();
        int buttonWidth = 200;
        int buttonHeight = 20;
        int startY = this.height / 2 - 40;

        this.addRenderableWidget(Button.builder(
                Component.translatable("gui.darkgathering.class_selection.onmyoji"),
                button -> {
                    PacketHandler.sendToServer(new C2SSelectClassPacket(IPlayerData.PlayerClass.ONMYOJI));
                    this.onClose();
                }
        ).bounds(this.width / 2 - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(
                Component.translatable("gui.darkgathering.class_selection.evil_spirit"),
                button -> {
                    PacketHandler.sendToServer(new C2SSelectClassPacket(IPlayerData.PlayerClass.EVIL_SPIRIT));
                    this.onClose();
                }
        ).bounds(this.width / 2 - buttonWidth / 2, startY + 30, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(
                Component.translatable("gui.darkgathering.class_selection.decline"),
                button -> {
                    PacketHandler.sendToServer(new C2SSelectClassPacket(IPlayerData.PlayerClass.NONE));
                    this.onClose();
                }
        ).bounds(this.width / 2 - buttonWidth / 2, startY + 60, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 70, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
