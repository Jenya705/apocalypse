package com.justserver.apocalypse.overworld;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.BukkitItem;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;

public enum ChestType {
    HOUSE(Registry.FLYING_AXE, Registry.GRIP, Registry.KNIFE,
            new BukkitItem(Apocalypse.getInstance(), Material.BRICK, -6, ItemRarity.RARE),
            new BukkitItem(Apocalypse.getInstance(), Material.OAK_PLANKS, -4, ItemRarity.COMMON),
            new BukkitItem(Apocalypse.getInstance(), Material.FLINT_AND_STEEL, 1, ItemRarity.UNCOMMON),
            new BukkitItem(Apocalypse.getInstance(), Material.IRON_INGOT, 1, ItemRarity.EPIC),
            new BukkitItem(Apocalypse.getInstance(), Material.COOKED_BEEF, 1, ItemRarity.RARE)
    ),
    FACTORY(Registry.MEDIUM_GUN_BODY,
            Registry.SCOPE,
            Registry.FLISMY_HELMET,
            Registry.FLISMY_CHESTPLATE,
            new BukkitItem(Apocalypse.getInstance(), Material.TNT, -1, ItemRarity.LEGENDARY),
            Registry.FLYING_AXE,
            Registry.FLISMY_HELMET,
            Registry.KNIFE,
            new BukkitItem(Apocalypse.getInstance(), Material.CHEST, 1, ItemRarity.EPIC),
            new BukkitItem(Apocalypse.getInstance(), Material.OAK_DOOR, 1, ItemRarity.RARE),
            new BukkitItem(Apocalypse.getInstance(), Material.BRICK, -12, ItemRarity.RARE),
            new BukkitItem(Apocalypse.getInstance(), Material.OAK_PLANKS, -7, ItemRarity.UNCOMMON),
            Registry.SMALL_GUN_BODY,
            Registry.MUZZLE,
            Registry.BUTT,
            new BukkitItem(Apocalypse.getInstance(), Material.IRON_INGOT, -3, ItemRarity.EPIC),
            new BukkitItem(Apocalypse.getInstance(), Material.GUNPOWDER, -3, ItemRarity.RARE)
    ),
    HOSPITAL(Registry.MEDKIT,Registry.MEDKIT, Registry.MEDKIT),
    MILITARY(Registry.BUTT,
            Registry.GUN_BODY,
            Registry.MUZZLE,
            Registry.MEDIUM_GUN_BODY,
            Registry.BULLETS,
            Registry.SCOPE,
            Registry.GRIP,
            Registry.SILENCER,
            Registry.MEDKIT,
            Registry.STRONG_HELMET,
            Registry.STRONG_CHESTPLATE,
            Registry.RADIO,
            new BukkitItem(Apocalypse.getInstance(), Material.IRON_INGOT, -3, ItemRarity.EPIC)
    ),

    POLICE( Registry.MEDIUM_GUN_BODY,
            Registry.MUZZLE,
            new BukkitItem(Apocalypse.getInstance(), Material.GUNPOWDER, -3, ItemRarity.RARE),
            Registry.SILENCER,
            Registry.FLYING_AXE,
            Registry.THOCK_HELMET,
            Registry.THOCK_CHESTPLATE,
            new BukkitItem(Apocalypse.getInstance(), Material.BRICK, -4, ItemRarity.RARE),
            new BukkitItem(Apocalypse.getInstance(), Material.OAK_PLANKS, -5, ItemRarity.COMMON),
            new BukkitItem(Apocalypse.getInstance(), Material.SHIELD, 1, ItemRarity.LEGENDARY),
            new BukkitItem(Apocalypse.getInstance(), Material.IRON_INGOT, -3, ItemRarity.EPIC),
            Registry.RADIO
        ),
    SHOP(
            new BukkitItem(Apocalypse.getInstance(), Material.CARROT, -3, ItemRarity.COMMON),
            new BukkitItem(Apocalypse.getInstance(), Material.BREAD, -2, ItemRarity.UNCOMMON),
            new BukkitItem(Apocalypse.getInstance(), Material.COOKED_BEEF, -8, ItemRarity.RARE),
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
