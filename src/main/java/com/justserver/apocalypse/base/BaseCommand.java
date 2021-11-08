package com.justserver.apocalypse.base;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record BaseCommand(Apocalypse plugin) implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {

            if (player.getWorld().getName().contains("dungeon")) return true;
            if (label.equals("base") && args.length > 0) {
                if (args.length == 1 && args[0].equals("create")) {
                    if (Base.getBasesByPlayer(plugin, player).size() + 1 > 1) {
                        player.sendMessage(ChatColor.DARK_RED + "У вас уже есть база");
                        return true;
                    }
                    double lowestDistance = 31; // ты кодишь?
                    for (Base base : plugin.loadedBases) {
                        if (base.location.distance(player.getLocation()) < lowestDistance) {
                            player.sendMessage(ChatColor.DARK_RED + "Вы не можете поставить тут базу!");
                            return true;
                        }
                    }
                    Base base = Base.createBase(plugin, player);
                    return true;
                } else if (args.length == 2 && args[0].equals("add")) {
                    Base base = Base.getBaseByBlock(plugin, player.getLocation().getBlock());
                    if (base == null) {
                        player.sendMessage(ChatColor.DARK_RED + "Вы не находитесь на теретории базы!");
                        return true;
                    }
                    if (!base.owner.equals(player.getUniqueId())) {
                        player.sendMessage(ChatColor.DARK_RED + "Вы не владелец базы!");
                        return true;
                    }
                    if (plugin.getServer().getPlayer(args[1]) == null) {
                        player.sendMessage(ChatColor.DARK_RED + "Игрок не найден!");
                        return true;
                    }
                    Player playerToAdd = plugin.getServer().getPlayer(args[1]);
                    if (Base.getBasesByPlayer(plugin, playerToAdd).size() + 1 > 1) {
                        player.sendMessage(ChatColor.DARK_RED + "У игрока уже есть база");
                        return true;
                    }
                    if (base.players.contains(playerToAdd.getUniqueId())) {
                        player.sendMessage(ChatColor.DARK_RED + "Игрок уже находится на вашей базе!");
                        return true;
                    }
                    base.addPlayer(playerToAdd.getUniqueId());
                    player.sendMessage(ChatColor.GREEN + "Игрок " + playerToAdd.getName() + " успешно добавлен в вашу базу");
                    return true;
                } else if (args.length == 2 && args[0].equals("remove")) {
                    Base base = Base.getBaseByBlock(plugin, player.getLocation().getBlock());
                    if (base == null) {
                        player.sendMessage(ChatColor.DARK_RED + "Вы не находитесь на територии базы!");
                        return true;
                    }
                    if (!base.owner.equals(player.getUniqueId())) {
                        player.sendMessage(ChatColor.DARK_RED + "Вы не владелец базы!");
                        return true;
                    }
                    OfflinePlayer playerToRemoveRaw = plugin.getServer().getOfflinePlayer(args[1]);
                    if (playerToRemoveRaw == null) {
                        player.sendMessage(ChatColor.DARK_RED + "Игрок не найден!");
                        return true;
                    }
                    UUID playerToRemove = playerToRemoveRaw.getUniqueId();
                    if(base.owner.equals(playerToRemove)){
                        player.sendMessage(ChatColor.DARK_RED + "Вы не можете удалить себя из базы");
                        return true;
                    }
                    if (!base.players.contains(playerToRemove)) {
                        player.sendMessage(ChatColor.DARK_RED + "Игрока нет в вашей базе!");
                        return true;
                    }
                    base.removePlayer(playerToRemove);
                    player.sendMessage(ChatColor.GREEN + "Игрок " + args[1] + " успешно удален из вашей базы");
                    return true;
                } else if(args.length == 1 && args[0].equals("delete")){
                    Base base = Base.getBaseByBlock(plugin, player.getLocation().getBlock());
                    if (base == null) {
                        player.sendMessage(ChatColor.DARK_RED + "Вы не находитесь на територии базы!");
                        return true;
                    }
                    if(base.owner.equals(player.getUniqueId())){
                        ArrayList<Base> foundBases = new ArrayList<>();
                        plugin.loadedBases.stream().filter(base1 -> base.id.equals(base1.id)).forEach(base1 -> {
                            if(plugin.bases.config.contains("bases." + base1.id)){
                                plugin.bases.config.set("bases." + base1.id, null);
                                plugin.bases.save();
                                plugin.bases.reload();
                                if(!foundBases.contains(base1)) player.sendMessage(ChatColor.RED + "База удалена!");
                                foundBases.add(base1);
                                base1.location.getBlock().setType(Material.AIR);
                            }
                        });
                        plugin.loadedBases.remove(base);
                        for(Base found : foundBases){
                            plugin.loadedBases.remove(found);
                        }
                    } else {
                        player.sendMessage(ChatColor.DARK_RED + "Вы не владелец базы!");
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable
    List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> tabs = new ArrayList<>();
        if (args.length == 1) {
            tabs.add("create");
            tabs.add("add");
            tabs.add("remove");
        } else if (args.length == 2 && (args[0].equals("add") || args[0].equals("remove"))) {
            tabs = null;
        }
        return tabs;
    }
}
