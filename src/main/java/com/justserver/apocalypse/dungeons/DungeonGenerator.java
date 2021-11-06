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
    private final static SecureRandom random = new SecureRandom();
    private final static ArrayList<RoomType> rooms = new ArrayList<>();
    static {
        Collections.addAll(rooms, RoomType.values());
        rooms.remove(RoomType.FIRST);
    }
    public static void generate(Location start, int roomsCount) {
//        Location starterLocation = start.clone();
//        try {
//            BlockFace currentBlockFace = BlockFace.NORTH;
//            RoomType currentRoom = RoomType.FIRST;
//            currentRoom.paste(starterLocation, currentBlockFace);
//            currentBlockFace = BlockFace.SOUTH;
//            for (int i = 0; i < roomsCount; i++) {
//                Doorway doorway;
//                if (currentRoom.getOutDoorways().length != 1) {
//                     doorway = currentRoom.getOutDoorways()[random.nextInt(currentRoom.getOutDoorways().length)];
//                } else {
//                    doorway = currentRoom.getOutDoorways()[0];
//                }
//                RoomType randomRoom = rooms.get(random.nextInt(rooms.size()));
//
//                double placementY = starterLocation.getBlockY() - (randomRoom.getInDoorway().getBlockY() - doorway.getBlockY());
//                Location nextRoom = LocationUtil.addInDirection(starterLocation.clone(), currentBlockFace, doorway.getBlockZ());
//                currentBlockFace = LocationUtil.getRelative(doorway.getRelativeDirection(), currentBlockFace);
//                nextRoom = LocationUtil.addInHorizontalRel(nextRoom, LocationUtil.getOpposite(currentBlockFace), randomRoom.getInDoorway().getBlockX() - doorway.getBlockX());
//                nextRoom.setY(placementY);
//                randomRoom.paste(nextRoom, currentBlockFace);
//                currentRoom = randomRoom;
//                starterLocation = nextRoom;
//            }
//        } catch (IOException | WorldEditException e) {
//            e.printStackTrace();
//        }
        Dungeon dungeon = new Dungeon(null);
        dungeon.generate(start, roomsCount);
    }

    public static ArrayList<RoomType> getRooms() {
        return rooms;
    }
}
