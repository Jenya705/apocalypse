package com.justserver.apocalypse.base;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BaseCommand implements CommandExecutor {

    private final Apocalypse plugin;

    public BaseCommand(Apocalypse plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(label.equals("base") && args.length > 0){
                if(args.length == 1 && args[0].equals("create")){
                    Base base = Base.createBase(plugin, player);
                    return true;
                }else if(args.length == 1 && args[0].equals("my")){
                    ArrayList<Base> bases = Base.getPlayerBases(plugin, player);
                    for(Base base : bases){
                        player.sendMessage(ChatColor.AQUA + "x: " + Math.round(base.location.getX()) + " y: " + Math.round(base.location.getY()) + " z: " + Math.round(base.location.getZ()));
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
