package com.mod.rctvgc.commands;

import com.mod.rctvgc.prefs.VgcPrefs;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class RctVgcCommands {
    private RctVgcCommands() {}

    public static void register(RegisterCommandsEvent e) {
        e.getDispatcher().register(
                Commands.literal("rctvgc")
                        .then(Commands.literal("status")
                                .executes(ctx -> {
                                    ServerPlayer p = ctx.getSource().getPlayerOrException();
                                    boolean doubles = VgcPrefs.doublesEnabled(p, true);
                                    boolean levelEnabled = VgcPrefs.levelSetEnabled(p, true);
                                    int level = VgcPrefs.levelSet(p, 50);

                                    p.sendSystemMessage(Component.literal(
                                            "RCTVGC prefs: doubles=" + doubles + ", levelSetEnabled=" + levelEnabled + ", levelSet=" + level
                                    ));
                                    return 1;
                                })
                        )
                        .then(Commands.literal("doubles")
                                .then(Commands.literal("on").executes(ctx -> {
                                    ServerPlayer p = ctx.getSource().getPlayerOrException();
                                    VgcPrefs.setDoublesEnabled(p, true);
                                    p.sendSystemMessage(Component.literal("RCTVGC: Doubles ON"));
                                    return 1;
                                }))
                                .then(Commands.literal("off").executes(ctx -> {
                                    ServerPlayer p = ctx.getSource().getPlayerOrException();
                                    VgcPrefs.setDoublesEnabled(p, false);
                                    p.sendSystemMessage(Component.literal("RCTVGC: Doubles OFF"));
                                    return 1;
                                }))
                        )
                        .then(Commands.literal("enableLevelSet")
                                .then(Commands.literal("on")
                                        .executes(ctx -> {
                                            ServerPlayer p = ctx.getSource().getPlayerOrException();
                                            VgcPrefs.setLevelSetEnabled(p, true);
                                            p.sendSystemMessage(Component.literal("RCTVGC: LevelSet ON"));
                                            return 1;
                                        })
                                        .then(Commands.argument("level", IntegerArgumentType.integer(1, 100))
                                                .executes(ctx -> {
                                                    ServerPlayer p = ctx.getSource().getPlayerOrException();
                                                    int level = IntegerArgumentType.getInteger(ctx, "level");
                                                    VgcPrefs.setLevelSetEnabled(p, true);
                                                    VgcPrefs.setLevelSet(p, level);
                                                    p.sendSystemMessage(Component.literal("RCTVGC: LevelSet ON (" + level + ")"));
                                                    return 1;
                                                })
                                        )
                                )
                                .then(Commands.literal("off").executes(ctx -> {
                                    ServerPlayer p = ctx.getSource().getPlayerOrException();
                                    VgcPrefs.setLevelSetEnabled(p, false);
                                    p.sendSystemMessage(Component.literal("RCTVGC: LevelSet OFF"));
                                    return 1;
                                }))
                        )
        );
    }
}
