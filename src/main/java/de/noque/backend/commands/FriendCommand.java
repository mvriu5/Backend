package de.noque.backend.commands;

import de.noque.backend.Network;
import de.noque.backend.model.PlayerObject;
import de.noque.backend.service.FriendRequestService;
import de.noque.backend.service.FriendService;
import de.noque.backend.service.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class FriendCommand implements CommandExecutor {

    private final PlayerService _playerService;
    private final FriendService _friendService;
    private final FriendRequestService _friendRequestService;


    public FriendCommand(Network network) {
        _playerService = network.getPlayerService();
        _friendService = network.getFriendService();
        _friendRequestService = network.getFriendRequestService();
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

        if (subCommand.equalsIgnoreCase("accept"))
            return accept(args, player);

        if (subCommand.equalsIgnoreCase("deny"))
            return deny(args, player);

        if (subCommand.equalsIgnoreCase("list"))
            return list(args, player);

        return false;
    }

    private boolean add(String[] args, Player player) {
        Player target = getPlayer(player, args[1]);

        //checken ob bereits in friendlist

        player.sendMessage("");
        target.sendMessage("");
        _friendRequestService.add(player.getUniqueId(), target.getUniqueId());

        return true;
    }

    private boolean remove(String[] args, Player player) {
        Player target = getPlayer(player, args[1]);

        //check if player is in friendlist

        player.sendMessage("");
        _friendService.removeFriend(player.getUniqueId(), target.getUniqueId());

        return false;
    }

    private boolean accept(String[] args, Player player) {
        Player target = getPlayer(player, args[1]);

        if (!_friendRequestService.checkRequest(target.getUniqueId(), player.getUniqueId())) {
            player.sendMessage("");
            return false;
        }

        _friendService.addFriend(player.getUniqueId(), target.getUniqueId());
        _friendRequestService.remove(target.getUniqueId(), player.getUniqueId());

        player.sendMessage("");
        target.sendMessage("");

        return true;
    }

    private boolean deny(String[] args, Player player) {
        Player target = getPlayer(player, args[1]);

        if (!_friendRequestService.checkRequest(target.getUniqueId(), player.getUniqueId())) {
            player.sendMessage("");
            return false;
        }

        _friendRequestService.remove(target.getUniqueId(), player.getUniqueId());
        player.sendMessage("");
        return true;
    }

    private boolean list(String[] args, Player player) {
        List<PlayerObject> friends = _friendService.getFriends(player);

        player.sendMessage(Component.text( "Your Friends", NamedTextColor.GOLD));

        for (PlayerObject friend : friends) {
            Player friendPlayer = (Player) Bukkit.getOfflinePlayer(friend.getUuid());

            if (friendPlayer.isOnline())
                player.sendMessage(Component.text(friendPlayer.displayName() + " (Online)", NamedTextColor.GREEN));
            else
                player.sendMessage(Component.text(friendPlayer.displayName() + " (Offline)", NamedTextColor.GREEN));
        }
        return true;
    }

    private Player getPlayer(Player sender, String name) {
        UUID targetUUID = _playerService.getUUID(name);

        if (targetUUID == null)
            sender.sendMessage("");

        return Bukkit.getPlayer(targetUUID);
    }
}
