package com.yourname.backrooms.block;

import com.yourname.backrooms.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class FluorescentLightBlock extends Block {
    public FluorescentLightBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(1.5F, 6.0F)
                .sound(SoundType.GLASS)
                .lightLevel(state -> 15));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) == 0) {
            level.playLocalSound(
                    pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    ModSounds.FLUORESCENT_HUM.get(),
                    SoundSource.BLOCKS,
                    0.25F,
                    0.9F + random.nextFloat() * 0.2F,
                    false
            );
        }

        if (random.nextInt(5) == 0) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
