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

        PlayerObject banPlayer = _playerService.get(args[0]);

        if (banPlayer == null) {
            player.sendMessage("");
            return false;
        }

        if (_banService.get(banPlayer.getUuid()) != null) {
            player.sendMessage("");
            return false;
        }

        new BanMenu(_network, banPlayer).open(player);

        return false;
    }
}
