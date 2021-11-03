package com.justserver.apocalypse.items.normal;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class Knife extends Item {
    public Knife(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public String customName() {
        return "Нож";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public double getLeftDamage() {
        return 4;
    }

    @Override
    public int getSlowdown() {
        return 0;
    }

    @Override
    protected void init() {

    }
}
