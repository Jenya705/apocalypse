package com.justserver.apocalypse.dungeons;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.utils.LocationUtil;
import com.sk89q.worldedit.WorldEditException;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

public class DungeonGenerator {
    private final static ArrayList<RoomType> rooms = new ArrayList<>();
    static {
        Collections.addAll(rooms, RoomType.values());
        rooms.remove(RoomType.FIRST);
    }
    public static void generate(Location start, int roomsCount) {
        Dungeon dungeon = new Dungeon((Player) null);
        dungeon.generate(start, roomsCount);
    }

    public static ArrayList<RoomType> getRooms() {
        return rooms;
    }
}
