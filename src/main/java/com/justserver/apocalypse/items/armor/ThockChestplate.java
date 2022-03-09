package com.justserver.apocalypse.items.armor;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class ThockChestplate extends Armor {

    public ThockChestplate(Apocalypse plugin) {
        super(plugin, 3, 2);
        minIronNuggets = 5;
        maxIronNuggets = 6;
    }

    @Override
    public String customName() {
        return "Средний бронижилет";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public Material getMaterial() {
        return Material.IRON_CHESTPLATE;
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
