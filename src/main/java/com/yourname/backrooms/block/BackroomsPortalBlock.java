package com.yourname.backrooms.block;

import com.yourname.backrooms.sound.ModSounds;
import com.yourname.backrooms.world.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BackroomsPortalBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public BackroomsPortalBlock() {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .strength(-1.0F)
                .sound(SoundType.GLASS)
                .lightLevel(state -> 11));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide() && entity instanceof ServerPlayer player && !player.isPassenger() && !player.isVehicle()) {

            ResourceKey<Level> currentDim = level.dimension();
            ResourceKey<Level> targetDim = currentDim.equals(ModDimensions.BACKROOMS_LEVEL_0) ?
                    Level.OVERWORLD : ModDimensions.BACKROOMS_LEVEL_0;

            ServerLevel targetLevel = player.getServer().getLevel(targetDim);
            if (targetLevel != null) {
                level.playSound(null, pos, ModSounds.PORTAL_TRAVEL.get(), SoundSource.BLOCKS, 0.8F, 1.0F);

                BlockPos targetPos;
                if (targetDim.equals(ModDimensions.BACKROOMS_LEVEL_0)) {
                    targetPos = new BlockPos(0, 65, 0);
                    // Make sure spawn area is safe
                    targetLevel.setBlock(new BlockPos(0, 64, 0), ModBlocks.MOIST_CARPET.get().defaultBlockState(), 3);
                    targetLevel.setBlock(new BlockPos(0, 65, 0), Blocks.AIR.defaultBlockState(), 3);
                    targetLevel.setBlock(new BlockPos(0, 66, 0), Blocks.AIR.defaultBlockState(), 3);
                } else {
                    targetPos = targetLevel.getSharedSpawnPos();
                }

                player.teleportTo(
                        targetLevel,
                        targetPos.getX() + 0.5D,
                        targetPos.getY(),
                        targetPos.getZ() + 0.5D,
                        player.getYRot(),
                        player.getXRot()
                );
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(3) == 0) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.END_ROD, x, y, z, 0.0D, 0.05D, 0.0D);
        }
    }
}
