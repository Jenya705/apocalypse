package com.justserver.apocalypse.commands;

import com.justserver.apocalypse.Apocalypse;

import com.justserver.apocalypse.dungeons.Dungeon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;



public class DungeonCommand implements CommandExecutor {

    public final Apocalypse plugin;


    public DungeonCommand(Apocalypse plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            new Dungeon((Player) sender);
        }
        return true;
    }
}

