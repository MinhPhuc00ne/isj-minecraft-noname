package com.yourname.backrooms.item;

import com.yourname.backrooms.BackroomsMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BackroomsMod.MOD_ID);

    public static final RegistryObject<Item> ALMOND_WATER = ITEMS.register("almond_water", AlmondWaterItem::new);
    public static final RegistryObject<Item> BACKROOMS_IGNITER = ITEMS.register("backrooms_igniter", BackroomsIgniterItem::new);

    // Audio Tapes
    public static final RegistryObject<Item> CASSETTE_TAPE_1 = ITEMS.register("cassette_tape_1",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CASSETTE_TAPE_2 = ITEMS.register("cassette_tape_2",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CASSETTE_TAPE_3 = ITEMS.register("cassette_tape_3",
            () -> new Item(new Item.Properties().stacksTo(1)));

    // Flashlight tool
    public static final RegistryObject<Item> FLASHLIGHT = ITEMS.register("flashlight",
            () -> new Item(new Item.Properties().durability(256)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
