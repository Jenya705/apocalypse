package com.justserver.apocalypse.base.workbenches.crafts;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.base.workbenches.Craft;
import com.justserver.apocalypse.base.workbenches.CraftItem;
import com.justserver.apocalypse.items.BukkitItem;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PistolCraft extends Craft {

    public List<CraftItem> needItems = new ArrayList<>(Arrays.asList(new CraftItem(new BukkitItem(plugin, Material.IRON_INGOT, 3, ItemRarity.COMMON), 1)));

    public PistolCraft(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public List<CraftItem> needItems() {
        return needItems;
    }

    @Override
    public Item getCraftResult() throws NoSuchFieldException, IllegalAccessException {
        return (Item) Registry.class.getDeclaredField("PISTOL").get(Registry.class);
    }
}
