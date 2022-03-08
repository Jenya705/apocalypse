package com.justserver.apocalypse.items.guns;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Gun;
import com.justserver.apocalypse.items.ItemRarity;

public class SVD extends Gun {
    public SVD(Apocalypse plugin) {
        super(plugin, false, 16, 100, 80, 4);
    }

    @Override
    public int getRechargeTime() {
        return 1;
    }

    @Override
    public String customName() {
        return "СВД";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public double getLeftDamage() {
        return 0;
    }

    @Override
    public int getSlowdown() {
        return 50;
    }


}
