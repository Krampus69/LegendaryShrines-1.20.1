package com.krampus.legendaryshrines.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.krampus.legendaryshrines.LegendaryShrines;
import com.krampus.legendaryshrines.data.ShrineBind;
import com.krampus.legendaryshrines.data.ShrineBinding;
import com.krampus.legendaryshrines.network.ModNetwork;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber(modid = LegendaryShrines.MOD_ID)
public final class ModCommands {


    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("shrine")
                .then(Commands.literal("unlink")
                        .executes(ModCommands::unlinkSelf)
                        .then(Commands.argument("targets", EntityArgument.players())
                                .requires(source -> source.hasPermission(2))
                                .executes(context -> unlink(
                                        context.getSource(),
                                        EntityArgument.getPlayers(context, "targets")))))
                .then(Commands.literal("status")
                        .executes(ModCommands::status));

        event.getDispatcher().register(root);
    }

    private static int unlinkSelf(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();

        if (ShrineBinding.get(player) == null) {
            context.getSource().sendFailure(
                    Component.translatable("commands.legendaryshrines.unlink.none"));
            return 0;
        }

        return unlink(context.getSource(), List.of(player));
    }

    private static int unlink(CommandSourceStack source, Collection<ServerPlayer> targets) {
        int count = 0;

        for (ServerPlayer player : targets) {
            ShrineBind bind = ShrineBinding.get(player);
            if (bind == null) {
                continue;
            }

            ShrineBinding.clear(player);
            ShrineBinding.setSuppressed(player, bind.pos());
            ModNetwork.syncBind(player, null, false);
            count++;
        }

        if (count == 0) {
            source.sendFailure(Component.translatable("commands.legendaryshrines.unlink.none_matched"));
            return 0;
        }

        int unlinked = count;
        if (targets.size() == 1 && source.getEntity() == targets.iterator().next()) {
            source.sendSuccess(() -> Component.translatable("commands.legendaryshrines.unlink.self"), false);
        } else {
            source.sendSuccess(() -> Component.translatable(
                    "commands.legendaryshrines.unlink.others", unlinked), true);
        }

        return unlinked;
    }

    private static int status(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        ShrineBind bind = ShrineBinding.get(player);

        if (bind == null) {
            source.sendSuccess(() -> Component.translatable("commands.legendaryshrines.status.none"), false);
            return 0;
        }

        String position = bind.pos().getX() + " " + bind.pos().getY() + " " + bind.pos().getZ();
        String dimension = bind.dimension().location().toString();

        if (!bind.dimension().equals(player.level().dimension())) {
            source.sendSuccess(() -> Component.translatable(
                    "commands.legendaryshrines.status.other_dimension", position, dimension), false);
            return 1;
        }

        long distance = Math.round(Math.sqrt(
                bind.distanceSqrTo(player.getX(), player.getY(), player.getZ())));

        source.sendSuccess(() -> Component.translatable(
                "commands.legendaryshrines.status.linked", position, dimension, distance), false);
        return 1;
    }
}
