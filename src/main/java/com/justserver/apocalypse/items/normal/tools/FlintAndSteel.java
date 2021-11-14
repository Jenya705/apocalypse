package com.justserver.apocalypse.items.normal.tools;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.Material;

public class FlintAndSteel extends Tool{
    public FlintAndSteel(Apocalypse plugin) {
        super(plugin);
        minIronNuggets = 1;
        maxIronNuggets = 2;
    }

    @Override
    public String customName() {
        return "Зажигалка";
    }

    @Override
    public Material getMaterial() {
        return Material.FLINT_AND_STEEL;
    }
}
