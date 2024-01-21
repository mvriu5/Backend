package de.noque.backend.events;

import de.noque.backend.Network;
import de.noque.backend.model.PlayerBan;
import de.noque.backend.model.PlayerObject;
import de.noque.backend.service.BanService;
import de.noque.backend.service.PlayerService;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.SQLException;

public class LoginListener implements Listener {

    private final PlayerService _playerService;
    private final BanService _banService;

    public LoginListener(Network network) {
        _playerService = network.getPlayerService();
        _banService = network.getBanService();
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) throws SQLException {
        var player = event.getPlayer();

        PlayerBan ban = _banService.get(player.getUniqueId());
        if (ban != null) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED,
                    Component.text(ban.getReason().toString() + " " + ban.getBannedUntil()));
        }

        var playerObject = new PlayerObject(player.getUniqueId(), player.getName());
        _playerService.add(playerObject);
    }
}
