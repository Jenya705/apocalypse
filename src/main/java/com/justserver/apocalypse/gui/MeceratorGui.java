package com.justserver.apocalypse.gui;

import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class MeceratorGui extends Gui{
    public MeceratorGui(){
        inventory = Bukkit.createInventory(null, 27, getName());
        for(int i = 0; i < 27; i++){
            if(i == 0) continue;
            inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
    }
    @Override
    public String getName() {
        return "Переработчик";
    }

    @Override
    public Gui handleClick(InventoryClickEvent event, Player player, ItemStack itemStack, InventoryView view, ClickType clickType) throws NoSuchFieldException, IllegalAccessException {
        event.setCancelled(false);
        handle(player, event);
        return new MeceratorGui();
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event, Player player, ItemStack itemStack, ClickType clickType) {
        event.setCancelled(false);
        handle(player, event);
        new MeceratorGui();
    }

    public void handle(Player player, InventoryClickEvent event){
        if(event.getCurrentItem() != null){
            if(event.getCurrentItem().getType().name().contains("_GLASS")){

                event.setCancelled(true);
                return;
            }
        }

        ItemStack recycleItem = inventory.getItem(0);
        //if(recycleItem == null) return;
        Item possibleItem = Registry.getItemByItemstack(recycleItem);
        if(possibleItem == null) {
            player.sendMessage(ChatColor.RED + "Этот предмет нельзя переработать");
            event.setCancelled(true);
            return;
        }
        if(possibleItem.minIronNuggets == 0 || possibleItem.maxIronNuggets == 0){
            event.setCancelled(true);
            return;
        }
        player.getWorld().dropItem(player.getLocation(), possibleItem.generateRandomNuggets(recycleItem.getAmount()));
        recycleItem.setAmount(0);
    }
}
