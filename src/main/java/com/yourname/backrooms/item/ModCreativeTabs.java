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
                        pOutput.accept(ModBlocks.MOIST_CARPET.get());
                        pOutput.accept(ModBlocks.CEILING_TILE.get());
                        pOutput.accept(ModBlocks.WALLPAPER_BASEBOARD.get());
                        pOutput.accept(ModBlocks.BACKROOMS_FRAME.get());
                        pOutput.accept(ModBlocks.FLUORESCENT_LIGHT.get());
                        pOutput.accept(ModItems.ALMOND_WATER.get());
                        pOutput.accept(ModItems.BACKROOMS_IGNITER.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
