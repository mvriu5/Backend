package de.noque.backend.events;

import de.noque.backend.Network;
import de.noque.backend.service.FriendService;
import de.noque.backend.service.PlayerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginListener implements Listener {

    private final PlayerService _playerService;
    private final FriendService _friendService;

    public LoginListener(Network network) {
        _playerService = network.getPlayerService();
        _friendService = network.getFriendService();
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        var player = event.getPlayer();
        _playerService.add(player);
        _friendService.add(player);
    }
}
