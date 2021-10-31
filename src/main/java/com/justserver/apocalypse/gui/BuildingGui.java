package com.justserver.apocalypse.gui;

import com.justserver.apocalypse.base.Base;
import com.justserver.apocalypse.base.buildings.Building;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BuildingGui extends Gui{
    public Building building;
    public Base base;

    @Override
    public String getName() {
        return "Постройка";
    }

    @Override
    public Gui handleClick(InventoryClickEvent event, Player player, ItemStack itemStack, InventoryView view, ClickType clickType) {
        if(building.needsResources.contains(itemStack)){
            int index = base.buildings.indexOf(building);
            ItemStack userItem = null;
            for(ItemStack item : player.getInventory()){
                for(ItemStack item2 : building.needsResources){
                    if(item2.getType().equals(item.getType())){
                        userItem = item;
                    }
                }
            }
            if(userItem == null) return null;
            boolean setted = false;
            for(ItemStack item : building.nowResources){
                if(item.getType().equals(userItem.getType())){
                    setted = true;
                    building.nowResources.get(building.nowResources.indexOf(item)).add(userItem.getAmount());
                }
            }
            if(!setted){
                building.nowResources.add(userItem);
            }
            player.getInventory().remove(userItem);
            if(building.nowResources.equals(building.needsResources)){
                building.isFinished = true;
                building.finish();
            }
            base.buildings.set(index, building);
            base.saveBase();
        }
        return null;
    }

    public BuildingGui(Base base, Building building){
        this.building = building;
        this.base = base;
        Inventory inventory = Bukkit.createInventory(null, 54, getName());
        for(int slot = 0; slot < inventory.getSize();slot++){
            inventory.setItem(slot, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        for(int slot = 0; slot < building.needsResources.size(); slot++){
            ItemStack item = building.needsResources.get(slot);
            List<String> lore = new ArrayList<>();
            if(building.nowResources.contains(item)){
                lore.add(building.nowResources.get(building.nowResources.indexOf(item)).getAmount() + "/" + building.needsResources.get(0).getAmount());
            }else{
                lore.add("0/" + building.needsResources.get(0).getAmount());
            }
            item.setLore(lore);
            inventory.setItem(slot, item);
        }
        this.inventory = inventory;
    }
}
