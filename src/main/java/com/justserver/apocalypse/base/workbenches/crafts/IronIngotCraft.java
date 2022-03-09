package com.justserver.apocalypse.base.workbenches.crafts;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.workbenches.Craft;
import com.justserver.apocalypse.base.workbenches.CraftItem;
import com.justserver.apocalypse.items.BukkitItem;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;

import java.util.List;

public class IronIngotCraft extends Craft {
    public IronIngotCraft(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public List<CraftItem> getNeedItems() {
        return List.of(new CraftItem(new BukkitItem(plugin, Material.IRON_NUGGET, 1, ItemRarity.COMMON), 12));
    }

    @Override
    public Item getCraftResult() {
        return new BukkitItem(plugin, Material.IRON_INGOT, 1, ItemRarity.COMMON);
    }
}
