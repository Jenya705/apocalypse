package com.justserver.apocalypse.items;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public interface IItem {
    String customName();

    ItemRarity getRarity();

    void onInteract(PlayerInteractEvent event);

    Material getMaterial();

    double getLeftDamage();

    int getSlowdown();
}
