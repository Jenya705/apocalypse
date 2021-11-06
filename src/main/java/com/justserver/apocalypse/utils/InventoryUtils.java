package com.justserver.apocalypse.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
    public static Inventory removeItem(Inventory inventory, ItemStack item, int i){
        for(ItemStack itemInInventory : inventory){
            if(itemInInventory == null) continue;
            if(item == null) continue;
            if(itemInInventory.equals(item)){
                itemInInventory.setAmount(itemInInventory.getAmount() - i);
                return inventory;
            }
        }
        return inventory;
    }

    public static boolean hasItem(Inventory inventory, ItemStack item, int i){
        int count = 0;
        for(ItemStack itemInInventory : inventory){
            if(itemInInventory == null) continue;
            if(item == null) continue;
            int amount = itemInInventory.getAmount();
            itemInInventory.setAmount(item.getAmount());
            if(itemInInventory.equals(item)){
                count += amount;
            }
            itemInInventory.setAmount(amount);
            if(count >= i){
                return true;
            }
        }
        return false;
    }
}
