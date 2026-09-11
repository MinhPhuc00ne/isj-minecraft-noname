package com.yourname.backrooms.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GhostCarpetBlock extends Block {
    public GhostCarpetBlock() {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .strength(0.5F)
                .sound(SoundType.WOOL));
    }
}
