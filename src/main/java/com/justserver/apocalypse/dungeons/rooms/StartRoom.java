package com.justserver.apocalypse.dungeons.rooms;

import com.justserver.apocalypse.dungeons.DungeonRoom;
import com.justserver.apocalypse.dungeons.RoomType;
import org.bukkit.Location;

public class StartRoom extends DungeonRoom {
    private final Location teleportLocation;

    public StartRoom(RoomType roomType, Location location) {
        super(roomType, location);
        if (roomType != RoomType.FIRST) {
            throw new IllegalArgumentException("Room type for start room must be FIRST! Not " + roomType.name());
        }
        this.teleportLocation = location.clone().add(roomType.getOutDoorways()[0]).add(-3.5, 0, -2);

    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }
}
