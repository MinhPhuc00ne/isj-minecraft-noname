package com.minhphuc.infinitygauntlet;

import com.minhphuc.infinitygauntlet.client.ModKeyBindings;
import com.minhphuc.infinitygauntlet.item.ModItems;
import com.minhphuc.infinitygauntlet.network.ModMessages;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(InfinityGauntletMod.MOD_ID)
public class InfinityGauntletMod {
    public static final String MOD_ID = "infinitygauntlet";
    public static final Logger LOGGER = LogUtils.getLogger();

    public InfinityGauntletMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register item & creative tab deferred registers
        ModItems.register(modEventBus);

        // Register lifecycle events
        modEventBus.addListener(this::commonSetup);

        // Register mod bus to Forge event bus
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);
        LOGGER.info("Infinity Gauntlet Mod initialized successfully!");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Infinity Gauntlet Client Setup complete!");
        }

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            ModKeyBindings.register(event);
        }
    }
}
