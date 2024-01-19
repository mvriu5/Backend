package de.noque.backend.menu;

import de.noque.backend.Network;
import de.noque.backend.model.PlayerObject;
import de.noque.backend.service.BanService;
import de.noque.backend.utils.BukkitPlayerInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class BanMenu extends BukkitPlayerInventory {

    private final BanService _banService;
    private final PlayerObject _banPlayer;

    public BanMenu(Network network, PlayerObject banPlayer) {
        super(Component.text("BanMenu", NamedTextColor.GREEN), 3);
        _banService = network.getBanService();
        _banPlayer = banPlayer;
    }

    public void open(Player player) {
        buildInventory();
        this.openInventory(player);
    }

    private void buildInventory() {


    }

}
