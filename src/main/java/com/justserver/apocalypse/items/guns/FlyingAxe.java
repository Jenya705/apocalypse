package com.justserver.apocalypse.items.guns;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class FlyingAxe extends Item {
    public FlyingAxe(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public String customName() {
        return "Метательный Топор";
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
        return Material.IRON_AXE;
    }

    @Override
    public double getLeftDamage() {
        return 10;
    }

    @Override
    public int getSlowdown() {
        return 10;
    }

    @Override
    protected void init() {

    }
}
