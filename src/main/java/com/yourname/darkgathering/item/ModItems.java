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

    public static final RegistryObject<Item> SEWING_NEEDLE = ITEMS.register("sewing_needle",
            () -> new SewingNeedleItem(new Item.Properties()));

    public static final RegistryObject<Item> BROKEN_DOLL = ITEMS.register("broken_doll",
            () -> new BrokenDollItem(new Item.Properties()));

    // 7 Plush Doll Variants
    public static final RegistryObject<Item> PLUSH_DOLL_BEAR = ITEMS.register("plush_doll_bear",
            () -> new PlushDollItem(PlushDollItem.DollType.BEAR, new Item.Properties()));

    public static final RegistryObject<Item> PLUSH_DOLL_BUNNY = ITEMS.register("plush_doll_bunny",
            () -> new PlushDollItem(PlushDollItem.DollType.BUNNY, new Item.Properties()));

    public static final RegistryObject<Item> PLUSH_DOLL_GHOST = ITEMS.register("plush_doll_ghost",
            () -> new PlushDollItem(PlushDollItem.DollType.GHOST, new Item.Properties()));

    public static final RegistryObject<Item> PLUSH_DOLL_CAT = ITEMS.register("plush_doll_cat",
            () -> new PlushDollItem(PlushDollItem.DollType.CAT, new Item.Properties()));

    public static final RegistryObject<Item> PLUSH_DOLL_PUPPY = ITEMS.register("plush_doll_puppy",
            () -> new PlushDollItem(PlushDollItem.DollType.PUPPY, new Item.Properties()));

    public static final RegistryObject<Item> PLUSH_DOLL_FOX = ITEMS.register("plush_doll_fox",
            () -> new PlushDollItem(PlushDollItem.DollType.FOX, new Item.Properties()));

    public static final RegistryObject<Item> PLUSH_DOLL_DEMON = ITEMS.register("plush_doll_demon",
            () -> new PlushDollItem(PlushDollItem.DollType.DEMON, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
