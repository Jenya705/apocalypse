package com.justserver.apocalypse.items.armor;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class Parachute extends Armor{
    public Parachute(Apocalypse plugin) {
        super(plugin, 1, 3);
        minIronNuggets = 3;
        maxIronNuggets = 6;
    }

    @Override
    public String customName() {
        return "Парашют";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_LEGGINGS;
    }

    @Override
    public double getLeftDamage() {
        return 0;
    }

    @Override
    public int getSlowdown() {
        return 0;
    }
}
