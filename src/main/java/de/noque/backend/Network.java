package de.noque.backend;

import de.noque.backend.commands.FriendCommand;
import de.noque.backend.commands.ServerCommand;
import de.noque.backend.events.LoginListener;
import de.noque.backend.service.*;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

@Getter
public final class Network extends JavaPlugin {

    private Connection connection;

    private PlayerService playerService;
    private BanService banService;
    private FriendRequestService friendRequestService;
    private ServerService serverService;

    @Override
    public void onEnable() {

        try {
            connection = new DbConnectionFactory(this).connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        playerService = new PlayerService(this);
        banService = new BanService(this);
        friendRequestService = new FriendRequestService(this);
        serverService = new ServerService(this);

        registerCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {
    }

    private void registerCommands() {
        getCommand("friend").setExecutor(new FriendCommand(this));
        getCommand("server").setExecutor(new ServerCommand(this));
        getCommand("server").setExecutor(new ServerCommand(this));

    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new LoginListener(this), this);
    }
}
