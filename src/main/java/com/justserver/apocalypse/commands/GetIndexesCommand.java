package com.justserver.apocalypse.commands;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.overworld.OverworldHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GetIndexesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender.isOp()){
            for(Map.Entry<UUID, Set<Item>> entry : OverworldHandler.indexedItems.entrySet()){
                commandSender.sendMessage(entry.getKey() + ": ");
                for(Item item : entry.getValue()){
                    commandSender.sendMessage("   " + item.customName());
                }
                commandSender.sendMessage(" ");
            }
        }
        return true;
    }
}
