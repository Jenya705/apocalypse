package com.justserver.apocalypse.base.buildings;

import com.justserver.apocalypse.base.BaseHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Campfire extends Building{

    public Campfire(Location center, boolean isPlaced){
        size = "1x1x1";
        needsResources.add(new ItemStack(Material.OAK_PLANKS, 5));
        if(!isPlaced) {
            this.getSize(center);
            summonArmorStands(center);
        }
        this.center = center;
        this.isPlaced = isPlaced;
    }

    public void finish(){
        Location center = this.center;
        center.setY(center.getY() + 1);
        center.getBlock().setType(Material.CAMPFIRE);
    }

}
