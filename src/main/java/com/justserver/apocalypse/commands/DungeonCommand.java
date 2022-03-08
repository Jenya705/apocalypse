package com.justserver.apocalypse.commands;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.dungeons.Dungeon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record DungeonCommand(Apocalypse plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player && sender.getName().equals("MisterFunny01")) { // only I have rights to test dungeons :p
            new Dungeon((Player) sender);
        } else {
            Component component = Component.text()
                    .color(TextColor.color(255, 55, 55))
                    .content("В разработке. Нажмите по сообщению, чтобы узнать больше про данжи")
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://discord.com/channels/615616371597770805/905537003775492156/942532845405093961"))
                    .build();
            sender.sendMessage(component);
        }
        return true;
    }
}

