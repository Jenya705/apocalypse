package com.justserver.apocalypse.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static ru.epserv.epmodule.util.StyleUtils.red;

public class CalculateDoorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player){
            try {
                Region selection = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player)).getSelection(BukkitAdapter.adapt(player.getWorld()));
                BlockVector3 min = selection.getMinimumPoint();
                BlockVector3 max = selection.getMaximumPoint();
                player.sendMessage("X: " + (max.getBlockX() - min.getBlockX()) + "; Y: " + (max.getBlockY() - min.getBlockY()) + "; Z: " + (max.getBlockZ() - min.getBlockZ()));
            } catch (IncompleteRegionException e) {
                player.sendMessage(red("Incomplete selection"));
            }
        }
        return true;
    }
}
