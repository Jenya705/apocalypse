package com.justserver.apocalypse.overworld;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.Gun;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.normal.Medkit;
import com.justserver.apocalypse.overworld.chests.Chest;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
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
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class OverworldHandler implements Listener {
    private final Apocalypse plugin;
    private final ArrayList<Chest> lootedChests = new ArrayList<>();

    public OverworldHandler(Apocalypse apocalypse) {
        this.plugin = apocalypse;
        Bukkit.getScheduler().runTaskTimer(apocalypse, lootedChests::clear, 0, 20 * 60 * 5);
    }

//    @EventHandler
//    public void onJoin(PlayerJoinEvent event){
//        event.getPlayer().getInventory().addItem(Registry.FLYING_AXE.createItemStack(plugin));
//    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        plugin.guiManager.clear(event.getPlayer());
        if(Registry.FLYING_AXE.getThrownAxes().containsKey(event.getPlayer().getUniqueId())){
            event.getPlayer().getInventory().addItem(Registry.FLYING_AXE.getThrownAxes().get(event.getPlayer().getUniqueId()));
            Registry.FLYING_AXE.removePlayer(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getClickedBlock() != null){
            if(event.getClickedBlock().getType().equals(Material.CHEST)){
                org.bukkit.block.Chest chest = (org.bukkit.block.Chest) event.getClickedBlock().getState();
                if(chest.getPersistentDataContainer().has(new NamespacedKey(plugin, "chest_type"), PersistentDataType.STRING)){
                    //lootedChests.add()
                }
                return;
            }
        }
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
            if(possibleItem.getLeftDamage() != 0){
                event.setDamage(possibleItem.getLeftDamage() / ((Player) event.getDamager()).getAttackCooldown());
            }
        }
    }

    @EventHandler
    public void onChangeEquipment(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(event.getNewSlot());
        if(itemStack == null){
            player.setWalkSpeed(0.2f);
            return;
        }
        itemStack = (itemStack.getType().equals(Material.AIR) ? null : itemStack);
        if(itemStack == null) {
            player.setWalkSpeed(0.2f);
            return;
        }
        Item possibleItem = Registry.getItemByItemstack(itemStack);
        if(possibleItem == null) {
            player.setWalkSpeed(0.2f);
            return;
        }
        player.setWalkSpeed(0.2f - (0.2f * (possibleItem.getSlowdown() / 100f)));
    }

    @EventHandler
    public void onCrossbowRecharge(EntityLoadCrossbowEvent event){
        if(event.getEntity() instanceof Player){
            ItemStack itemStack = ((Player) event.getEntity()).getInventory().getItemInMainHand();
            itemStack = (itemStack.getType().equals(Material.AIR) ? null : itemStack);
            if(itemStack == null) return;
            Item possibleItem = Registry.getItemByItemstack(itemStack);
            if(possibleItem == null) return;
            if(possibleItem instanceof Gun){
                ItemMeta meta = itemStack.getItemMeta();
                PersistentDataContainer data = meta.getPersistentDataContainer();
                int currentAmmo = data.get(new NamespacedKey(plugin, "ammo_count"), PersistentDataType.INTEGER);
                if(currentAmmo != 0) {event.setCancelled(true); return;}
                data.set(new NamespacedKey(plugin, "ammo_count"), PersistentDataType.INTEGER, ((Gun) possibleItem).getInitialAmmoCount());
                itemStack.setItemMeta(meta);
            }
        }
    }

    @EventHandler
    public void onChestOpen()
}
