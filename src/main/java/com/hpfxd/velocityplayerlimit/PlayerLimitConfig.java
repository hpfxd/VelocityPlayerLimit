package com.hpfxd.velocityplayerlimit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles loading/saving the plugin configuration.
 * <p>
 * The configuration file is saved once on startup, as well as whenever the {@code player-limit}
 * value is updated (by {@link PlayerLimitCommand}).
 */
public class PlayerLimitConfig {
    private final @NotNull ConfigurationLoader<ConfigurationNode> loader;
    private final @NotNull ConfigurationNode rootNode;

    private final boolean showInPlayerList;
    private int playerLimit;
    private final @NotNull Component kickMessage;

    public PlayerLimitConfig(@NotNull Path dataDirectory) {
        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create data directory.", e);
        }

        this.loader = YAMLConfigurationLoader.builder()
            .setPath(dataDirectory.resolve("config.yml"))
            .setFlowStyle(DumperOptions.FlowStyle.BLOCK)
            .setIndent(2)
            .setDefaultOptions(opts -> opts
                .withShouldCopyDefaults(true)
                .withHeader("Configuration for Velocity Player Limit v" + Constants.VERSION + " by hpfxd.com\n" +
                    "https://github.com/hpfxd/VelocityPlayerLimit\n" +
                    "\n" +
                    "Permission to bypass limit: \"velocityplayerlimit.bypass\"\n" +
                    "Permission to use /playerlimit: \"velocityplayerlimit.command\""))
            .build();

        try {
            this.rootNode = this.loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration.", e);
        }

        this.showInPlayerList = this.rootNode.getNode("show-in-player-list").getBoolean(true);
        this.playerLimit = this.rootNode.getNode("player-limit").getInt(1000);
        this.kickMessage = LegacyComponentSerializer.legacyAmpersand()
            .deserialize(this.rootNode.getNode("kick-message").getString("&cThe server is full!"));

        try {
            this.loader.save(this.rootNode);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save configuration.", e);
        }
    }

    public boolean shouldShowInPlayerList() {
        return this.showInPlayerList;
    }

    public int getPlayerLimit() {
        return this.playerLimit;
    }

    public @NotNull Component getKickMessage() {
        return this.kickMessage;
    }

    public void updatePlayerLimit(int limit) throws IOException {
        this.playerLimit = limit;
        this.rootNode.getNode("player-limit").setValue(limit);
        this.loader.save(this.rootNode);
    }
}
