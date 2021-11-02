package com.justserver.apocalypse.overworld;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.BukkitItem;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;

public class PoliceChest extends Chest{
    public PoliceChest(Apocalypse plugin){
        super(plugin,
                Registry.PISTOL,
                Registry.BULLETS,
                Registry.SHOTGUN,
                Registry.GRIP,
                Registry.SCOPE,
                Registry.SILENCER,
                new BukkitItem(plugin, Material.BRICKS, -5, ItemRarity.RARE),
                new BukkitItem(plugin, Material.BRICK, -2, ItemRarity.RARE),
                new BukkitItem(plugin, Material.OAK_PLANKS, -5, ItemRarity.COMMON)
        );
    }
}
