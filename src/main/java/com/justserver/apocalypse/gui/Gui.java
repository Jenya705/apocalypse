package com.justserver.apocalypse.gui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import java.util.Objects;

public abstract class Gui {
    public abstract String getName();
    public Inventory inventory;
    public abstract Gui handleClick(InventoryClickEvent event, Player player, ItemStack itemStack, InventoryView view, ClickType clickType);
    public abstract Gui handleInventoryClick(InventoryClickEvent event, Player player, ItemStack itemStack, ClickType clickType);
    public boolean isInventory(InventoryView view){
        return Objects.equals(getName(), view.getTitle());
    }
}
