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
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

import static ru.epserv.epmodule.util.StyleUtils.red;
import static ru.epserv.epmodule.util.StyleUtils.regular;

/**
 Main dungeon class that handles all that happening in the dungeon
 @author MisterFunny01
 */
public class Dungeon implements Listener {
    private final ArrayList<DungeonRoom> rooms = new ArrayList<>();
    private StartRoom startRoom;
    private World world;
    private final ArrayList<Player> players = new ArrayList<>();
    private final HashMap<UUID, Location> playerToStartLocation = new HashMap<>();
    long start = System.currentTimeMillis();
    long totalStart;
    private final String levelName;
    private volatile boolean isGenerated = false;

    public Dungeon(Player... players) {
        Collections.addAll(this.players, players);
        String name = "dungeon_" + System.currentTimeMillis();
        this.levelName = name;
        totalStart = start;
        try {
            DungeonGenerator.generate(name, () -> {
                System.out.println(name);
                System.out.println("Took time to copy dungeon: " + (System.currentTimeMillis() - start));
                start = System.currentTimeMillis();
                WorldCreator creator = new WorldCreator(name);
                this.world = DungeonGenerator.getDungeonServer().createWorld(creator);
                System.out.println("Took time to load dungeon: " + (System.currentTimeMillis() - start));
                if (world == null) {
                    announceMessage("Cannot generate dungeon");
                    return;
                }
                world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                start = System.currentTimeMillis();
                generate(new Location(world, 0, 30, 0), 5, () -> {
                    this.players.forEach(player -> {
                        playerToStartLocation.put(player.getUniqueId(), player.getLocation().clone());
                        player.teleport(startRoom.getTeleportLocation());
                    });
                    Bukkit.getPluginManager().registerEvents(this, Apocalypse.getInstance());
                });
                System.out.println("Took time to generate dungeon: " + (System.currentTimeMillis() - start));
                System.out.println("In total: " + (System.currentTimeMillis() - totalStart));
                isGenerated = true;
            }, () -> {
                for (Player player : players) {
                    player.sendMessage(regular(red("Произошла ошибка генерации. ID данжа: " + name)));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            for (Player player : players) {
                player.sendMessage(regular(red("Произошла ошибка генерации. ID данжа: " + name)));
            }
        }

    }

    public boolean isGenerated() {
        return isGenerated;
    }

    public void announceMessage(String message) {
        players.forEach(player -> player.sendMessage(message));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().equalsIgnoreCase("end")) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(Apocalypse.getInstance(), this::endDungeon);
        }
    }

    private volatile boolean unloaded = false;

    public void endDungeon() {
        for (Map.Entry<UUID, Location> entry : playerToStartLocation.entrySet()) {
            if (Bukkit.getPlayer(entry.getKey()) != null) {
                Bukkit.getPlayer(entry.getKey()).teleport(entry.getValue());
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(Apocalypse.getInstance(), () -> {
            while (!isGenerated) Thread.onSpinWait();
            Bukkit.getScheduler().runTask(Apocalypse.getInstance(), () -> {
                HandlerList.unregisterAll(this);
                if(world == null){
                    Bukkit.unloadWorld(levelName, false);
                } else Bukkit.unloadWorld(world, false);
                unloaded = true;
            });
            while (!unloaded) Thread.onSpinWait();
            FileUtils.deleteWorld(new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + File.separator + levelName));
        });
    }

    private final static SecureRandom random = new SecureRandom();

    public void generate(Location start, int roomsCount, Runnable afterStart) {
        Location starterLocation = start.clone();
        try {
            BlockFace currentBlockFace = BlockFace.NORTH;
            RoomType currentRoom = RoomType.FIRST;
            startRoom = (StartRoom) currentRoom.paste(starterLocation, currentBlockFace);
            afterStart.run();
            DungeonRoom currentRoomWrapper = startRoom;
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
                if(room == null) continue;
                room.parent = currentRoomWrapper;
                currentRoomWrapper = room;
                rooms.add(room);
                currentRoom = randomRoom;
                starterLocation = nextRoom;
            }
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }


}
