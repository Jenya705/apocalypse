package com.justserver.apocalypse.items.normal.tools;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class Pickaxe extends Tool{
    public Pickaxe(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public String customName() {
        return "Кирка";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_PICKAXE;
    }
}
