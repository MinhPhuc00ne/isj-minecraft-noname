package com.yourname.backrooms.item;

import com.yourname.backrooms.BackroomsMod;
import com.yourname.backrooms.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BackroomsMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BACKROOMS_TAB = CREATIVE_MODE_TABS.register("backrooms_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.YELLOW_WALLPAPER.get()))
                    .title(Component.translatable("creativetab.backrooms_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.YELLOW_WALLPAPER.get());
                        pOutput.accept(ModBlocks.MOSSY_WALLPAPER.get());
                        pOutput.accept(ModBlocks.MOIST_CARPET.get());
                        pOutput.accept(ModBlocks.GHOST_MOIST_CARPET.get());
                        pOutput.accept(ModBlocks.CEILING_TILE.get());
                        pOutput.accept(ModBlocks.WALLPAPER_BASEBOARD.get());
                        pOutput.accept(ModBlocks.BACKROOMS_FRAME.get());
                        pOutput.accept(ModBlocks.DARK_MOIST_EARTH.get());
                        pOutput.accept(ModBlocks.FLUORESCENT_LIGHT.get());
                        pOutput.accept(ModBlocks.FLICKERING_LIGHT.get());
                        pOutput.accept(ModBlocks.CASSETTE_PLAYER.get());
                        
                        pOutput.accept(ModBlocks.BUILDING_WINDOW_WALL.get());
                        pOutput.accept(ModBlocks.COURTYARD_LAWN.get());
                        pOutput.accept(ModBlocks.COURTYARD_TILE.get());
                        pOutput.accept(ModBlocks.POOL_TILE.get());
                        pOutput.accept(ModBlocks.SUBMERGED_PILLAR.get());
                        pOutput.accept(ModBlocks.BACKROOMS_SIGN.get());
                        pOutput.accept(ModBlocks.ABANDONED_WOOD_PLANK.get());
                        
                        // Doors
                        pOutput.accept(ModBlocks.YELLOW_WOOD_DOOR.get());
                        pOutput.accept(ModBlocks.OFFICE_GLASS_DOOR.get());
                        pOutput.accept(ModBlocks.VENT_METAL_DOOR.get());
                        pOutput.accept(ModBlocks.MOSSY_FOREST_DOOR.get());

                        // Items
                        pOutput.accept(ModItems.ALMOND_WATER.get());
                        pOutput.accept(ModItems.BACKROOMS_IGNITER.get());
                        pOutput.accept(ModItems.FLASHLIGHT.get());
                        pOutput.accept(ModItems.CASSETTE_TAPE_1.get());
                        pOutput.accept(ModItems.CASSETTE_TAPE_2.get());
                        pOutput.accept(ModItems.CASSETTE_TAPE_3.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
