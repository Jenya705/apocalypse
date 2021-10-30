package com.justserver.apocalypse.base;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.gui.BaseGui;
import com.justserver.apocalypse.gui.Gui;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class BaseHandler implements Listener {

    public final Apocalypse plugin;

    public static HashMap<Player, HashMap<Location, ArmorStand>> placementArmorStands = new HashMap<>();

    public BaseHandler(Apocalypse plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void viewBase(PlayerInteractEvent event){
        if(event.getClickedBlock() == null) return;
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().equals(Material.SMITHING_TABLE)){
            Player player = event.getPlayer();
            for(Base base : plugin.loadedBases) {
                if(base.location.getBlock().getLocation().equals(event.getClickedBlock().getLocation()) && base.players.contains(event.getPlayer().getUniqueId())){
                    plugin.guiManager.setGui(player, new BaseGui());
                    System.out.println(base);
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void baseRegion(BlockBreakEvent event){
        for(Base base : plugin.loadedBases) {
            int minX = base.location.getBlockX() - 25;
            int minY = base.location.getBlockY() - 25;
            int minZ = base.location.getBlockZ() - 25;
            int maxX = base.location.getBlockX() + 25;
            int maxY = base.location.getBlockY() + 25;
            int maxZ = base.location.getBlockZ() + 25;
            Location blockLocation = event.getBlock().getLocation().clone();
            for(int x = minX; x <= maxX; x++){
                for(int y = minY; y <= maxY; y++){
                    for(int z = minZ; z <= maxZ; z++){
                        if(blockLocation.equals(new Location(event.getBlock().getWorld(), x, y, z))){
                            if(!base.players.contains(event.getPlayer().getUniqueId())) {
                                event.setCancelled(true);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}
