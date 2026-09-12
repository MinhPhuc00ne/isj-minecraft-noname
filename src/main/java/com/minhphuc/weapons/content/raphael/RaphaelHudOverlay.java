package com.minhphuc.weapons.content.raphael;

import com.minhphuc.weapons.WeaponsMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WeaponsMod.MOD_ID, value = Dist.CLIENT)
public class RaphaelHudOverlay {

    private static final ResourceLocation MAGIC_CIRCLE_TEX =
            new ResourceLocation(WeaponsMod.MOD_ID, "textures/gui/raphael_magic_circle.png");
    private static final ResourceLocation ANALYSIS_HUD_TEX =
            new ResourceLocation(WeaponsMod.MOD_ID, "textures/gui/raphael_analysis_hud.png");

    private static int activeHudType = 0; // 0 = None, 1 = Biome, 2 = Appraisal
    private static int hudTicksRemaining = 0;
    private static String hudTitle = "";
    private static String hudDetail = "";
    private static float rotationAngle = 0.0F;

    public static void triggerHudAnimation(int hudType, String title, String detail) {
        activeHudType = hudType;
        hudTicksRemaining = (hudType == 1) ? 90 : 120; // 4.5s or 6s duration
        hudTitle = title;
        hudDetail = detail;
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) return;
        if (hudTicksRemaining <= 0 || activeHudType == 0) return;

        hudTicksRemaining--;
        rotationAngle += 3.0F;
        if (rotationAngle >= 360.0F) rotationAngle -= 360.0F;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        GuiGraphics graphics = event.getGuiGraphics();
        Font font = mc.font;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        if (activeHudType == 1) {
            // ==========================================
            // TYPE 1: BIOME ENTRANCE MAGIC CIRCLE HUD
            // ==========================================
            int circleSize = 64;
            int x = screenWidth - circleSize - 15;
            int y = 20;

            // Render rotating magic circle
            graphics.pose().pushPose();
            graphics.pose().translate(x + circleSize / 2.0F, y + circleSize / 2.0F, 0);
            graphics.pose().mulPose(com.mojang.math.Axis.ZP.rotationDegrees(rotationAngle));
            graphics.pose().translate(-circleSize / 2.0F, -circleSize / 2.0F, 0);
            graphics.blit(MAGIC_CIRCLE_TEX, 0, 0, 0, 0, circleSize, circleSize, circleSize, circleSize);
            graphics.pose().popPose();

            // Render text next to magic circle
            int textX = x - 210;
            int textY = y + 12;

            graphics.fill(textX - 8, textY - 6, textX + 200, textY + 40, 0xAA000000);
            graphics.renderOutline(textX - 8, textY - 6, 208, 46, 0xFFFFD700);

            graphics.drawString(font, hudTitle, textX, textY, 0xFFFFD700, true);
            graphics.drawString(font, hudDetail, textX, textY + 14, 0xFFFFFFFF, true);
            graphics.drawString(font, "§7[Phân Tích Vùng Sinh Thái Hoàn Tất]", textX, textY + 26, 0xFFAAAA00, true);

        } else if (activeHudType == 2) {
            // ==========================================
            // TYPE 2: MOB APPRAISAL & 0% WIN RATE HUD
            // ==========================================
            int boxWidth = 240;
            int boxHeight = 110;
            int x = (screenWidth - boxWidth) / 2;
            int y = (screenHeight - boxHeight) / 2 - 30;

            // Render background frame
            graphics.blit(ANALYSIS_HUD_TEX, x, y, 0, 0, boxWidth, boxHeight, boxWidth, boxHeight);

            // Render small rotating circle icon inside header
            int circleSize = 36;
            int circleX = x + 15;
            int circleY = y + 12;

            graphics.pose().pushPose();
            graphics.pose().translate(circleX + circleSize / 2.0F, circleY + circleSize / 2.0F, 0);
            graphics.pose().mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-rotationAngle * 1.5F));
            graphics.pose().translate(-circleSize / 2.0F, -circleSize / 2.0F, 0);
            graphics.blit(MAGIC_CIRCLE_TEX, 0, 0, 0, 0, circleSize, circleSize, circleSize, circleSize);
            graphics.pose().popPose();

            // Text info
            graphics.drawString(font, "§e§l✦ THÔNG TUỆ VƯƠNG RAPHAEL ✦", x + 58, y + 16, 0xFFFFD700, true);
            graphics.drawString(font, "§7MA TRẬN PHÂN TÍCH THẨM ĐỊNH TẢI XONG", x + 58, y + 28, 0xFFFFAA00, true);

            graphics.drawString(font, "§fMục Tiêu Sinh Vật: §c§l" + hudTitle, x + 20, y + 52, 0xFFFFFFFF, true);
            graphics.drawString(font, "§fSinh Lực Hiện Tại: §a§l" + hudDetail, x + 20, y + 66, 0xFFFFFFFF, true);
            graphics.drawString(font, "§fTỷ Lệ Chiến Thắng: §c§l0.0% §f(Thất bại là tất yếu)", x + 20, y + 80, 0xFFFF5555, true);
        }

        RenderSystem.disableBlend();
    }
}
