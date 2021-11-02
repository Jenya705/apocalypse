package com.justserver.apocalypse.overworld;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.BukkitItem;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;

public class FactoryChest extends Chest {

    public FactoryChest(Apocalypse plugin) {
        super(plugin,
                Registry.MEDKIT,
                Registry.BULLETS,
                Registry.SILENCER,
                Registry.GRIP,
                Registry.SCOPE,
                new BukkitItem(plugin, Material.TNT, -10, ItemRarity.COMMON),
                Registry.PISTOL
                );
    }
}
