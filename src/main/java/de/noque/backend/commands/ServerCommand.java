package de.noque.backend.commands;

import de.noque.backend.Network;
import de.noque.backend.service.ServerService;
import de.noque.lobbysystem.LobbySystem;
import de.noque.lobbysystem.model.ServerDocument;
import de.noque.lobbysystem.service.ServerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServerCommand implements CommandExecutor {

    private final Network _network;
    private final ServerService _serverService;

    public ServerCommand(Network network) {
        _network = network;
        _serverService = network.getServerService();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length < 1) return false;

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("add"))
            return add(args, player);

        if (subCommand.equalsIgnoreCase("remove"))
            return remove(args, player);

        if (subCommand.equalsIgnoreCase("list"))
            return getList(player);

        return false;
    }

    private boolean add(String[] args, Player player) {
        if (args[1] == null || args[2] == null) {
            player.sendMessage("");
            return false;
        }

        boolean success = _serverService.add(args[1], args[2]);

        if (!success) {
            player.sendMessage("");
            return false;
        }

        if (success) {
            player.sendMessage("");
            return true;
        }
        return false;
    }

    private boolean remove(String[] args, Player player) {
        if (args[1] == null) {
            player.sendMessage("");
            return false;
        }

        boolean success = _serverService.remove(args[1]);

        if (!success) {
            player.sendMessage("");
            return false;
        }

        if (success) {
            player.sendMessage("");
            return true;
        }
        return false;
    }

    private boolean getList(Player player) {
        List<ServerDocument> servers = _serverService.loadAll();

        servers.forEach((server) ->
            player.sendMessage(Component.text(
                    NamedTextColor.YELLOW + server.getName() +
                            NamedTextColor.GREEN + " [" + server.getGameMode() + "]" +
                            NamedTextColor.GRAY + " - " + server.getState()))
        );

        return false;
    }

}
