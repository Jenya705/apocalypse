package com.justserver.apocalypse.dungeons;

import org.bukkit.Location;

public class DungeonRoom {
    public DungeonRoom(RoomType roomType, Location location) {
        this.roomType = roomType;
    }

    private final RoomType roomType;


    public RoomType getRoomType() {
        return roomType;
    }
}
