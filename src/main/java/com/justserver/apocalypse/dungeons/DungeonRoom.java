package com.justserver.apocalypse.dungeons;


import com.sk89q.worldedit.WorldEditException;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.io.IOException;

public class DungeonRoom {
    private final RoomType roomType;
    private final int minX;
    private final int minY;
    private final Dungeon dungeon;

    public DungeonRoom(Dungeon dungeon, RoomType roomType, int minX, int minY, int minZ) throws IOException, WorldEditException {
        this.roomType = roomType;
        this.minX = minX;
        this.minY = minY;
        this.dungeon = dungeon;
        roomType.paste(new Location(dungeon.getWorld(), minX, minY, minZ), BlockFace.NORTH);
    }

    public DungeonRoom(DungeonRoom parent, RoomType roomType, int minX, int minY, int minZ) throws IOException, WorldEditException {
        this.roomType = roomType;
        this.dungeon = parent.getDungeon();
        this.minX = minX;
        this.minY = minY;
        roomType.paste(new Location(dungeon.getWorld(), minX, minY, minZ), parent.randomDoorwayBlockface());
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }
    public BlockFace randomDoorwayBlockface(){
        return BlockFace.NORTH;
    }
}
