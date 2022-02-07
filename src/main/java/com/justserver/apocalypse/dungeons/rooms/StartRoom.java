package com.justserver.apocalypse.dungeons.rooms;

import com.justserver.apocalypse.dungeons.DungeonRoom;
import com.justserver.apocalypse.dungeons.RoomType;
import org.bukkit.Location;

/**
 Start room meta info class
 @author MisterFunny01
 */
public class StartRoom extends DungeonRoom {
    private final Location teleportLocation;

    public StartRoom(RoomType roomType, Location location) {
        super(roomType, location);
        if (roomType != RoomType.FIRST) {
            throw new IllegalArgumentException("Room type for start room must be FIRST! Not " + roomType.name());
        }
        this.teleportLocation = location.clone().add(-4.5, 5, 4.5);

    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }
}
