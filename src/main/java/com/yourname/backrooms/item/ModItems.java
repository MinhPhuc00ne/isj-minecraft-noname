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

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
