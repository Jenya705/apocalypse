package com.justserver.apocalypse.base;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.buildings.Building;
import com.justserver.apocalypse.base.buildings.Campfire;
import com.justserver.apocalypse.gui.BaseGui;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseHandler implements Listener {

    public final Apocalypse plugin;

    public static HashMap<Player, Building> placementArmorStands = new HashMap<>();
    public static HashMap<Player, ArrayList<Location>> placementBuildings = new HashMap<>();

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
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void movePlacementArmorStands(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(placementArmorStands.containsKey(player)){
            Location armorStandsLocation = player.getTargetBlock(null, 5).getLocation().getBlock().getLocation();
            for(Map.Entry<ArmorStand, HashMap<String, Double>> entry : placementArmorStands.get(player).armorStands.entrySet()){
                Building.updateArmorStand(entry.getKey(), armorStandsLocation, entry.getValue());
            }
        }
    }

    @EventHandler
    public void onMove(EntityMoveEvent event){
        if(event.getEntity().getType().equals(EntityType.ARMOR_STAND)){
            for(Map.Entry<Player, Building> entry : placementArmorStands.entrySet()){
                for(Map.Entry<ArmorStand, HashMap<String, Double>> entry2 : placementArmorStands.get(entry.getKey()).armorStands.entrySet()){
                    if(entry2.getKey().equals(event.getEntity())){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void deletePlacementArmorStands(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        if(placementArmorStands.containsKey(player)){
            for(Map.Entry<ArmorStand, HashMap<String, Double>> entry : placementArmorStands.get(player).armorStands.entrySet()){
                entry.getKey().remove();
            }
            placementBuildings.remove(player);
            placementArmorStands.remove(player);
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
                                event.getPlayer().sendMessage(ChatColor.DARK_RED + "Вы не можете ломать блоки на чужой базе");
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}
