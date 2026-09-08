package com.yourname.darkgathering.item;

import com.yourname.darkgathering.DarkGatheringMod;
import com.yourname.darkgathering.entity.ModEntities;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
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

    // Spawn Eggs for Evil Spirits
    public static final RegistryObject<Item> EVIL_SPIRIT_SPAWN_EGG = ITEMS.register("evil_spirit_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.EVIL_SPIRIT, 0x1A1A24, 0x00E5FF, new Item.Properties()));

    public static final RegistryObject<Item> ASURA_SPIRIT_SPAWN_EGG = ITEMS.register("asura_spirit_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ASURA_SPIRIT, 0x3C0A0A, 0xFF0000, new Item.Properties()));

    public static final RegistryObject<Item> NURSE_SPIRIT_SPAWN_EGG = ITEMS.register("nurse_spirit_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.NURSE_SPIRIT, 0x0A3228, 0x00FFBB, new Item.Properties()));

    public static final RegistryObject<Item> HEAD_SPIRIT_SPAWN_EGG = ITEMS.register("head_spirit_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.HEAD_SPIRIT, 0x280032, 0xAA00FF, new Item.Properties()));

    public static final RegistryObject<Item> SHADOW_CHILD_SPAWN_EGG = ITEMS.register("shadow_child_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SHADOW_CHILD, 0x0F0F14, 0x555555, new Item.Properties()));

    public static final RegistryObject<Item> OIRAN_SPIRIT_SPAWN_EGG = ITEMS.register("oiran_spirit_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.OIRAN_SPIRIT, 0x461428, 0xFF0055, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
