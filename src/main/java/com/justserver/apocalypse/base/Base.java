package com.justserver.apocalypse.base;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.buildings.Building;
import com.justserver.apocalypse.gui.BuildingGui;
import com.justserver.apocalypse.utils.CustomConfiguration;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class Base {
    private final Apocalypse plugin;
    public ArrayList<UUID> players = new ArrayList<>();
    public UUID owner;
    public String id;
    public Location location;
    public ArrayList<Building> buildings = new ArrayList<>();

    public Base(Apocalypse plugin){
        this.plugin = plugin;
    }

    public void saveBase(){
        CustomConfiguration config = plugin.bases;
        config.config.set("bases." + this.id + ".id", this.id);
        config.config.set("bases." + this.id + ".owner", this.owner.toString());
        ArrayList<String> stringUUIDS = new ArrayList<>();
        for(UUID uuid : players){
            stringUUIDS.add(uuid.toString());
        }
        config.config.set("bases." + this.id + ".players", stringUUIDS);
        config.config.set("bases." + this.id + ".location", location);
        ArrayList<HashMap<String, Object>> inYmlBuildings = new ArrayList<>();
        for(Building building : buildings){
            HashMap<String, Object> params = new HashMap<>();
            params.put("name", building.getClass().getName());
            params.put("location", building.center);
            params.put("isFinished", building.isFinished);
            params.put("fulled", building.nowResources);
            inYmlBuildings.add(params);
        }
        config.config.set("bases." + this.id + ".buildings", inYmlBuildings);
        config.save();
        plugin.loadedBases.remove(this);
        plugin.loadedBases.add(this);
    }

    public void addPlayer(UUID uuid){
        this.players.add(uuid);
        saveBase();
    }

    public void removePlayer(UUID uuid){
        this.players.remove(uuid);
        saveBase();
    }

    public void setOwner(UUID uuid){
        this.owner = uuid;
        saveBase();
    }

    @SuppressWarnings("unchecked")
    public static Base getBase(Apocalypse plugin, UUID id){
        for(Base base : plugin.loadedBases){
            if(base.id.equals(id.toString())){
                return base;
            }
        }
        return null;
    }

    public static Base createBase(Apocalypse plugin, Player owner){
        Base base = new Base(plugin);
        base.id = UUID.randomUUID().toString();
        base.owner = owner.getUniqueId();
        base.players.add(owner.getUniqueId());
        base.location = owner.getLocation();
        owner.getWorld().getBlockAt(owner.getLocation()).setType(Material.SMITHING_TABLE);
        base.saveBase();
        plugin.loadedBases.add(base);
        plugin.bases.reload();
        return base;
    }

    public static ArrayList<Base> getPlayerBases(Apocalypse plugin, Player player){
        ArrayList<Base> bases = new ArrayList<>();
        for(String baseKey : plugin.bases.config.getConfigurationSection("bases").getKeys(false)){
            String owner = plugin.bases.config.getString("bases." + baseKey + ".owner");
            if(owner.equals(player.getUniqueId().toString())){
                bases.add(getBase(plugin, UUID.fromString(plugin.bases.config.getString("bases." + baseKey + ".id"))));
            }
        }
        return bases;
    }
}
