package com.justserver.apocalypse.dungeons;

import org.bukkit.Location;
import org.bukkit.entity.Player;

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
