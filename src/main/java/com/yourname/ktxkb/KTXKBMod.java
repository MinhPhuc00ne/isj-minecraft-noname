package com.yourname.ktxkb;

import com.yourname.ktxkb.block.ModBlocks;
import com.yourname.ktxkb.item.ModCreativeTabs;
import com.yourname.ktxkb.item.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yourname.ktxkb.command.SpawnKTXCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod(KTXKBMod.MOD_ID)
public class KTXKBMod {
    public static final String MOD_ID = "ktxkb";
    public static final Logger LOGGER = LogManager.getLogger();

    public KTXKBMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("KTX Khu B VNU-HCM Mod setup complete!");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        SpawnKTXCommand.register(event.getDispatcher());
    }
}
