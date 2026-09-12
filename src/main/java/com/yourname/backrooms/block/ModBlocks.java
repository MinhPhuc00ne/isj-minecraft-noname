package com.yourname.backrooms.block;

import com.yourname.backrooms.BackroomsMod;
import com.yourname.backrooms.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BackroomsMod.MOD_ID);

    public static final RegistryObject<Block> YELLOW_WALLPAPER = registerBlock("yellow_wallpaper",
            () -> new Block(BlockBehaviour.Properties.of().strength(2.0F, 6.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> MOIST_CARPET = registerBlock("moist_carpet",
            () -> new Block(BlockBehaviour.Properties.of().strength(1.0F, 3.0F).sound(SoundType.WOOL)));

    public static final RegistryObject<Block> CEILING_TILE = registerBlock("ceiling_tile",
            () -> new Block(BlockBehaviour.Properties.of().strength(1.5F, 5.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> WALLPAPER_BASEBOARD = registerBlock("wallpaper_baseboard",
            () -> new Block(BlockBehaviour.Properties.of().strength(2.0F, 6.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> BACKROOMS_FRAME = registerBlock("backrooms_frame",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0F, 9.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> FLUORESCENT_LIGHT = registerBlock("fluorescent_light",
            FluorescentLightBlock::new);

    public static final RegistryObject<Block> BACKROOMS_PORTAL = BLOCKS.register("backrooms_portal",
            BackroomsPortalBlock::new);

    // New Expansion Blocks & Traps
    public static final RegistryObject<Block> GHOST_MOIST_CARPET = registerBlock("ghost_moist_carpet",
            GhostCarpetBlock::new);

    public static final RegistryObject<Block> FLICKERING_LIGHT = registerBlock("flickering_light",
            FlickeringLightBlock::new);

    public static final RegistryObject<Block> MOSSY_WALLPAPER = registerBlock("mossy_wallpaper",
            () -> new Block(BlockBehaviour.Properties.of().strength(2.0F, 6.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> DARK_MOIST_EARTH = registerBlock("dark_moist_earth",
            () -> new Block(BlockBehaviour.Properties.of().strength(1.0F).sound(SoundType.GRAVEL)));

    public static final RegistryObject<Block> CASSETTE_PLAYER = registerBlock("cassette_player",
            CassettePlayerBlock::new);

    // Hotel Courtyard & Ocean Poolroom Blocks
    public static final RegistryObject<Block> BUILDING_WINDOW_WALL = registerBlock("building_window_wall",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> COURTYARD_LAWN = registerBlock("courtyard_lawn",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.6F).sound(SoundType.GRASS)));

    public static final RegistryObject<Block> COURTYARD_TILE = registerBlock("courtyard_tile",
            () -> new Block(BlockBehaviour.Properties.of().strength(2.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> POOL_TILE = registerBlock("pool_tile",
            () -> new Block(BlockBehaviour.Properties.of().strength(2.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> SUBMERGED_PILLAR = registerBlock("submerged_pillar",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0F).sound(SoundType.STONE)));

    public static final RegistryObject<Block> BACKROOMS_SIGN = registerBlock("backrooms_sign",
            () -> new Block(BlockBehaviour.Properties.of().strength(1.5F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> ABANDONED_WOOD_PLANK = registerBlock("abandoned_wood_plank",
            () -> new Block(BlockBehaviour.Properties.of().strength(2.0F).sound(SoundType.WOOD)));

    // 4 Doors
    public static final RegistryObject<Block> YELLOW_WOOD_DOOR = registerDoorBlock("yellow_wood_door",
            () -> new DoorBlock(BlockBehaviour.Properties.of().strength(3.0F).sound(SoundType.WOOD).noOcclusion(), BlockSetType.OAK));

    public static final RegistryObject<Block> OFFICE_GLASS_DOOR = registerDoorBlock("office_glass_door",
            () -> new DoorBlock(BlockBehaviour.Properties.of().strength(2.0F).sound(SoundType.GLASS).noOcclusion(), BlockSetType.OAK));

    public static final RegistryObject<Block> VENT_METAL_DOOR = registerDoorBlock("vent_metal_door",
            () -> new DoorBlock(BlockBehaviour.Properties.of().strength(5.0F).sound(SoundType.METAL).noOcclusion(), BlockSetType.IRON));

    public static final RegistryObject<Block> MOSSY_FOREST_DOOR = registerDoorBlock("mossy_forest_door",
            () -> new DoorBlock(BlockBehaviour.Properties.of().strength(3.0F).sound(SoundType.WOOD).noOcclusion(), BlockSetType.OAK));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerDoorBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new DoubleHighBlockItem(toReturn.get(), new Item.Properties()));
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
