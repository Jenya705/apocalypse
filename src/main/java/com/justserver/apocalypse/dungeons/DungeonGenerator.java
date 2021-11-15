package com.justserver.apocalypse.dungeons;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

public class DungeonGenerator {

    private static DungeonServer dungeonServer;

    static {
        try {
            dungeonServer = new DungeonServer();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void generate(String name, Runnable callback, Runnable error) throws IOException {
        File container = Bukkit.getWorldContainer();
        String containerPath = container.getCanonicalPath();
        Bukkit.getScheduler().runTaskAsynchronously(Apocalypse.getInstance(), () -> {
            try {
                System.out.println(Thread.currentThread().getName());
                File worldFile = new File(containerPath + File.separator + name);
                FileUtils.copyDirectory(new File(containerPath + File.separator + "dungeon"), worldFile);
                Bukkit.getScheduler().runTask(Apocalypse.getInstance(), callback);
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getScheduler().runTask(Apocalypse.getInstance(), error);
            }
        });
    }

    private final static ArrayList<RoomType> rooms = new ArrayList<>();
    static {
        Collections.addAll(rooms, RoomType.values());
        rooms.remove(RoomType.FIRST);
    }
    public static ArrayList<RoomType> getRooms() {
        return rooms;
    }

    public static DungeonServer getDungeonServer() {
        return dungeonServer;
    }
}
