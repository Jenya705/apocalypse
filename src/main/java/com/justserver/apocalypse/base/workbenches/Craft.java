package com.justserver.apocalypse.base.workbenches;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.BukkitItem;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Craft {
    public final Apocalypse plugin;

    public Craft(Apocalypse plugin) {
        this.plugin = plugin;
    }

    abstract public List<CraftItem> getNeedItems();

    public abstract Item getCraftResult();
}
