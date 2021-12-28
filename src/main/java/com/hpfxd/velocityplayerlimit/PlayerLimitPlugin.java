package com.hpfxd.velocityplayerlimit;

import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "velocity-player-limit",
        name = "Velocity Player Limit",
        version = Constants.VERSION,
        authors = {"hpfxd"}
)
public class PlayerLimitPlugin {
    private final @NotNull ProxyServer proxyServer;
    private final @NotNull Logger logger;
    private final @NotNull PlayerLimitConfig storage;

    @Inject
    public PlayerLimitPlugin(@NotNull ProxyServer proxyServer,
                             @NotNull Logger logger,
                             @NotNull @DataDirectory Path dataDirectory) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.storage = new PlayerLimitConfig(dataDirectory);
    }

    @Subscribe
    public void onInitialization(@NotNull ProxyInitializeEvent event) {
        this.proxyServer.getCommandManager().register(new BrigadierCommand(new PlayerLimitCommand(this).createNode()));
    }

    @Subscribe
    public void onPing(@NotNull ProxyPingEvent event) {
        if (!this.storage.shouldShowInPlayerList()) return;

        // set maximum players in ping
        event.setPing(event.getPing().asBuilder()
                .maximumPlayers(this.storage.getPlayerLimit())
                .build());
    }

    @Subscribe
    public void onJoin(@NotNull LoginEvent event) {
        final Player player = event.getPlayer();
        if (player.hasPermission("velocityplayerlimit.bypass")) return;

        int current = this.proxyServer.getPlayerCount();
        int max = this.storage.getPlayerLimit();

        if (current >= max) {
            // server full, deny the player entry
            event.setResult(ResultedEvent.ComponentResult.denied(this.storage.getKickMessage()));
        }
    }

    public @NotNull ProxyServer getProxyServer() {
        return this.proxyServer;
    }

    public @NotNull Logger getLogger() {
        return this.logger;
    }

    public @NotNull PlayerLimitConfig getStorage() {
        return this.storage;
    }
}
