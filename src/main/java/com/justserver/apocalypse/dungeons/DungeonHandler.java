package com.justserver.apocalypse.dungeons;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.Base;
import com.justserver.apocalypse.dungeons.dungs.GeneratedDungeon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Map;

public class DungeonHandler implements Listener {
    public final Apocalypse plugin;

    public DungeonHandler(Apocalypse plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        if(plugin.inDungeon.containsKey(player)){
            GeneratedDungeon generatedDungeon = plugin.inDungeon.get(player);
            for(Map.Entry<Player, GeneratedDungeon> entry : plugin.inDungeon.entrySet()){
                if(entry.getValue().equals(generatedDungeon)){
                    plugin.inDungeon.remove(entry.getKey());
                }
            }
            if(!plugin.inDungeon.containsValue(generatedDungeon)){
                generatedDungeon.finishDungeon(false);
            }
        }
    }

    @EventHandler
    public void onFinish(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(e.getClickedBlock().getType().equals(Material.STONE_BUTTON)){
                Player player = e.getPlayer();
                if(plugin.inDungeon.containsKey(player)){
                    System.out.println(plugin.inDungeon);
                    GeneratedDungeon generatedDungeon = plugin.inDungeon.get(player);
                    for(Player playerInDungeon : generatedDungeon.players){
                        playerInDungeon.teleport(new Location(Bukkit.getWorld("world"), 205, 63, 249));
                    }
                    for(Map.Entry<Player, GeneratedDungeon> entry : plugin.inDungeon.entrySet()){
                        if(entry.getValue().equals(generatedDungeon)){
                            plugin.inDungeon.remove(entry.getKey());
                        }
                    }
                    generatedDungeon.finishDungeon(true);
                }
            }
        }
    }
}
