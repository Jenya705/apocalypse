package com.justserver.apocalypse.tasks;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.normal.Medkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MedkitUseTask extends BukkitRunnable {
    private final Player player;
    private final Apocalypse plugin;
    int counter = 0;

    public MedkitUseTask(Player player, Apocalypse plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if(player == null){
            cancel();
            return;
        }
        if(Registry.getItemByItemstack(player.getInventory().getItemInMainHand()) instanceof Medkit){
            if(counter >= 60){
                double neededHealth = player.getHealth() + 12;
                if (neededHealth > 20.0) neededHealth = 20.0;
                player.setHealth(neededHealth);
                player.sendMessage(ChatColor.GREEN + "Перевязано");
                player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.7f, 0.4f);
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                cancel();
                return;
            }
            counter++;
        } else {
            cancel();
            player.sendMessage(ChatColor.RED + "Вы убрали аптечку из рук");ы
        }

    }
}
