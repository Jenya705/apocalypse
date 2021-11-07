package com.justserver.apocalypse.items.guns;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Gun;
import com.justserver.apocalypse.items.ItemRarity;

public class AK_47 extends Gun {
    public AK_47(Apocalypse plugin) {
        super(plugin, false, 4, 70, 0, 30);
    }

    @Override
    public int getRechargeTime() {
        return 1;
    }

    @Override
    public String customName() {
        return "AK-47";
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
