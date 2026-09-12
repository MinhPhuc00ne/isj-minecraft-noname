package com.minhphuc.weapons;

import com.minhphuc.weapons.init.ModItems;
import com.minhphuc.weapons.init.ModKeyBindings;
import com.minhphuc.weapons.network.ModMessages;
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

@Mod(WeaponsMod.MOD_ID)
public class WeaponsMod {
    public static final String MOD_ID = "weapons";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WeaponsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Đăng ký Items và Creative Tabs
        ModItems.register(modEventBus);

        // Lifecycle events
        modEventBus.addListener(this::commonSetup);

        // Đăng ký Forge Event Bus
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);
        LOGGER.info("Weapons Mod initialized successfully!");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Weapons Mod Client Setup complete!");
        }

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            ModKeyBindings.register(event);
        }
    }
}
