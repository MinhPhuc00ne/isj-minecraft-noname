package com.yourname.darkgathering.item;

import com.yourname.darkgathering.DarkGatheringMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DarkGatheringMod.MOD_ID);

    public static final RegistryObject<Item> DARK_GATHERING_BOOK = ITEMS.register("dark_gathering_book",
            () -> new DarkGatheringBookItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
