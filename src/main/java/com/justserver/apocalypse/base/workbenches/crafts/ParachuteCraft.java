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

public class ParachuteCraft extends Craft {
    public ParachuteCraft(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public List<CraftItem> getNeedItems() {
        return List.of(
                new CraftItem(new BukkitItem(Apocalypse.getInstance(), Material.IRON_NUGGET, 1, ItemRarity.COMMON), 2),
                new CraftItem(new BukkitItem(Apocalypse.getInstance(), Material.STRING, 1, ItemRarity.COMMON), 5),
                new CraftItem(new BukkitItem(Apocalypse.getInstance(), Material.WHITE_WOOL, 1, ItemRarity.COMMON), 7)
        );
    }

    @Override
    public Item getCraftResult() {
        return Registry.PARACHUTE;
    }
}
