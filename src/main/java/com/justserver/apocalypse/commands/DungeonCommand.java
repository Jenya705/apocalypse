package com.justserver.apocalypse.commands;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.dungeons.Dungeon;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record DungeonCommand(Apocalypse plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ChatColor.RED + "У вас недостаточно прав, чтобы тестировать данжи :( Подождите немного, и данжи выйдут в открытый доступ");
        if (sender instanceof Player && sender.isOp()) {
            new Dungeon((Player) sender);
        }
        return true;
    }
}

