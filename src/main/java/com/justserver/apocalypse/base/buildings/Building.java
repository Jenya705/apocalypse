package com.justserver.apocalypse.base.buildings;

import com.justserver.apocalypse.base.BaseHandler;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract public class Building {

    public String size;
    public ArrayList<Location> buildingSize = new ArrayList<>();
    public HashMap<ArmorStand, HashMap<String, Double>> armorStands;
    public ArrayList<ItemStack> needsResources = new ArrayList<>();
    public ArrayList<ItemStack> nowResources = new ArrayList<>();
    public boolean isFinished = false;
    public boolean isPlaced = false;

    public Location center;

    public ArrayList<Location> getSize(Location center){
        this.center = center;
        double x, y, z;
        x = center.getX() + Math.floor(Double.parseDouble(size.split("x")[0]) / 2);
        y = center.getY() + 1;
        z = center.getZ() + Math.floor(Double.parseDouble(size.split("x")[2]) / 2);
        Location location1 = new Location(center.getWorld(), x, y, z);
        double x1, y1, z1;
        x1 = center.getX() - Math.floor(Double.parseDouble(size.split("x")[0]) / 2);
        if(Double.parseDouble(size.split("x")[1]) != 1.0) y1 = center.getY() + Double.parseDouble(size.split("x")[1]) + 1;
        else y1 = center.getY() + 1;
        z1 = center.getZ() - Math.floor(Double.parseDouble(size.split("x")[2]) / 2);
        Location location2 = new Location(center.getWorld(), x1, y1, z1);
        buildingSize.clear();
        buildingSize.add(location1);
        buildingSize.add(location2);
        return buildingSize;
    }

    public boolean canFill(){
        Block maxBlock = buildingSize.get(0).getWorld().getBlockAt(buildingSize.get(0));
        Block minBlock = buildingSize.get(0).getWorld().getBlockAt(buildingSize.get(1));
        int minX = minBlock.getX();
        int minY = minBlock.getY();
        int minZ = minBlock.getZ();
        int maxX = maxBlock.getX();
        int maxY = maxBlock.getY();
        int maxZ = maxBlock.getZ();
        for(int x = minX; x <= maxX; x++){
            for(int y = minY; y <= maxY; y++){
                for(int z = minZ; z <= maxZ; z++){
                    if(!buildingSize.get(0).getWorld().getBlockAt(new Location(buildingSize.get(0).getWorld(), x, y, z)).getType().equals(Material.AIR)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public abstract void finish();

    public void fill(){
        Block maxBlock = buildingSize.get(0).getWorld().getBlockAt(buildingSize.get(0));
        Block minBlock = buildingSize.get(0).getWorld().getBlockAt(buildingSize.get(1));
        int minX = minBlock.getX();
        int minY = minBlock.getY();
        int minZ = minBlock.getZ();
        int maxX = maxBlock.getX();
        int maxY = maxBlock.getY();
        int maxZ = maxBlock.getZ();
        for(int x = minX; x <= maxX; x++){
            for(int y = minY; y <= maxY; y++){
                for(int z = minZ; z <= maxZ; z++){
                    buildingSize.get(0).getWorld().getBlockAt(x, y, z).setType(Material.RED_STAINED_GLASS);
                }
            }
        }
    }

    public void summonArmorStands(Location center){
        HashMap<ArmorStand, HashMap<String, Double>> armorStands = new HashMap<>();

        HashMap<String, Double> coords;
        for(int b = 0; b < 2; b++) {
            double x, y, z;
            if(b == 0){
                y = 0;
            }else{
                y = Double.parseDouble(size.split("x")[1]);
            }
            boolean xAlready = true;
            boolean minus = true;
            for (int i = 0; i <= 4; i++) {
                if (i % 2 == 0) {
                    if (minus) {
                        x = -Math.ceil(Double.parseDouble(size.split("x")[0]) / 2);
                        z = -Math.ceil(Double.parseDouble(size.split("x")[2]) / 2);
                        minus = false;
                    } else {
                        x = Math.ceil(Double.parseDouble(size.split("x")[0]) / 2);
                        z = Math.ceil(Double.parseDouble(size.split("x")[2]) / 2);
                    }

                } else {
                    if (xAlready) {
                        x = Math.ceil(Double.parseDouble(size.split("x")[0]) / 2);
                        z = -Math.ceil(Double.parseDouble(size.split("x")[2]) / 2);
                        xAlready = false;
                    } else {
                        x = -Math.ceil(Double.parseDouble(size.split("x")[0]) / 2);
                        z = Math.ceil(Double.parseDouble(size.split("x")[2]) / 2);
                    }

                }

                coords = new HashMap<>();
                coords.put("x", x);
                coords.put("y", y);
                coords.put("z", z);
                armorStands.put(Building.spawnArmorStand(center, coords), coords);
            }
        }
        this.armorStands = armorStands;
    }

    public static ArmorStand spawnArmorStand(Location armorStandsLocation, HashMap<String, Double> coords){
        ArmorStand armorStand = (ArmorStand) armorStandsLocation.getWorld().spawnEntity(new Location(armorStandsLocation.getWorld(), armorStandsLocation.getX() + coords.get("x") + 0.5, armorStandsLocation.getY() + coords.get("y"), armorStandsLocation.getZ() +coords.get("z") + 0.5), EntityType.ARMOR_STAND);
        armorStand.getEquipment().setHelmet(new ItemStack(Material.BRICKS));
        armorStand.setInvisible(true);
        return armorStand;
    }

    public static ArmorStand updateArmorStand(ArmorStand armorStand, Location armorStandsLocation, HashMap<String, Double> coords){
        Location location = new Location(armorStandsLocation.getWorld(), armorStandsLocation.getX() + coords.get("x") + 0.5, armorStandsLocation.getY() + coords.get("y"), armorStandsLocation.getZ() + coords.get("z") + 0.5);
        armorStand.teleport(location);
        return armorStand;
    }
}
