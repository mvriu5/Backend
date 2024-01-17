package de.noque.backend;

import de.noque.backend.commands.FriendCommand;
import de.noque.backend.commands.ServerCommand;
import de.noque.backend.events.LoginListener;
import de.noque.backend.service.FriendRequestService;
import de.noque.backend.service.FriendService;
import de.noque.backend.service.PlayerService;
import de.noque.backend.service.ServerService;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Network extends JavaPlugin {

    private @Getter MongoManager mongoManager;

    private @Getter PlayerService playerService;
    private @Getter FriendService friendService;
    private @Getter FriendRequestService friendRequestService;
    private @Getter ServerService serverService;

    @Override
    public void onEnable() {
        mongoManager = new MongoManager(this);

        playerService = new PlayerService(this);
        friendService = new FriendService(this);
        friendRequestService = new FriendRequestService(this);
        serverService = new ServerService(this);

        registerCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        getCommand("friend").setExecutor(new FriendCommand(this));
        getCommand("server").setExecutor(new ServerCommand(this));
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new LoginListener(this), this);
    }
}
