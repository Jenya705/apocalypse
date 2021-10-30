package com.justserver.apocalypse.gui;

import com.justserver.apocalypse.base.Base;
import com.justserver.apocalypse.base.BaseHandler;
import com.justserver.apocalypse.base.buildings.Campfire;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseGui extends Gui {
    @Override
    public String getName() {
        return "База";
    }

    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, getName());;
        for(int slot = 0; slot < inventory.getSize();slot++){
            inventory.setItem(slot, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        inventory.setItem(0, new ItemStack(Material.CHEST));
        inventory.setItem(45, new ItemStack(Material.CRAFTING_TABLE));
        return inventory;
    }

    @Override
    public void init(){
        Inventory inventory = Bukkit.createInventory(null, 54, getName());
        for(int slot = 0; slot < inventory.getSize();slot++){
            inventory.setItem(slot, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        inventory.setItem(0, new ItemStack(Material.CHEST));
        inventory.setItem(10, new ItemStack(Material.CAMPFIRE));
        inventory.setItem(45, new ItemStack(Material.CRAFTING_TABLE));
        this.inventory = inventory;
    }

    public ArmorStand getArmorStand(Player player, Location armorStandsLocation, Location addLocation){
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(armorStandsLocation.add(addLocation), EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        if(armorStand.getEquipment() == null) return null;
        armorStand.getEquipment().setHelmet(new ItemStack(Material.BRICKS));
        return armorStand;
    }


    @Override
    public Gui handleClick(InventoryClickEvent event, Player player, ItemStack itemStack, InventoryView view, ClickType clickType) {
        if(itemStack.getType().equals(Material.CAMPFIRE)){
            Location armorStandsLocation = player.getTargetBlockExact(5).getLocation();

            Campfire campfire = new Campfire(armorStandsLocation, player);

            return null;
        }
        return null;
    }
}
