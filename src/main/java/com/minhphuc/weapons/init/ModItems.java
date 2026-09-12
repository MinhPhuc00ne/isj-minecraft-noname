package com.minhphuc.weapons.init;

import com.minhphuc.weapons.WeaponsMod;
import com.minhphuc.weapons.content.infinitygauntlet.InfinityGauntletItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, WeaponsMod.MOD_ID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WeaponsMod.MOD_ID);

    // ==========================================
    // MODULE: INFINITY GAUNTLET & STONES
    // ==========================================
    public static final RegistryObject<Item> INFINITY_GAUNTLET = ITEMS.register("infinity_gauntlet",
            () -> new InfinityGauntletItem(new Item.Properties()));

    public static final RegistryObject<Item> POWER_STONE = ITEMS.register("power_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> SPACE_STONE = ITEMS.register("space_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> REALITY_STONE = ITEMS.register("reality_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> SOUL_STONE = ITEMS.register("soul_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> TIME_STONE = ITEMS.register("time_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> MIND_STONE = ITEMS.register("mind_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    // ==========================================
    // MODULE: WISDOM KING RAPHAEL (TENSURA)
    // ==========================================
    public static final RegistryObject<Item> RAPHAEL_BOOK = ITEMS.register("raphael_book",
            () -> new com.minhphuc.weapons.content.raphael.RaphaelBookItem(new Item.Properties()));

    // ==========================================
    // CREATIVE TAB
    // ==========================================
    public static final RegistryObject<CreativeModeTab> WEAPONS_TAB = CREATIVE_MODE_TABS.register("weapons_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("§6§lVũ Khí & Găng Tay Vô Cực"))
                    .icon(() -> new ItemStack(INFINITY_GAUNTLET.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(INFINITY_GAUNTLET.get());
                        output.accept(POWER_STONE.get());
                        output.accept(SPACE_STONE.get());
                        output.accept(REALITY_STONE.get());
                        output.accept(SOUL_STONE.get());
                        output.accept(TIME_STONE.get());
                        output.accept(MIND_STONE.get());
                        output.accept(RAPHAEL_BOOK.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
