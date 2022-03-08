package com.justserver.apocalypse.base.workbenches.crafts;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.base.workbenches.Craft;
import com.justserver.apocalypse.base.workbenches.CraftItem;
import com.justserver.apocalypse.items.BukkitItem;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;

import java.util.List;

public class MaceratorCraft extends Craft {
    public MaceratorCraft(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public List<CraftItem> getNeedItems() {
        return List.of(new CraftItem(new BukkitItem(plugin, Material.IRON_INGOT, 10, ItemRarity.COMMON), 10));
    }

    @Override
    public Item getCraftResult() {
        return Registry.MACERATOR;
    }
}
