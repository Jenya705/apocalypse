package com.justserver.apocalypse.protection;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlacklistedItemsHandler implements Listener {

    @EventHandler
    public void onHeldItem(PlayerItemHeldEvent event) {
        ItemStack newSlot = event.getPlayer().getInventory().getItem(event.getNewSlot());
        checkItemIfBlacklistedDelete(event.getPlayer(), newSlot);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for (ItemStack itemStack : event.getPlayer().getInventory()) {
            checkItemIfBlacklistedDelete(event.getPlayer(), itemStack);
        }
        checkItemIfBlacklistedDelete(event.getPlayer(), event.getPlayer().getItemInUse());
        checkItemIfBlacklistedDelete(event.getPlayer(), event.getPlayer().getActiveItem());
        checkItemIfBlacklistedDelete(event.getPlayer(), event.getPlayer().getItemOnCursor());
        checkItemIfBlacklistedDelete(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
        checkItemIfBlacklistedDelete(event.getPlayer(), event.getPlayer().getInventory().getItemInOffHand());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        for (ItemStack itemStack : event.getPlayer().getInventory()) {
            checkItemIfBlacklistedDelete(event.getPlayer(), itemStack);
        }
        checkItemIfBlacklistedDelete(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
        checkItemIfBlacklistedDelete(event.getPlayer(), event.getPlayer().getInventory().getItemInOffHand());
    }

    public void checkItemIfBlacklistedDelete(@NotNull Player player, @Nullable ItemStack itemStack) {
        if (itemStack == null) return;
        if (itemStack.getType().equals(Material.AIR)) return;
        if (itemStack.getType().name().contains("STAINED_GLASS_PANE") || itemStack.getType().equals(Material.WATER_BUCKET)) {
            itemStack.setAmount(0);
            player.sendMessage(ChatColor.RED + "У вас в инвентаре был запрещенный предмет, увы нам пришлось его удалить.");
        }
    }

}
