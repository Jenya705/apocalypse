package com.justserver.apocalypse.overworld;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.normal.Medkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.Objects;

public class OverworldHandler implements Listener {
    private final Apocalypse plugin;


    public OverworldHandler(Apocalypse apocalypse) {
        this.plugin = apocalypse;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().getInventory().addItem(Registry.FLYING_AXE.createItemStack(plugin));
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        plugin.guiManager.clear(event.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getItem() == null) return;
        if(event.getItem().getItemMeta() == null) return;
        if(!Objects.equals(event.getHand(), EquipmentSlot.HAND)) return;
        Item possibleItem = Registry.getItemByItemstack(event.getItem());
        if(possibleItem == null) return;
        possibleItem.onInteract(event);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            ItemStack itemStack = ((Player) event.getDamager()).getInventory().getItemInMainHand();
            itemStack = (itemStack.getType().equals(Material.AIR) ? null : itemStack);
            if(itemStack == null) return;
            Item possibleItem = Registry.getItemByItemstack(itemStack);
            if(possibleItem == null) return;
            event.setDamage(possibleItem.getLeftDamage());
        }
    }

//    @EventHandler
//    public void onChangeEquipment(PlayerItemHeldEvent event){
//        ItemStack possibleItem = event.getPlayer().getInventory().getItem(event.getNewSlot());
//        if(possibleItem == null || possibleItem.getType().equals(Material.AIR)) return;
//    }
}
