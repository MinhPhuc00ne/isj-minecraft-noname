package com.yourname.ktxkb.block;

import com.yourname.ktxkb.KTXKBMod;
import com.yourname.ktxkb.item.ModItems;
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
            DeferredRegister.create(ForgeRegistries.BLOCKS, KTXKBMod.MOD_ID);

    // Building Signs - Cluster A
    public static final RegistryObject<Block> A1_SIGN = registerBlock("a1_sign", () -> createSignBlock());
    public static final RegistryObject<Block> A2_SIGN = registerBlock("a2_sign", () -> createSignBlock());
    public static final RegistryObject<Block> A3_SIGN = registerBlock("a3_sign", () -> createSignBlock());
    public static final RegistryObject<Block> A4_SIGN = registerBlock("a4_sign", () -> createSignBlock());
    public static final RegistryObject<Block> A5_SIGN = registerBlock("a5_sign", () -> createSignBlock());

    // Building Signs - Cluster B
    public static final RegistryObject<Block> B1_SIGN = registerBlock("b1_sign", () -> createSignBlock());
    public static final RegistryObject<Block> B2_SIGN = registerBlock("b2_sign", () -> createSignBlock());
    public static final RegistryObject<Block> B3_SIGN = registerBlock("b3_sign", () -> createSignBlock());
    public static final RegistryObject<Block> B4_SIGN = registerBlock("b4_sign", () -> createSignBlock());
    public static final RegistryObject<Block> B5_SIGN = registerBlock("b5_sign", () -> createSignBlock());

    // Building Signs - Cluster C
    public static final RegistryObject<Block> C1_SIGN = registerBlock("c1_sign", () -> createSignBlock());
    public static final RegistryObject<Block> C2_SIGN = registerBlock("c2_sign", () -> createSignBlock());
    public static final RegistryObject<Block> C3_SIGN = registerBlock("c3_sign", () -> createSignBlock());
    public static final RegistryObject<Block> C4_SIGN = registerBlock("c4_sign", () -> createSignBlock());
    public static final RegistryObject<Block> C5_SIGN = registerBlock("c5_sign", () -> createSignBlock());
    public static final RegistryObject<Block> C6_SIGN = registerBlock("c6_sign", () -> createSignBlock());

    // Building Signs - Cluster D
    public static final RegistryObject<Block> D2_SIGN = registerBlock("d2_sign", () -> createSignBlock());
    public static final RegistryObject<Block> D3_SIGN = registerBlock("d3_sign", () -> createSignBlock());
    public static final RegistryObject<Block> D4_SIGN = registerBlock("d4_sign", () -> createSignBlock());
    public static final RegistryObject<Block> D5_SIGN = registerBlock("d5_sign", () -> createSignBlock());
    public static final RegistryObject<Block> D6_SIGN = registerBlock("d6_sign", () -> createSignBlock());

    // Building Signs - Rear Cluster
    public static final RegistryObject<Block> E1_SIGN = registerBlock("e1_sign", () -> createSignBlock());
    public static final RegistryObject<Block> F1_SIGN = registerBlock("f1_sign", () -> createSignBlock());
    public static final RegistryObject<Block> F2_SIGN = registerBlock("f2_sign", () -> createSignBlock());
    public static final RegistryObject<Block> G1_SIGN = registerBlock("g1_sign", () -> createSignBlock());

    // Gates & Direction Signs
    public static final RegistryObject<Block> CONG_TRUOC_SIGN = registerBlock("cong_truoc_sign", () -> createSignBlock());
    public static final RegistryObject<Block> CONG_SAU_SIGN = registerBlock("cong_sau_sign", () -> createSignBlock());
    public static final RegistryObject<Block> LOBBY_HEADER_SIGN = registerBlock("lobby_header_sign", () -> createSignBlock());
    public static final RegistryObject<Block> VNU_LOGO_BLOCK = registerBlock("vnu_logo_block", () -> createSignBlock());

    private static Block createSignBlock() {
        return new Block(BlockBehaviour.Properties.of().strength(2.0F, 6.0F).sound(SoundType.STONE));
    }

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
