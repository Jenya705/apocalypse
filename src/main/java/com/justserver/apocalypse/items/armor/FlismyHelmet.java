package com.justserver.apocalypse.items.armor;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class FlismyHelmet extends Armor implements Helmet {
    public FlismyHelmet(Apocalypse plugin) {
        super(plugin, 0, 0);
        minIronNuggets = 1;
        maxIronNuggets = 3;
    }

    @Override
    public String customName() {
        return "Хлипкая Каска";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public Material getMaterial() {
        return Material.IRON_HELMET;
    }

    @Override
    public double getLeftDamage() {
        return 0;
    }

    @Override
    public int getSlowdown() {
        return 2;
    }


    @Override
    public double getHeadshotModifier() {
        return 2.5;
    }
}
