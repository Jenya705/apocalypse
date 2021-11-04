package com.justserver.apocalypse.base.workbenches;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;

import java.util.List;

public abstract class Craft {
    public final Apocalypse plugin;

    protected Craft(Apocalypse plugin) {
        this.plugin = plugin;
    }

    public abstract List<CraftItem> needItems();
    public abstract Item getCraftResult() throws NoSuchFieldException, IllegalAccessException;
}
