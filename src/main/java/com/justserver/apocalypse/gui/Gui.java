package com.justserver.apocalypse.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public abstract class Gui implements IGui {
    public abstract String getName();

    public Inventory inventory;

    public abstract Gui handleClick(InventoryClickEvent event, Player player, ItemStack itemStack, InventoryView view, ClickType clickType) throws NoSuchFieldException, IllegalAccessException;

    public abstract void handleInventoryClick(InventoryClickEvent event, Player player, ItemStack itemStack, ClickType clickType);

    public boolean isInventory(InventoryView view) {
        return Objects.equals(getName(), view.getTitle());
    }

    public void onClose(InventoryCloseEvent event) {
    }
}
