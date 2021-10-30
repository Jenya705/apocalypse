package com.justserver.apocalypse.items;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public interface IItem {
    String customName();
    ItemRarity getRarity();
    void onInteract(PlayerInteractEvent event);
    Material getMaterial();
    double getLeftDamage();
    int getSlowdown();
}
