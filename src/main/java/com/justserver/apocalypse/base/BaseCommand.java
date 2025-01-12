package com.justserver.apocalypse.base;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.tasks.CombatCooldownTask;
import org.bukkit.Bukkit;
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

import java.util.*;

public record BaseCommand(Apocalypse plugin) implements CommandExecutor, TabCompleter {

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {

            if (player.getWorld().getName().contains("dungeon")) return true;
            if(CombatCooldownTask.tasks.containsKey(player)){
                player.sendMessage(ChatColor.RED + "Вы не можете использовать эту команду в PvP");
                return true;
            }
            if (label.equals("base") && args.length > 0) {
                if (args[0].equals("create")) {
                    List<Base> playersBases = Base.getBasesByPlayer(plugin, player);
                    if (playersBases.size() + 1 > 1) {
                        player.sendMessage(ChatColor.DARK_RED + "У вас уже есть база");
                        if (args.length > 1) {
                            StringBuilder bases = new StringBuilder(ChatColor.AQUA + "");
                            int counter = 1;
                            for (Base playersBase : playersBases) {
                                bases.append("База #").append(counter).append(": X: ").append(playersBase.location.getBlockX()).append(" Y: ").append(playersBase.location.getBlockY()).append(" Z: ").append(playersBase.location.getBlockZ()).append("\n");
                                counter++;
                            }
                            player.sendMessage(bases.toString());
                        }

                        return true;
                    }
                    double lowestDistance = 31;
                    for (Base base : plugin.loadedBases) {
                        if (base.location.distance(player.getLocation()) < lowestDistance) {
                            player.sendMessage(ChatColor.DARK_RED + "Вы не можете поставить тут базу! Дистанция: " + Math.round(base.location.distance(player.getLocation())));
                            return true;
                        }
                    }
                    int sides = 2;
                    for(int x = -1; x < sides; x++){
                        for(int y = -1; y < sides; y++) {
                            for (int z = -1; z < sides; z++) {
                                if(player.getLocation().clone().add(x, y, z).getBlock().getType().equals(Material.CHEST)){
                                    player.sendMessage(ChatColor.RED + "Вы слишком близко к сундуку");
                                    return true;
                                }
                            }
                        }
                    }
                    Base.createBase(plugin, player);
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
                    if (playerToAdd == null) {
                        player.sendMessage(ChatColor.DARK_RED + "Игрок оффлайн");
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
                    UUID playerToRemove = playerToRemoveRaw.getUniqueId();
                    if (base.owner.equals(playerToRemove)) {
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
                } else if (args.length == 1 && args[0].equals("delete")) {
                    Base base = Base.getBaseByBlock(plugin, player.getLocation().getBlock());
                    System.out.println(base);
                    if (base == null) {
                        player.sendMessage(ChatColor.DARK_RED + "Вы не находитесь на територии базы!");
                        return true;
                    }
                    if (base.owner.equals(player.getUniqueId())) {
                        Base.delete(base);
                    } else {
                        player.sendMessage(ChatColor.DARK_RED + "Вы не владелец базы!");
                    }
                } else if (args.length == 1 && args[0].equals("list")) {
                    Base base = Base.getBaseByBlock(plugin, player.getLocation().getBlock());
                    if (base == null) {
                        player.sendMessage(ChatColor.RED + "Вы находитесь не на территории базы");
                        return true;
                    }
                    if (!base.owner.equals(player.getUniqueId())) {
                        player.sendMessage(ChatColor.RED + "Вы не владелец");
                        return true;
                    }
                    StringBuilder players = new StringBuilder();
                    for (UUID basePlayer : base.players) {
                        players.append(ChatColor.AQUA).append(Bukkit.getOfflinePlayer(basePlayer).getName()).append("\n");
                    }
                    player.sendMessage(players.toString());
                } else if (args.length == 1 && args[0].equals("dummy") && player.isOp()) {
                    Base.createBase(plugin, player);
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable
    List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("create", "add", "remove", "delete");
        } else if (args.length == 2 && (args[0].equals("add") || args[0].equals("remove"))) {
            return null;
        }
        return Collections.emptyList();
    }
}
