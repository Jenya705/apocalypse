package com.justserver.apocalypse.protection;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class BlacklistedItemsHandler implements Listener {
    @EventHandler
    public void onHeldItem(PlayerItemHeldEvent event){
        ItemStack newSlot = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if(newSlot == null) return;
        if(newSlot.getType().name().contains("STAINED_GLASS_PANE") || newSlot.getType().equals(Material.WATER_BUCKET)){
            newSlot.setAmount(0);
            event.getPlayer().sendMessage(ChatColor.RED + "У вас в инвентаре был запрещенный предмет, увы нам пришлось его удалить.");
        }
    }
}
