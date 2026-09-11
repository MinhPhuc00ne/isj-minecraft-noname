package com.yourname.backrooms.block;

import com.yourname.backrooms.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class FlickeringLightBlock extends Block {
    public FlickeringLightBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(1.5F, 6.0F)
                .sound(SoundType.GLASS)
                .lightLevel(state -> 10));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(4) == 0) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.ELECTRIC_SPARK, x, y, z, 0.0D, -0.1D, 0.0D);

            if (random.nextInt(3) == 0) {
                level.playLocalSound(
                        pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                        ModSounds.FLUORESCENT_HUM.get(),
                        SoundSource.BLOCKS,
                        0.5F,
                        1.4F + random.nextFloat() * 0.4F,
                        false
                );
            }
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide() && entity instanceof LivingEntity living) {
            living.hurt(level.damageSources().lightningBolt(), 1.0F);
            living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0));
        }
        super.stepOn(level, pos, state, entity);
    }
}
