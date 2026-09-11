package com.yourname.backrooms;

import com.mojang.serialization.Codec;
import com.yourname.backrooms.block.ModBlocks;
import com.yourname.backrooms.item.ModCreativeTabs;
import com.yourname.backrooms.item.ModItems;
import com.yourname.backrooms.sound.ModSounds;
import com.yourname.backrooms.world.BackroomsChunkGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BackroomsMod.MOD_ID)
public class BackroomsMod {
    public static final String MOD_ID = "backrooms";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATOR =
            DeferredRegister.create(Registries.CHUNK_GENERATOR, MOD_ID);

    public BackroomsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        CHUNK_GENERATOR.register("level_0_gen", () -> BackroomsChunkGenerator.CODEC);
        CHUNK_GENERATOR.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("The Backrooms Mod setup complete!");
    }
}
