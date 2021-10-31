package com.justserver.apocalypse.base;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.buildings.Building;
import com.justserver.apocalypse.base.buildings.Campfire;
import com.justserver.apocalypse.gui.BaseGui;
import com.justserver.apocalypse.gui.BuildingGui;
import com.justserver.apocalypse.gui.GuiManager;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseHandler implements Listener {

    public final Apocalypse plugin;

    public static HashMap<Player, Building> placementBuilding = new HashMap<>();
    public static HashMap<Player, Base> placementBuildingBases = new HashMap<>();

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
                    plugin.guiManager.setGui(player, new BaseGui(base));
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void movePlacementArmorStands(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(placementBuilding.containsKey(player)){
            Location armorStandsLocation = player.getTargetBlock(null, 5).getLocation().getBlock().getLocation();
            ItemStack armorStandBlock = new ItemStack(Material.RED_STAINED_GLASS);
            if(placementBuilding.get(player).canFill()) armorStandBlock = new ItemStack(Material.GREEN_STAINED_GLASS);
            for(Map.Entry<ArmorStand, HashMap<String, Double>> entry : placementBuilding.get(player).armorStands.entrySet()){
                Building.updateArmorStand(entry.getKey(), armorStandsLocation, entry.getValue());
                placementBuilding.get(player).getSize(armorStandsLocation);
                entry.getKey().getEquipment().setHelmet(armorStandBlock);
            }
        }
    }

    @EventHandler
    public void onMove(EntityMoveEvent event){
        if(event.getEntity().getType().equals(EntityType.ARMOR_STAND)){
            for(Map.Entry<Player, Building> entry : placementBuilding.entrySet()){
                for(Map.Entry<ArmorStand, HashMap<String, Double>> entry2 : placementBuilding.get(entry.getKey()).armorStands.entrySet()){
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
        if(placementBuilding.containsKey(player)){
            for(Map.Entry<ArmorStand, HashMap<String, Double>> entry : placementBuilding.get(player).armorStands.entrySet()){
                entry.getKey().remove();
            }
            placementBuilding.remove(player);
            BaseHandler.placementBuildingBases.remove(player);
        }
    }

    @EventHandler
    public void createBuilding(PlayerInteractEvent event){
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand().equals(EquipmentSlot.HAND)){
            if(placementBuilding.containsKey(event.getPlayer())){
                Player player = event.getPlayer();
                Building building = placementBuilding.get(player);
                building.getSize(player.getTargetBlock(5).getLocation());
                if(!building.canFill()) {
                    player.sendMessage(ChatColor.DARK_RED + "Вы не можете поставить здесь постройку!");
                    return;
                }
                building.fill();
                for(Map.Entry<ArmorStand, HashMap<String, Double>> entry : placementBuilding.get(player).armorStands.entrySet()){
                    entry.getKey().remove();
                }
                Base base = placementBuildingBases.get(player);
                base.buildings.add(building);
                base.saveBase();
                placementBuilding.remove(player);
                BaseHandler.placementBuildingBases.remove(player);
            }
        }
    }

    @EventHandler
    public void addResourceToBuilding(PlayerInteractEvent  event){
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand().equals(EquipmentSlot.HAND) && event.getClickedBlock().getType().equals(Material.RED_STAINED_GLASS)){
            Player player = event.getPlayer();
            Location center = null;
            Base base = null;
            Building building = null;
            for(Base basefor : plugin.loadedBases){
                for(Building buildingfor : basefor.buildings){
                    Block maxBlock = buildingfor.buildingSize.get(0).getWorld().getBlockAt(buildingfor.buildingSize.get(0));
                    Block minBlock = buildingfor.buildingSize.get(0).getWorld().getBlockAt(buildingfor.buildingSize.get(1));
                    int minX = minBlock.getX();
                    int minY = minBlock.getY();
                    int minZ = minBlock.getZ();
                    int maxX = maxBlock.getX();
                    int maxY = maxBlock.getY();
                    int maxZ = maxBlock.getZ();
                    for(int x = minX; x <= maxX; x++){
                        for(int y = minY; y <= maxY; y++){
                            for(int z = minZ; z <= maxZ; z++){
                                if(buildingfor.buildingSize.get(0).getWorld().getBlockAt(new Location(buildingfor.buildingSize.get(0).getWorld(), x, y, z)).equals(event.getClickedBlock())){
                                    base = basefor;
                                    center = buildingfor.center;
                                    building = buildingfor;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if(base == null) return;
            plugin.guiManager.setGui(player, new BuildingGui(base, building));
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
