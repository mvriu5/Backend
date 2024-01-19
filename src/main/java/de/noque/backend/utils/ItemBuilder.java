package de.noque.backend.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class ItemBuilder {

    private final ItemStack item;

    public ItemBuilder(final Material material) {
        this(material, 1);
    }

    public ItemBuilder(final ItemStack item) {
        super();
        this.item = item;
    }

    public ItemBuilder(final Material material, final int amount) {
        super();
        this.item = new ItemStack(material, amount);
    }

    public ItemBuilder setName(final String name) {
        final var itemMeta = getItemMeta();
        itemMeta.displayName(MiniMessage.miniMessage().deserialize(name));
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setAmount(final int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder setUnbreakable(final boolean unbreakable) {
        final var itemMeta = getItemMeta();
        itemMeta.setUnbreakable(unbreakable);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchant(final Enchantment enchantment, final int level) {
        final var itemMeta = getItemMeta();
        itemMeta.addEnchant(enchantment, level, true);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchantments(final Map<Enchantment, Integer> enchantments) {
        this.item.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder removeEnchantment(final Enchantment enchantment) {
        this.item.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder addLoreLine(final String line) {
        final var itemMeta = getItemMeta();
        final List<String> lores;

        if (itemMeta.getLore() == null) lores = new ArrayList<>();
        else lores = new ArrayList<>(itemMeta.getLore());

        lores.add(line);

        itemMeta.setLore(lores);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addLoreLines(final String... lines) {
        final var itemMeta = getItemMeta();
        final List<String> lores;

        if (itemMeta.getLore() == null) lores = new ArrayList<>();
        else lores = new ArrayList<>(itemMeta.getLore());

        lores.addAll(Arrays.asList(lines));

        itemMeta.setLore(lores);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setSkullOwner(final UUID uuid) {
        final var itemMeta = getItemMeta();

        if (!(itemMeta instanceof SkullMeta skullMeta)) return this;

        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        this.item.setItemMeta(skullMeta);
        return this;
    }

    public ItemStack toItemStack() {
        return this.item;
    }

    private ItemMeta getItemMeta() {

        if (item.getItemMeta() == null)
            item.setItemMeta(Bukkit.getItemFactory().getItemMeta(item.getType()));

        return item.getItemMeta();
    }
}


