package com.justserver.apocalypse.base.workbenches.crafts;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.base.workbenches.Craft;
import com.justserver.apocalypse.base.workbenches.CraftItem;
import com.justserver.apocalypse.base.workbenches.Workbench1;
import com.justserver.apocalypse.items.BukkitItem;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import com.justserver.apocalypse.items.guns.components.Muzzle;
import com.justserver.apocalypse.items.guns.components.SmallGunBody;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Workbench1Craft extends Craft {
    public Workbench1Craft(Apocalypse plugin) {
        super(plugin);
    }

    public List<CraftItem> needItems = new ArrayList<>(Arrays.asList(
            new CraftItem(new BukkitItem(plugin, Material.IRON_INGOT, 3, ItemRarity.COMMON), 5),
            new CraftItem(new BukkitItem(plugin, Material.OAK_PLANKS, 3, ItemRarity.COMMON), 15)
    ));

    @Override
    public List<CraftItem> getNeedItems() {
        return needItems;
    }

    @Override
    public Item getCraftResult() {
        return Registry.WORKBENCH_1;
    }
}
