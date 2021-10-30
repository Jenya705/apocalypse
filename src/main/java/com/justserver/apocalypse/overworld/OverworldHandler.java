package com.justserver.apocalypse.overworld;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.Item;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;

public class OverworldHandler implements Listener {
    private final Apocalypse plugin;

    public OverworldHandler(Apocalypse apocalypse) {
        this.plugin = apocalypse;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().getInventory().addItem(Registry.MEDKIT.createItemStack(plugin));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        plugin.guiManager.clear(event.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getItem() == null) return;
        if(event.getItem().getItemMeta() == null) return;
        NamespacedKey key = new NamespacedKey(plugin, "APO_ID");
        if(event.getItem().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) return;
        ItemMeta meta = event.getItem().getItemMeta();
        String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        for(Field field : Registry.class.getFields()){
            try {
                Item item = (Item) field.get(Registry.class);
                if(item.getId().equalsIgnoreCase(id)){
                    item.onInteract(event);
                    break;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
