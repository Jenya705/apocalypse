package com.justserver.apocalypse.items.normal;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class Macerator extends Item {
    public Macerator(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public String customName() {
        return "Переработчик";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        event.setCancelled(false);
    }

    @Override
    public Material getMaterial() {
        return Material.OBSIDIAN;
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
