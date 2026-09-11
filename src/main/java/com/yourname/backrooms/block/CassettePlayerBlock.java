package com.yourname.backrooms.block;

import com.yourname.backrooms.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CassettePlayerBlock extends Block {
    public CassettePlayerBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(1.5F)
                .sound(SoundType.METAL));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (held.is(ModItems.CASSETTE_TAPE_1.get())) {
                level.playSound(null, pos, SoundEvents.MUSIC_DISC_13, SoundSource.RECORDS, 1.0F, 1.0F);
                player.displayClientMessage(Component.literal("Playing: Tape #01 - 'Don't look at the whispering walls...'"), true);
                return InteractionResult.SUCCESS;
            } else if (held.is(ModItems.CASSETTE_TAPE_2.get())) {
                level.playSound(null, pos, SoundEvents.MUSIC_DISC_CAT, SoundSource.RECORDS, 1.0F, 0.8F);
                player.displayClientMessage(Component.literal("Playing: Tape #02 - 'The lights... they never stop buzzing.'"), true);
                return InteractionResult.SUCCESS;
            } else if (held.is(ModItems.CASSETTE_TAPE_3.get())) {
                level.playSound(null, pos, SoundEvents.GHAST_SCREAM, SoundSource.RECORDS, 1.0F, 0.7F);
                player.displayClientMessage(Component.literal("Playing: Tape #03 - 'IT'S HERE!! RUN!!'"), true);
                return InteractionResult.SUCCESS;
            } else {
                level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.8F, 0.8F);
                player.displayClientMessage(Component.literal("Cassette Player: Insert a Cassette Tape to play audio log."), true);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
