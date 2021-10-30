package com.justserver.apocalypse.items.normal;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import com.justserver.apocalypse.tasks.MedkitUseTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class Medkit extends Item {

    public Medkit(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    protected void init() {

    }

    @Override
    public String customName() {
        return "Аптечка";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public double getLeftDamage() {
        return 1;
    }

    @Override
    public int getSlowdown() {
        return 0;
    }

    @Override
    public Material getMaterial() {
        return Material.POTION;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction().name().contains("RIGHT_CLICK")){
            event.getPlayer().sendMessage(ChatColor.GREEN + "Перевязываем... Не меняйте слот в течении 3 секунд");
            new MedkitUseTask(event.getPlayer(), plugin).runTaskTimer(plugin, 0, 1);
        }
    }

}
