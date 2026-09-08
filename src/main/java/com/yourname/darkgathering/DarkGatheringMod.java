package com.yourname.darkgathering;

import com.yourname.darkgathering.client.gui.ManaAndSkillOverlay;
import com.yourname.darkgathering.client.renderer.EvilSpiritRenderer;
import com.yourname.darkgathering.command.MaxManaCommand;
import com.yourname.darkgathering.entity.ModEntities;
import com.yourname.darkgathering.item.ModItems;
import com.yourname.darkgathering.network.PacketHandler;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod(DarkGatheringMod.MOD_ID)
public class DarkGatheringMod {
    public static final String MOD_ID = "darkgathering";

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<CreativeModeTab> DARK_GATHERING_TAB = CREATIVE_MODE_TABS.register("darkgathering_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.DARK_GATHERING_BOOK.get()))
                    .title(Component.translatable("itemGroup.darkgathering_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.DARK_GATHERING_BOOK.get());
                        output.accept(ModItems.SEWING_NEEDLE.get());
                        output.accept(ModItems.BROKEN_DOLL.get());
                        output.accept(ModItems.PLUSH_DOLL_BEAR.get());
                        output.accept(ModItems.PLUSH_DOLL_BUNNY.get());
                        output.accept(ModItems.PLUSH_DOLL_GHOST.get());
                        output.accept(ModItems.PLUSH_DOLL_CAT.get());
                        output.accept(ModItems.PLUSH_DOLL_PUPPY.get());
                        output.accept(ModItems.PLUSH_DOLL_FOX.get());
                        output.accept(ModItems.PLUSH_DOLL_DEMON.get());
                    })
                    .build());

    public DarkGatheringMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::register);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        MaxManaCommand.register(event.getDispatcher());
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.EVIL_SPIRIT.get(), EvilSpiritRenderer::new);
            event.registerEntityRenderer(ModEntities.GRADUATE_BASE.get(), EvilSpiritRenderer::new);
            event.registerEntityRenderer(ModEntities.ASURA_SPIRIT.get(), EvilSpiritRenderer::new);
            event.registerEntityRenderer(ModEntities.NURSE_SPIRIT.get(), EvilSpiritRenderer::new);
            event.registerEntityRenderer(ModEntities.HEAD_SPIRIT.get(), EvilSpiritRenderer::new);
            event.registerEntityRenderer(ModEntities.SHADOW_CHILD.get(), EvilSpiritRenderer::new);
            event.registerEntityRenderer(ModEntities.OIRAN_SPIRIT.get(), EvilSpiritRenderer::new);
            event.registerEntityRenderer(ModEntities.SUMMON_RITUAL.get(), NoopRenderer::new);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("mana_overlay", ManaAndSkillOverlay.HUD_MANA);
        }
    }
}
