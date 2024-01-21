package de.noque.backend.menu;

import de.noque.backend.Network;
import de.noque.backend.model.PlayerObject;
import de.noque.backend.model.enums.BanReason;
import de.noque.backend.service.BanService;
import de.noque.backend.utils.BukkitPlayerInventory;
import de.noque.backend.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.naming.InvalidNameException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.UUID;

public class BanMenu extends BukkitPlayerInventory {

    private final BanService _banService;
    private final UUID _banUUID;

    private BanReason _banReason;
    private Duration _banDuration;

    public BanMenu(Network network, UUID banUUID) {
        super(Component.text("BanMenu", NamedTextColor.GREEN), 3);
        _banService = network.getBanService();
        _banUUID = banUUID;
    }

    public void open(Player player) {
        buildInventory();
        this.openInventory(player);
    }

    private void buildInventory() {
        var head = new ItemBuilder(Material.PAPER).setName("").toItemStack(); //add skull
        var reasonHacks = new ItemBuilder(Material.PAPER).setName("").toItemStack();
        var reasonChat = new ItemBuilder(Material.PAPER).setName("").toItemStack();
        var timeThreeDays = new ItemBuilder(Material.PAPER).setName("").toItemStack();
        var timeOneWeek = new ItemBuilder(Material.PAPER).setName("").toItemStack();
        var timeOneMonth = new ItemBuilder(Material.PAPER).setName("").toItemStack();
        var timeThreeMonths = new ItemBuilder(Material.PAPER).setName("").toItemStack();
        var timeLifetime = new ItemBuilder(Material.PAPER).setName("").toItemStack();
        var finish = new ItemBuilder(Material.EMERALD).setName("").toItemStack();

        this.addSlot(head);
        this.addSlot(reasonHacks, inventoryClickEvent -> onReasonClick(inventoryClickEvent, BanReason.HACKS));
        this.addSlot(reasonChat, inventoryClickEvent -> onReasonClick(inventoryClickEvent, BanReason.CHAT));
        this.addSlot(timeThreeDays, inventoryClickEvent -> onDurationClick(inventoryClickEvent, Duration.ofDays(3)));
        this.addSlot(timeOneWeek, inventoryClickEvent -> onDurationClick(inventoryClickEvent, Duration.ofDays(7)));
        this.addSlot(timeOneMonth, inventoryClickEvent -> onDurationClick(inventoryClickEvent, Duration.ofDays(30)));
        this.addSlot(timeThreeMonths, inventoryClickEvent -> onDurationClick(inventoryClickEvent, Duration.ofDays(90)));
        this.addSlot(timeLifetime, inventoryClickEvent -> onDurationClick(inventoryClickEvent, Duration.ofDays(365000)));
        this.addSlot(finish, this::onFinishClick);
    }

    private void onReasonClick(InventoryClickEvent event, BanReason reason) {
        _banReason = reason;
    }

    private void onDurationClick(InventoryClickEvent event, Duration duration) {
        _banDuration = duration;
    }

    private void onFinishClick(InventoryClickEvent event) throws SQLException {
        _banService.add(_banUUID, _banReason, _banDuration);
    }

}
