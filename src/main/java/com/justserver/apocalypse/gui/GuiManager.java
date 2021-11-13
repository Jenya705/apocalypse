package com.justserver.apocalypse.gui;

import com.justserver.apocalypse.overworld.OverworldHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GuiManager implements Listener {
     public HashMap<Player, Gui> playerToGuiMap = new HashMap<>();

     public Gui getOpenGui(Player player){
          return playerToGuiMap.get(player);
     }

     public void setGui(Player player, Gui gui){
          clear(player);

          playerToGuiMap.put(player, gui);
          if(gui == null || gui.inventory == null){
               player.closeInventory();
               return;
          }
          player.openInventory(gui.inventory);

     }

     public void clear(Player player){
          playerToGuiMap.remove(player);
     }

     @EventHandler
     public void onClick(InventoryClickEvent event){
          if(event.getCurrentItem() != null){
               if(OverworldHandler.chestLootTasks.containsKey(event.getWhoClicked().getUniqueId())){
                    event.setCancelled(true);
                    return;
               }
          }
          if(playerToGuiMap.get((Player)event.getWhoClicked()) != null){
               event.setCancelled(true);
          } else return;
          //Bukkit.getLogger().info(Objects.equals(event.getClickedInventory(),
          if(!Objects.equals(event.getClickedInventory(), event.getWhoClicked().getInventory())){
               //System.out.println("123");
               for(Map.Entry<Player, Gui> entry : playerToGuiMap.entrySet()){
                    if(entry.getValue().inventory.equals(event.getClickedInventory())){
                         try {
                              Gui nextGui = entry.getValue().handleClick(event, entry.getKey(), event.getCurrentItem(), event.getWhoClicked().getOpenInventory(), event.getClick());
                              if(nextGui instanceof ReturnGui) return;
                              if(nextGui == null) event.getWhoClicked().closeInventory();
                         } catch (NoSuchFieldException e) {
                              e.printStackTrace();
                         } catch (IllegalAccessException e) {
                              e.printStackTrace();
                         }
                         break;
                    }
               }
          } else {
               for(Map.Entry<Player, Gui> entry : playerToGuiMap.entrySet()){
                    if(entry.getKey().getInventory().equals(event.getClickedInventory())){
                         entry.getValue().handleInventoryClick(event, entry.getKey(), event.getCurrentItem(), event.getClick());
                         break;
                    }
               }
          }

     }

     @EventHandler
     public void onClose(InventoryCloseEvent event){
          for(Map.Entry<Player, Gui> entry : playerToGuiMap.entrySet()){
              // System.out.println(entry);
               if(entry.getValue().inventory.equals(event.getInventory())){
                    entry.getValue().onClose(event);
                    break;
               }
          }
          clear((Player) event.getPlayer());
     }
}
