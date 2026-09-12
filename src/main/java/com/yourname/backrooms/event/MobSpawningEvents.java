package com.yourname.backrooms.event;

import com.yourname.backrooms.BackroomsMod;
import com.yourname.backrooms.world.ModDimensions;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.Level;

@Mod.EventBusSubscriber(modid = BackroomsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MobSpawningEvents {

    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getLevel() instanceof Level level && level.dimension().equals(ModDimensions.BACKROOMS_LEVEL_0)) {

            if (event.getEntity() instanceof Enemy ||
                event.getEntity().getType().getCategory() == MobCategory.MONSTER) {
                event.setSpawnCancelled(true);
                event.setResult(Event.Result.DENY);
                return;
            }

            if (event.getEntity() instanceof Animal || event.getEntity() instanceof Bat) {
                if (event.getLevel().getRandom().nextFloat() > 0.05F) {
                    event.setSpawnCancelled(true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
}
