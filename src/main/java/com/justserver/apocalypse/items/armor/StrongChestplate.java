package com.justserver.apocalypse.items.armor;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class StrongChestplate extends Armor {
    public StrongChestplate(Apocalypse plugin) {
        super(plugin, 2, 3);
        minIronNuggets = 9;
        maxIronNuggets = 12;
    }

    @Override
    public String customName() {
        return "Прочный бронижилет";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_CHESTPLATE;
    }

    @Override
    public double getLeftDamage() {
        return 0;
    }

    @Override
    public int getSlowdown() {
        return 0;
    }


}
