package com.justserver.apocalypse.dungeons;

import com.sk89q.worldedit.WorldEditException;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
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
<<<<<<< HEAD
            RoomType.FIRST.paste(new Location(world, 0.0, 10.0, 0.0), BlockFace.EAST);
=======
            RoomType.FIRST.paste(new Location(world, 0.0, 10.0, 0.0), BlockFace.NORTH);
>>>>>>> 41c117812613da8abfa821c68519c9fe7a6824ba
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
