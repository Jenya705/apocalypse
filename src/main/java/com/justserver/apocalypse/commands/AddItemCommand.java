package com.justserver.apocalypse.commands;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.Item;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AddItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) return false;
        if (sender instanceof Player && sender.isOp()) {
            try {
                ((Player) sender).getInventory().addItem(((Item) Registry.class.getDeclaredField(args[0]).get(Registry.class)).createItemStack(Apocalypse.getPlugin(Apocalypse.class)));
            } catch (NoSuchFieldException e) {
                sender.sendMessage(ChatColor.RED + "Предмет не найден");
            } catch (IllegalAccessException e) {
                sender.sendMessage("Произошла ошибка. Посмотрите консоль для деталей");
                e.printStackTrace();
            }
        }
        return true;
    }
}
