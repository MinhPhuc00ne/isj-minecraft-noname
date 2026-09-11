package com.yourname.backrooms.block;

import com.yourname.backrooms.BackroomsMod;
import com.yourname.backrooms.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
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

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
