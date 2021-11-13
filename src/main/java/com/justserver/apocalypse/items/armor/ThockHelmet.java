package com.justserver.apocalypse.items.armor;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class ThockHelmet extends Armor implements Helmet {
    public ThockHelmet(Apocalypse plugin) {
        super(plugin, 2, 1);
        minIronNuggets = 4;
        maxIronNuggets = 4;
    }

    @Override
    public String customName() {
        return "Средняя каска";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
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
        return 0;
    }


    @Override
    public double getHeadshotModifier() {
        return 1.5;
    }
}
