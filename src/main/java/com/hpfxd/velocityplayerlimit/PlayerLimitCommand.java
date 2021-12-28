package com.hpfxd.velocityplayerlimit;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DecimalFormat;

public class PlayerLimitCommand {
    private static final @NotNull DecimalFormat FORMAT = new DecimalFormat("#,###");

    private final @NotNull PlayerLimitPlugin plugin;

    public PlayerLimitCommand(@NotNull PlayerLimitPlugin plugin) {
        this.plugin = plugin;
    }

    public @NotNull LiteralArgumentBuilder<CommandSource> createNode() {
        return LiteralArgumentBuilder.<CommandSource>literal("playerlimit")
                .requires(s -> s.hasPermission("velocityplayerlimit.command"))
                .then(
                        RequiredArgumentBuilder.<CommandSource, Integer>argument("limit", IntegerArgumentType.integer(0))
                                .executes(ctx -> {
                                    int limit = ctx.getArgument("limit", Integer.class);

                                    try {
                                        this.plugin.getStorage().updatePlayerLimit(limit);
                                    } catch (IOException e) {
                                        throw new RuntimeException("Failed to save player limit.", e);
                                    }

                                    this.plugin.getLogger().info("Updated player limit to {}.", FORMAT.format(limit));

                                    ctx.getSource().sendMessage(Component.text("Updated player limit to ", NamedTextColor.GREEN)
                                            .append(Component.text(FORMAT.format(limit), NamedTextColor.YELLOW))
                                            .append(Component.text(".")));
                                    return 1;
                                })
                ).executes((ctx) -> {
                    int limit = this.plugin.getStorage().getPlayerLimit();

                    ctx.getSource().sendMessage(Component.text("The current player limit is ", NamedTextColor.GREEN)
                            .append(Component.text(FORMAT.format(limit), NamedTextColor.YELLOW))
                            .append(Component.text(".")));
                    return 1;
                });
    }
}
