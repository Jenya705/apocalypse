package com.justserver.apocalypse.base.buildings;

import com.justserver.apocalypse.base.BaseHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

abstract public class Building {

    public String size;
    public ArrayList<Location> buildingSize;
    public HashMap<ArmorStand, HashMap<String, Double>> armorStands;
    public ArrayList<Location> getSize(Location center){
        Double x, y, z;
        x = center.getX() + Math.floor(Double.parseDouble(size.split("x")[0]) / 2);
        y = center.getY() + Math.floor(Double.parseDouble(size.split("x")[1]) / 2);
        z = center.getZ() + Math.floor(Double.parseDouble(size.split("x")[2]) / 2);
        Location location1 = new Location(center.getWorld(), x, y, z);
        x = center.getX() - Math.floor(Double.parseDouble(size.split("x")[0]) / 2);
        y = center.getY() - Math.floor(Double.parseDouble(size.split("x")[1]) / 2);
        z = center.getZ() - Math.floor(Double.parseDouble(size.split("x")[2]) / 2);
        Location location2 = new Location(center.getWorld(), x, y, z);
        buildingSize = new ArrayList<>();
        buildingSize.add(location1);
        buildingSize.add(location2);
        return buildingSize;
    }

    public void summonArmorStands(Location center){
        HashMap<ArmorStand, HashMap<String, Double>> armorStands = new HashMap<>();

        HashMap<String, Double> coords;

        Double x, y, z;
        for(int i = 1; i <= 4; i++){
            coords = new HashMap<>();
            y = 0.0;
            if(i % 2 == 0) {
                x = center.getX() + Math.floor(Double.parseDouble(size.split("x")[0]) / 2);
                z = center.getZ() + Math.floor(Double.parseDouble(size.split("x")[2]) / 2);
            }else{
                x = center.getX() + Math.floor(Double.parseDouble(size.split("x")[0]) / 2);
                z = center.getZ() + Math.floor(Double.parseDouble(size.split("x")[2]) / 2);
            }

            coords.put("x", x);
            coords.put("y", y);
            coords.put("z", z);
            armorStands.put(Building.spawnArmorStand(center, coords), coords);
        }
        this.armorStands = armorStands;
    }

    public static ArmorStand spawnArmorStand(Location armorStandsLocation, HashMap<String, Double> coords){
        ArmorStand armorStand = (ArmorStand) armorStandsLocation.getWorld().spawnEntity(armorStandsLocation, EntityType.ARMOR_STAND);
        Building.updateArmorStand(armorStand, armorStandsLocation, coords);
        armorStand.setInvisible(true);
        armorStand.setHelmet(new ItemStack(Material.BRICKS));
        return armorStand;
    }

    public static ArmorStand updateArmorStand(ArmorStand armorStand, Location armorStandsLocation, HashMap<String, Double> coords){
        Location location = new Location(armorStandsLocation.getWorld(), armorStandsLocation.getX() + coords.get("x") + 0.5, armorStandsLocation.getY() + coords.get("y"), armorStandsLocation.getZ() + coords.get("z") + 0.5);
        armorStand.teleport(location);
        return armorStand;
    }
}
