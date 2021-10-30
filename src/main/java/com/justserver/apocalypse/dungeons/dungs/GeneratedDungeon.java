package com.justserver.apocalypse.dungeons.dungs;

import com.justserver.apocalypse.dungeons.FilesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;

public class GeneratedDungeon {
    public Dungeon dungeon;
    public World world;
    public ArrayList<Player> players = new ArrayList<>();

    public void teleportPlayers(){
        for(Player player : players){
            Location loc = dungeon.spawn;
            loc.setWorld(world);
            player.teleport(loc);
        }
    }

    public GeneratedDungeon(World world, Dungeon dungeon){
        this.world = world;
        this.dungeon = dungeon;
    }

    public void finishDungeon(Boolean win){
        for(Chunk c : this.world.getLoadedChunks()){
            c.unload(false);
        }
        if(win) dungeon.finish(players.get(0));
        Bukkit.getServer().unloadWorld(this.world.getName(), false);
        FilesUtils.deleteWorld(new File(this.world.getName()));
    }
}
