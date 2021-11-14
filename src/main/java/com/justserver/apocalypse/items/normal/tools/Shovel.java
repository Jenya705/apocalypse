package com.justserver.apocalypse.items.normal.tools;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.Material;

public class Shovel extends Tool{
    public Shovel(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public String customName() {
        return "Лопата";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SHOVEL;
    }
}
