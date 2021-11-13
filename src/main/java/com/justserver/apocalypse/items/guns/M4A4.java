package com.justserver.apocalypse.items.guns;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Gun;
import com.justserver.apocalypse.items.ItemRarity;

public class M4A4 extends Gun {
    public M4A4(Apocalypse plugin) {
        super(plugin, false, 3.5, 80, 0, 30);
    }

    @Override
    public int getRechargeTime() {
        return 0;
    }

    @Override
    public String customName() {
        return "M4A4";
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
        return 20;
    }


}
