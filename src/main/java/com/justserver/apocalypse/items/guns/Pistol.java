package com.justserver.apocalypse.items.guns;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Gun;
import com.justserver.apocalypse.items.ItemRarity;

public class Pistol extends Gun {
    public Pistol(Apocalypse plugin) {
        super(plugin, false, 3, 30, 7, 7);
        minIronNuggets = 4;
        maxIronNuggets = 6;
    }

    @Override
    public String customName() {
        return "Пистолет";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public double getLeftDamage() {
        return 0;
    }

    @Override
    public int getSlowdown() {
        return 5;
    }

    @Override
    public int getRechargeTime() {
        return 3;
    }
}
