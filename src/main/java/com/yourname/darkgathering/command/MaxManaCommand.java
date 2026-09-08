package com.yourname.darkgathering.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.yourname.darkgathering.capability.PlayerDataProvider;
import com.yourname.darkgathering.network.PacketHandler;
import com.yourname.darkgathering.network.S2CSyncPlayerDataPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class MaxManaCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("maxmana")
                .requires(source -> source.hasPermission(2)) // Permission level 2 (OP)
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("amount", FloatArgumentType.floatArg(1.0f))
                                .executes(context -> {
                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
                                    float amount = FloatArgumentType.getFloat(context, "amount");

                                    for (ServerPlayer player : targets) {
                                        player.getCapability(PlayerDataProvider.PLAYER_DATA).ifPresent(data -> {
                                            data.setMaxMana(amount);
                                            data.setCurrentMana(amount);
                                            PacketHandler.sendToPlayer(new S2CSyncPlayerDataPacket(data), player);
                                        });
                                    }

                                    context.getSource().sendSuccess(() ->
                                            Component.translatable("command.darkgathering.maxmana.success", targets.size(), amount), true);
                                    return targets.size();
                                })
                        )
                )
        );
    }
}
