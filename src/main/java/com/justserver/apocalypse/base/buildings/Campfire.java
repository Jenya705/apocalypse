package com.justserver.apocalypse.base.buildings;

import com.justserver.apocalypse.base.BaseHandler;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Campfire extends Building{
    public Campfire(Location center, Player player){
        summonArmorStands(center);
        size = "1x1x1";
        this.getSize(center);

        BaseHandler.placementBuildings.put(player, this.buildingSize);
        BaseHandler.placementArmorStands.put(player, this);
    }


}
