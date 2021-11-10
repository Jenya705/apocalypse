package com.justserver.apocalypse.base.workbenches.crafts;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.workbenches.Craft;
import com.justserver.apocalypse.base.workbenches.CraftItem;
import com.justserver.apocalypse.items.BukkitItem;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

public class BricksFromBrickBlock extends Craft {
    public BricksFromBrickBlock(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public List<CraftItem> getNeedItems() {
        return Collections.singletonList(new CraftItem(new BukkitItem(plugin, Material.BRICKS, 1, ItemRarity.COMMON), 1));
    }

    @Override
    public Item getCraftResult() {
        return new BukkitItem(plugin, Material.BRICK, 4, ItemRarity.COMMON);
    }
}
