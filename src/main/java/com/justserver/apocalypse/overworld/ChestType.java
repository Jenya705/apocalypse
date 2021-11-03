package com.justserver.apocalypse.overworld;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.BukkitItem;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;

public enum ChestType {
    HOUSE(Registry.FLYING_AXE, Registry.PISTOL, Registry.GRIP, Registry.KNIFE,
            new BukkitItem(Apocalypse.getInstance(), Material.BRICK, -6, ItemRarity.RARE),
            new BukkitItem(Apocalypse.getInstance(), Material.OAK_PLANKS, -4, ItemRarity.COMMON),
            new BukkitItem(Apocalypse.getInstance(), Material.FLINT_AND_STEEL, 1, ItemRarity.UNCOMMON)
    ),
    FACTORY(Registry.BULLETS,
            Registry.SCOPE,
            Registry.FLISMY_HELMET,
            Registry.FLISMY_CHESTPLATE,
            new BukkitItem(Apocalypse.getInstance(), Material.TNT, -1, ItemRarity.LEGENDARY),
            Registry.PISTOL,
            Registry.FLYING_AXE,
            Registry.FLISMY_HELMET,
            Registry.KNIFE,
            new BukkitItem(Apocalypse.getInstance(), Material.CHEST, 1, ItemRarity.EPIC),
            new BukkitItem(Apocalypse.getInstance(), Material.OAK_DOOR, 1, ItemRarity.RARE),
            new BukkitItem(Apocalypse.getInstance(), Material.BRICK, -12, ItemRarity.RARE),
            new BukkitItem(Apocalypse.getInstance(), Material.OAK_PLANKS, -7, ItemRarity.UNCOMMON)
    ),
    HOSPITAL(Registry.MEDKIT,Registry.MEDKIT, Registry.MEDKIT,
            Registry.BULLETS),
    MILITARY(Registry.AK_47,
            Registry.SVD,
            Registry.M4A4,
            Registry.SHOTGUN,
            Registry.BULLETS,
            Registry.SCOPE,
            Registry.GRIP,
            Registry.SILENCER,
            Registry.MEDKIT,
            Registry.STRONG_HELMET,
            Registry.STRONG_CHESTPLATE
    ),

    POLICE( Registry.PISTOL,
            Registry.BULLETS,
            Registry.SHOTGUN,
            Registry.SILENCER,
            Registry.FLYING_AXE,
            Registry.THOCK_HELMET,
            Registry.THOCK_CHESTPLATE,
            new BukkitItem(Apocalypse.getInstance(), Material.BRICK, -4, ItemRarity.RARE),
            new BukkitItem(Apocalypse.getInstance(), Material.OAK_PLANKS, -5, ItemRarity.COMMON),
            new BukkitItem(Apocalypse.getInstance(), Material.SHIELD, 1, ItemRarity.LEGENDARY)
        ),
    SHOP(
            new BukkitItem(Apocalypse.getInstance(), Material.CARROT, -3, ItemRarity.COMMON),
            new BukkitItem(Apocalypse.getInstance(), Material.BREAD, -2, ItemRarity.UNCOMMON),
            new BukkitItem(Apocalypse.getInstance(), Material.COOKED_BEEF, -2, ItemRarity.RARE),
            new BukkitItem(Apocalypse.getInstance(), Material.COOKED_MUTTON, -2, ItemRarity.RARE),
            new BukkitItem(Apocalypse.getInstance(), Material.CAMPFIRE, -2, ItemRarity.RARE),
            new BukkitItem(Apocalypse.getInstance(), Material.FLINT_AND_STEEL, 1, ItemRarity.UNCOMMON)
    );

    private final Item[] whatSpawns;
    ChestType(Item... whatSpawns){
        this.whatSpawns = whatSpawns;
    }

    public Item[] getWhatSpawns() {
        return whatSpawns;
    }
}
