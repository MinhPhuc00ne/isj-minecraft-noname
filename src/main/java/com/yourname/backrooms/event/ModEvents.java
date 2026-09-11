package com.yourname.backrooms.event;

import com.yourname.backrooms.BackroomsMod;
import com.yourname.backrooms.block.BackroomsPortalFrame;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BackroomsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.getItem() instanceof FlintAndSteelItem) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            Player player = event.getEntity();

            if (BackroomsPortalFrame.trySpawnPortal(level, pos) ||
                    BackroomsPortalFrame.trySpawnPortal(level, pos.relative(event.getFace()))) {

                level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (player != null && !player.isCreative()) {
                    itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(event.getHand()));
                }
                event.setCanceled(true);
            }
        }
    }
}
