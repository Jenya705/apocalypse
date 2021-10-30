package com.justserver.apocalypse;

import com.justserver.apocalypse.base.Base;
import com.justserver.apocalypse.base.BaseCommand;
import com.justserver.apocalypse.base.BaseHandler;
import com.justserver.apocalypse.commands.DungeonCommand;
import com.justserver.apocalypse.commands.DungeonCommand;
import com.justserver.apocalypse.dungeons.DungeonHandler;
import com.justserver.apocalypse.dungeons.dungs.GeneratedDungeon;
import com.justserver.apocalypse.dungeons.dungs.SuperDungeon;
import com.justserver.apocalypse.gui.GuiManager;
import com.justserver.apocalypse.overworld.OverworldHandler;
import com.justserver.apocalypse.utils.CustomConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Apocalypse extends JavaPlugin {

    public SuperDungeon superDungeon = new SuperDungeon(this);
    public HashMap<Player, GeneratedDungeon> inDungeon = new HashMap<>();
    public CustomConfiguration bases = new CustomConfiguration(this, "bases.yml");
    public GuiManager guiManager = new GuiManager();
    public ArrayList<Base> loadedBases = new ArrayList<>();

    @Override
    public void onEnable() {
        getCommand("startdungeon").setExecutor(new DungeonCommand(this));
        getCommand("base").setExecutor(new BaseCommand(this));
        Bukkit.getPluginManager().registerEvents(new DungeonHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new BaseHandler(this), this);
        Bukkit.getPluginManager().registerEvents(guiManager, this);
        Bukkit.getPluginManager().registerEvents(new OverworldHandler(this), this);
        for(String key : bases.config.getConfigurationSection("bases").getKeys(false)){
            Base base = new Base(this);
            base.id = bases.config.getString("bases." + key + ".id");
            base.owner = UUID.fromString(bases.config.getString("bases." + key + ".owner"));
            List<String> stringUUIDs = bases.config.getStringList("bases." + key + ".players");
            for(String stringUUID : stringUUIDs){
                base.players.add(UUID.fromString(stringUUID));
            }
            base.location = bases.config.getLocation("bases." + key + ".location");
            loadedBases.add(base);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
