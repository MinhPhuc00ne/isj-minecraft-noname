package com.yourname.backrooms.client;

import com.yourname.backrooms.BackroomsMod;
import com.yourname.backrooms.world.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BackroomsMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BackroomsFogEvents {

    @SubscribeEvent
    public static void onComputeFogColor(ViewportEvent.ComputeFogColor event) {
        Level level = Minecraft.getInstance().level;
        if (level != null && level.dimension().equals(ModDimensions.BACKROOMS_LEVEL_0)) {
            event.setRed(0.75F);
            event.setGreen(0.70F);
            event.setBlue(0.45F);
        }
    }

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        Level level = Minecraft.getInstance().level;
        if (level != null && level.dimension().equals(ModDimensions.BACKROOMS_LEVEL_0)) {
            event.setNearPlaneDistance(4.0F);
            event.setFarPlaneDistance(28.0F);
            event.setCanceled(true);
        }
    }
}
