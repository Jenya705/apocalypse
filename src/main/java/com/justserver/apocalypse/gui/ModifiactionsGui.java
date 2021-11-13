package com.justserver.apocalypse.gui;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.Gun;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.guns.modifications.Modify;
import com.justserver.apocalypse.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;

public class ModifiactionsGui extends Gui {

    public ItemStack gun;

    @Override
    public String getName() {
        return "Модификации";
    }

    public final Apocalypse plugin;

    public ModifiactionsGui(ItemStack gun, Apocalypse plugin) {
        this.gun = gun;
        this.plugin = plugin;
        Inventory inventory = Bukkit.createInventory(null, 27, getName());
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        for (int slot = 10; slot < 15; slot++) {
            inventory.setItem(slot, new ItemStack(Material.GRAY_DYE));
        }
        inventory.setItem(16, new ItemStack(Material.BARRIER));

        List<String> modifications = Modify.getModifications(plugin, gun);
        for (int slot = 10; slot < 10 + modifications.size(); slot++) {
            try {
                if (modifications.get(slot - 10).trim().equals("")) {
                    continue;
                }
                inventory.setItem(slot, (Registry.getItemById(modifications.get(slot - 10))).createItemStack(plugin));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.inventory = inventory;

    }

    @Override
    public Gui handleClick(InventoryClickEvent event, Player player, ItemStack itemStack, InventoryView view, ClickType clickType) {
        ItemMeta itemMeta = gun.getItemMeta();
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        if (Registry.getItemByItemstack(itemStack) instanceof Modify) {
            List<String> modifications = Modify.getModifications(plugin, gun);
            if (modifications.contains(Registry.getItemByItemstack(itemStack).getId())) {
                modifications.remove(Registry.getItemByItemstack(itemStack).getId());
            } else {
                player.sendMessage(ChatColor.DARK_RED + "Обвеса нет!");
            }
            Modify.setModifications(plugin, gun, modifications, dataContainer);
            gun.setItemMeta(itemMeta);
            player.getInventory().addItem(itemStack);
            player.closeInventory();
            plugin.guiManager.clear(player);
            plugin.guiManager.setGui(player, new ModifiactionsGui(gun, plugin));
            return this;
        }
        if (itemStack.getType().equals(Material.BARRIER)) {
            plugin.guiManager.clear(player);
            player.closeInventory();
            return null;
        } else {
            return this;
        }
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event, Player player, ItemStack itemStack, ClickType clickType) {
        ItemMeta itemMeta = gun.getItemMeta();
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        if (Registry.getItemByItemstack(itemStack) instanceof Modify modify) {
            Gun gunClass = (Gun) Registry.getItemByItemstack(gun);
            if (!modify.getForGuns().contains(gunClass.getId())) {
                player.sendMessage(ChatColor.DARK_RED + "Данный обвес нельзя установить на данное оружие!");
                return;
            }
            List<String> modifications = Modify.getModifications(plugin, gun);
            if (modifications.size() + 1 > 5) {
                player.sendMessage(ChatColor.DARK_RED + "У вас максимальное кол-во обвесов!");
                return;
            }

            if (!modifications.contains(Registry.getItemByItemstack(itemStack).getId())) {
                modifications.add(Registry.getItemByItemstack(itemStack).getId());
            } else {
                player.sendMessage(ChatColor.DARK_RED + "Обвес уже установлен!");
                return;
            }
            InventoryUtils.removeItem(player.getInventory(), itemStack, 1);
            Modify.setModifications(plugin, gun, modifications, dataContainer);
            gun.setItemMeta(itemMeta);
            player.closeInventory();
            plugin.guiManager.clear(player);
            plugin.guiManager.setGui(player, new ModifiactionsGui(gun, plugin));
        }
    }
}
