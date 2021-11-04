package com.justserver.apocalypse.dungeons;

import com.sk89q.worldedit.WorldEditException;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;

public class Dungeon {
    private final ArrayList<RoomType> rooms = new ArrayList<>();
    private final World world;

    public Dungeon(Player forPlayer){
        WorldCreator creator = new WorldCreator("dungeon_" + System.currentTimeMillis());
        creator.type(WorldType.FLAT);
        creator.generateStructures(false);
        creator.hardcore(false);
        this.world = creator.createWorld();
        if(world == null){
            forPlayer.sendMessage("Cannot generate dungeon");
            return;
        }

        try {
            RoomType.FIRST.paste(new Location(world, 0.0, 10.0, 0.0));
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
//        ctx.setActor(BukkitAdapter.adapt(Bukkit.getConsoleSender()));
    }

    public void clear(){


    }

    public World getWorld() {
        return world;
    }
}
