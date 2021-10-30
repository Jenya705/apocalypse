package com.justserver.apocalypse.items.normal;

import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class Medkit extends Item {

    @Override
    protected void init() {

    }

    @Override
    public String customName() {
        return null;
    }

    @Override
    public ItemRarity getRarity() {
        return null;
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
    public Material getMaterial() {
        return Material.POTION;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }
}
