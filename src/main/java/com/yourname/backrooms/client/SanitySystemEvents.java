package com.yourname.backrooms.client;

import com.yourname.backrooms.BackroomsMod;
import com.yourname.backrooms.world.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BackroomsMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SanitySystemEvents {
    private static int ticksInDarkness = 0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Level level = mc.level;

        if (player != null && level != null && level.dimension().equals(ModDimensions.BACKROOMS_LEVEL_0)) {
            BlockPos pos = player.blockPosition();
            int light = level.getMaxLocalRawBrightness(pos);

            if (light <= 4) {
                ticksInDarkness++;
                if (ticksInDarkness > 200) { // After 10 seconds in darkness
                    RandomSource random = level.getRandom();
                    if (random.nextInt(120) == 0) {
                        level.playLocalSound(
                                pos.getX() + random.nextInt(10) - 5,
                                pos.getY(),
                                pos.getZ() + random.nextInt(10) - 5,
                                SoundEvents.AMBIENT_CAVE.get(),
                                SoundSource.AMBIENT,
                                0.6F,
                                0.6F + random.nextFloat() * 0.3F,
                                false
                        );
                    }
                }
            } else {
                ticksInDarkness = Math.max(0, ticksInDarkness - 2);
            }
        } else {
            ticksInDarkness = 0;
        }
    }
}
