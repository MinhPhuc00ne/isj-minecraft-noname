package com.yourname.darkgathering.event;

import com.yourname.darkgathering.DarkGatheringMod;
import com.yourname.darkgathering.entity.EvilSpiritEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DarkGatheringMod.MOD_ID)
public class MobSpawnEvents {

    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
        // Prevent natural spawning of vanilla hostile mobs
        if (event.getEntity() instanceof Monster && !(event.getEntity() instanceof EvilSpiritEntity)) {
            MobSpawnType spawnType = event.getSpawnType();
            // Deny natural spawning, structure spawning, spawner spawning, etc. Allow spawn eggs & command spawn.
            if (spawnType == MobSpawnType.NATURAL ||
                spawnType == MobSpawnType.CHUNK_GENERATION ||
                spawnType == MobSpawnType.PATROL ||
                spawnType == MobSpawnType.STRUCTURE ||
                spawnType == MobSpawnType.REINFORCEMENT) {

                event.setSpawnCancelled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
