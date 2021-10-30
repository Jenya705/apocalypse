package com.justserver.apocalypse.commands;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.dungeons.dungs.GeneratedDungeon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DungeonCommand implements CommandExecutor {

    public final Apocalypse plugin;


    public DungeonCommand(Apocalypse plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(plugin.inDungeon.containsKey(player)) {
                player.sendMessage(ChatColor.RED + "Вы уже на миссии!");
            }else{
                GeneratedDungeon generatedDungeon = plugin.superDungeon.generateDungeon(UUID.randomUUID().toString());
                System.out.println(generatedDungeon);
                generatedDungeon.players.addAll(Bukkit.getOnlinePlayers());
                generatedDungeon.teleportPlayers();
                for(Player playerInTeam : Bukkit.getServer().getOnlinePlayers()){
                    plugin.inDungeon.put(playerInTeam, generatedDungeon);
                }
            }
        }
        return true;
    }
}

