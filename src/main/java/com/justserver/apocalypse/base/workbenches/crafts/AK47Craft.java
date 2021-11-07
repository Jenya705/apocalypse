package com.justserver.apocalypse.base.workbenches.crafts;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.base.workbenches.Craft;
import com.justserver.apocalypse.base.workbenches.CraftItem;
import com.justserver.apocalypse.items.BukkitItem;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import com.justserver.apocalypse.items.guns.components.Muzzle;
import com.justserver.apocalypse.items.guns.components.SmallGunBody;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AK47Craft extends Craft {
    public List<CraftItem> needItems = new ArrayList<>(Arrays.asList(
            new CraftItem(new BukkitItem(plugin, Material.IRON_INGOT, 3, ItemRarity.COMMON), 10),
            new CraftItem(new BukkitItem(plugin, Material.FLINT_AND_STEEL, 1, ItemRarity.COMMON), 1),
            new CraftItem(Registry.MUZZLE, 1),
            new CraftItem(Registry.GUN_BODY, 1),
            new CraftItem(Registry.BUTT, 1)
    ));

    @Override
    public List<CraftItem> getNeedItems() {
        return needItems;
    }
    public AK47Craft(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public Item getCraftResult() {
        return Registry.AK_47;
    }
}
