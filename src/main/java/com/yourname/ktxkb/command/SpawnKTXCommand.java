package com.yourname.ktxkb.command;

import com.mojang.brigadier.CommandDispatcher;
import com.yourname.ktxkb.world.KTXCampusBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SpawnKTXCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawn_ktx")
                .requires(source -> source.hasPermission(0)) // Available to all players
                .executes(context -> {
                    CommandSourceStack source = context.getSource();
                    ServerPlayer player = source.getPlayerOrException();
                    BlockPos pos = player.blockPosition();

                    source.sendSuccess(() -> Component.literal("§a[KTXKB] Dang tu dong sinh toan bo khuon vien KTX Khu B (1:1)..."), true);
                    
                    KTXCampusBuilder.buildFullCampus(player.level(), pos);
                    
                    source.sendSuccess(() -> Component.literal("§e[KTXKB] Xay dung thanh cong 24 toà nha, cong V va khuon vien KTX Khu B!"), true);
                    return 1;
                }));
    }
}
