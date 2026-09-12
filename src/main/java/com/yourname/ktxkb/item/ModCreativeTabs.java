package com.yourname.ktxkb.item;

import com.yourname.ktxkb.KTXKBMod;
import com.yourname.ktxkb.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KTXKBMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> KTXKB_TAB = CREATIVE_MODE_TABS.register("ktxkb_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.VNU_LOGO_BLOCK.get()))
                    .title(Component.translatable("creativetab.ktxkb_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.VNU_LOGO_BLOCK.get());
                        output.accept(ModBlocks.CONG_TRUOC_SIGN.get());
                        output.accept(ModBlocks.CONG_SAU_SIGN.get());
                        output.accept(ModBlocks.LOBBY_HEADER_SIGN.get());

                        output.accept(ModBlocks.A1_SIGN.get());
                        output.accept(ModBlocks.A2_SIGN.get());
                        output.accept(ModBlocks.A3_SIGN.get());
                        output.accept(ModBlocks.A4_SIGN.get());
                        output.accept(ModBlocks.A5_SIGN.get());

                        output.accept(ModBlocks.B1_SIGN.get());
                        output.accept(ModBlocks.B2_SIGN.get());
                        output.accept(ModBlocks.B3_SIGN.get());
                        output.accept(ModBlocks.B4_SIGN.get());
                        output.accept(ModBlocks.B5_SIGN.get());

                        output.accept(ModBlocks.C1_SIGN.get());
                        output.accept(ModBlocks.C2_SIGN.get());
                        output.accept(ModBlocks.C3_SIGN.get());
                        output.accept(ModBlocks.C4_SIGN.get());
                        output.accept(ModBlocks.C5_SIGN.get());
                        output.accept(ModBlocks.C6_SIGN.get());

                        output.accept(ModBlocks.D2_SIGN.get());
                        output.accept(ModBlocks.D3_SIGN.get());
                        output.accept(ModBlocks.D4_SIGN.get());
                        output.accept(ModBlocks.D5_SIGN.get());
                        output.accept(ModBlocks.D6_SIGN.get());

                        output.accept(ModBlocks.E1_SIGN.get());
                        output.accept(ModBlocks.F1_SIGN.get());
                        output.accept(ModBlocks.F2_SIGN.get());
                        output.accept(ModBlocks.G1_SIGN.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
