package com.justserver.apocalypse.base.workbenches;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;

import java.util.List;

public abstract class Craft {
    public final Apocalypse plugin;

    public Craft(Apocalypse plugin) {
        this.plugin = plugin;
    }

    abstract public List<CraftItem> getNeedItems();

    public abstract Item getCraftResult();
}
