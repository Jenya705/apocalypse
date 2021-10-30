package com.justserver.apocalypse.dungeons.dungs;

import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.dungeons.FilesUtils;
import com.justserver.apocalypse.Apocalypse;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.*;

public abstract class Dungeon {

    public final Apocalypse plugin;

    public Dungeon(Apocalypse plugin){
        this.plugin = plugin;
    }

    public String name;
    public Location spawn;

    public GeneratedDungeon generateDungeon(String name) {
        File dest = new File(this.name + "_" + name);
        File src = new File(this.name);
        FilesUtils.copyDirectory(src, dest);
        World generatedWorld = plugin.getServer().createWorld(new WorldCreator(dest.getName()));

        return new GeneratedDungeon(generatedWorld, this);
    }

    public abstract void finish(Player player);

}
