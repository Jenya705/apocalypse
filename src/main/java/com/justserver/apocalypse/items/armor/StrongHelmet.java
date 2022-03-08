package com.justserver.apocalypse.items.armor;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class StrongHelmet extends Armor implements Helmet {
    public StrongHelmet(Apocalypse plugin) {
        super(plugin, 2, 2);
        minIronNuggets = 7;
        maxIronNuggets = 10;
    }

    @Override
    public String customName() {
        return "Прочная каска";
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
        return Material.DIAMOND_HELMET;
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
        return 1.0;
    }
}
