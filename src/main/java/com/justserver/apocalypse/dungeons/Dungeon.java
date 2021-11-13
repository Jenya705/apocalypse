package com.justserver.apocalypse.dungeons;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.dungeons.rooms.StartRoom;
import com.justserver.apocalypse.utils.FileUtils;
import com.justserver.apocalypse.utils.LocationUtil;
import com.sk89q.worldedit.WorldEditException;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

public class Dungeon implements Listener {
    private final ArrayList<DungeonRoom> rooms = new ArrayList<>();
    private StartRoom startRoom;
    private final World world;
    private final ArrayList<Player> players = new ArrayList<>();
    private final HashMap<UUID, Location> playerToStartLocation = new HashMap<>();


    public Dungeon(Player... players) {
        Collections.addAll(this.players, players);
        WorldCreator creator = new WorldCreator("dungeon_" + System.currentTimeMillis());
        creator.type(WorldType.FLAT);
        creator.generateStructures(false);
        creator.hardcore(false);
        creator.generator(new DungeonChunkGenerator());
        this.world = creator.createWorld();
        if (world == null) {
            announceMessage("Cannot generate dungeon");
            return;
        }
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

        generate(new Location(world, 0, 30, 0), 15);
        this.players.forEach(player -> {
            playerToStartLocation.put(player.getUniqueId(), player.getLocation().clone());
            player.teleport(startRoom.getTeleportLocation());
        });
        Bukkit.getPluginManager().registerEvents(this, Apocalypse.getInstance());
    }

    public void announceMessage(String message) {
        players.forEach(player -> player.sendMessage(message));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().equalsIgnoreCase("end")) {
            Bukkit.getScheduler().runTask(Apocalypse.getInstance(), this::endDungeon);

        }
    }

    public void endDungeon() {
        for (Map.Entry<UUID, Location> entry : playerToStartLocation.entrySet()) {
            if (Bukkit.getPlayer(entry.getKey()) != null) {
                Bukkit.getPlayer(entry.getKey()).teleport(entry.getValue());
            }
        }
        HandlerList.unregisterAll(this);
        Bukkit.unloadWorld(world, false);
        Bukkit.getScheduler().runTaskAsynchronously(Apocalypse.getInstance(), () -> FileUtils.deleteWorld(new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + File.separator + world.getName())));
    }

    private final static SecureRandom random = new SecureRandom();

    public void generate(Location start, int roomsCount) {
        Location starterLocation = start.clone();
        try {
            BlockFace currentBlockFace = BlockFace.NORTH;
            RoomType currentRoom = RoomType.FIRST;
            startRoom = (StartRoom) currentRoom.paste(starterLocation, currentBlockFace);
            currentBlockFace = BlockFace.SOUTH;
            for (int i = 0; i < roomsCount; i++) {
                Doorway doorway;
                if (currentRoom.getOutDoorways().length != 1) {
                    doorway = currentRoom.getOutDoorways()[random.nextInt(currentRoom.getOutDoorways().length)];
                } else {
                    doorway = currentRoom.getOutDoorways()[0];
                }
                RoomType randomRoom = DungeonGenerator.getRooms().get(random.nextInt(DungeonGenerator.getRooms().size()));

                double placementY = starterLocation.getBlockY() - (randomRoom.getInDoorway().getBlockY() - doorway.getBlockY());
                Location nextRoom = LocationUtil.addInDirection(starterLocation.clone(), currentBlockFace, doorway.getBlockZ());
                currentBlockFace = LocationUtil.getRelative(doorway.getRelativeDirection(), currentBlockFace);
                nextRoom = LocationUtil.addInHorizontalRel(nextRoom, LocationUtil.getOpposite(currentBlockFace), randomRoom.getInDoorway().getBlockX() - doorway.getBlockX());
                nextRoom.setY(placementY);
                DungeonRoom room = randomRoom.paste(nextRoom, currentBlockFace);
                rooms.add(room);
                currentRoom = randomRoom;
                starterLocation = nextRoom;
            }
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }


}
