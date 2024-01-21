package de.noque.backend.commands;

import de.noque.backend.Network;
import de.noque.backend.menu.BanMenu;
import de.noque.backend.model.PlayerObject;
import de.noque.backend.service.BanService;
import de.noque.backend.service.PlayerService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

public class BanCommand implements CommandExecutor {

    private final Network _network;
    private final PlayerService _playerService;
    private final BanService _banService;

    public BanCommand(Network network) {
        _network = network;
        _playerService = network.getPlayerService();
        _banService = network.getBanService();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (!player.isOp()) return false;
        if (args.length < 1) return false;

        try {
            UUID uuid = _playerService.getUUID(args[0]);

            if (uuid == null) {
                player.sendMessage("");
                return false;
            }

            new BanMenu(_network, uuid).open(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
