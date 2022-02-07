package com.justserver.apocalypse.dungeons;

import org.bukkit.Location;

/**
 Room class that stores meta information about room
 @author MisterFunny01
 */
public class DungeonRoom {
    public DungeonRoom(RoomType roomType, Location location) {
        this.roomType = roomType;
    }

    private final RoomType roomType;
    public DungeonRoom parent = null;

    public RoomType getRoomType() {
        return roomType;
    }
}
