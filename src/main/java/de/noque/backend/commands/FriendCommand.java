package de.noque.backend.commands;

import de.noque.backend.Network;
import de.noque.backend.model.PlayerObject;
import de.noque.backend.service.FriendRequestService;
import de.noque.backend.service.PlayerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class FriendCommand implements CommandExecutor {

    private final PlayerService _playerService;
    private final FriendRequestService _friendRequestService;


    public FriendCommand(Network network) {
        _playerService = network.getPlayerService();
        _friendRequestService = network.getFriendRequestService();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length < 1) return false;

        String subCommand = args[0];

        try {
            if (subCommand.equalsIgnoreCase("add")) return add(args, player);
            if (subCommand.equalsIgnoreCase("remove")) return remove(args, player);
            if (subCommand.equalsIgnoreCase("accept")) return accept(args, player);
            if (subCommand.equalsIgnoreCase("deny")) return deny(args, player);
            if (subCommand.equalsIgnoreCase("list")) return list(args, player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    private boolean add(String[] args, Player player) throws SQLException {
        Player target = getPlayer(player, args[1]);
        assert target != null;

        if (_playerService.containsFriend(player.getUniqueId(), target.getUniqueId())) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You already have this player in your friend list."));
            return false;
        }

        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You've sent " + target.getName() + " a friend request."));
        target.sendMessage(MiniMessage.miniMessage().deserialize("<green>You've got a friend request from " + player.getName()));
        _friendRequestService.add(player.getUniqueId(), target.getUniqueId());
        return true;
    }

    private boolean remove(String[] args, Player player) throws SQLException {
        Player target = getPlayer(player, args[1]);
        assert target != null;

        if (!_playerService.containsFriend(player.getUniqueId(), target.getUniqueId())) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You are not friends with this player."));
            return false;
        }

        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You removed " + target.getName() + " from you friend list."));
        _playerService.removeFriend(player.getUniqueId(), target.getUniqueId());
        return true;
    }

    private boolean accept(String[] args, Player player) throws SQLException {
        Player target = getPlayer(player, args[1]);
        assert target != null;

        if (!_friendRequestService.getRequest(player.getUniqueId(), target.getUniqueId())) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You got no friend request from that player"));
            return false;
        }

        _playerService.addFriend(player.getUniqueId(), target.getUniqueId());
        _friendRequestService.remove(target.getUniqueId(), player.getUniqueId());

        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You are now friends with " + target.getName()));
        target.sendMessage(MiniMessage.miniMessage().deserialize("<green>You are now friends with " + player.getName()));

        return true;
    }

    private boolean deny(String[] args, Player player) throws SQLException {
        Player target = getPlayer(player, args[1]);
        assert target != null;

        if (!_friendRequestService.getRequest(player.getUniqueId(), target.getUniqueId())) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You got no friend request from that player"));
            return false;
        }

        _friendRequestService.remove(target.getUniqueId(), player.getUniqueId());
        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You've denied the friend request of " + target.getName()));
        return true;
    }

    private boolean list(String[] args, Player player) throws SQLException {
        List<UUID> friends = _playerService.getFriends(player.getUniqueId());

        player.sendMessage(Component.text( "Your Friends", NamedTextColor.GOLD));

        for (UUID uuid : friends) {
            Player friend = (Player) Bukkit.getOfflinePlayer(uuid);

            if (friend.isOnline())
                player.sendMessage(Component.text(friend.displayName() + " (Online)", NamedTextColor.GREEN));
            else
                player.sendMessage(Component.text(friend.displayName() + " (Offline)", NamedTextColor.GREEN));
        }
        return true;
    }

    private Player getPlayer(Player sender, String name) throws SQLException {
        UUID targetUUID = _playerService.getUUID(name);

        if (targetUUID == null) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>This player never joined the server."));
            return null;
        }

        return Bukkit.getPlayer(targetUUID);
    }
}
