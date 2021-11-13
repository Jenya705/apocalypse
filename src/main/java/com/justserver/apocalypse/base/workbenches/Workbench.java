package com.justserver.apocalypse.base.workbenches;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import org.bukkit.Material;

import java.util.List;

public abstract class Workbench extends Item {
    public final Apocalypse plugin;

    public Workbench(Apocalypse plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public abstract Material getMaterial();

    public abstract Integer getLevel();

    public abstract List<Craft> getCrafts();
}
