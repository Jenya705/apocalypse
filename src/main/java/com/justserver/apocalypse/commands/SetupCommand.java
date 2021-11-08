package com.justserver.apocalypse.commands;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender.isOp()){
            if(args[0].equalsIgnoreCase("start")){
                Apocalypse.initSetup();
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(player.isOp()){
                        Apocalypse.getSetup().enterSetup(player);
                    } else {
                        player.kickPlayer("Setup");
                        Bukkit.setWhitelist(true);
                    }
                }
            } else {
                Bukkit.setWhitelist(false);
                Apocalypse.getInstance().initEvents(false);
            }
        }
        return true;
    }
}
