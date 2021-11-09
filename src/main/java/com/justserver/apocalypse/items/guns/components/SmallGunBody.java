package com.justserver.apocalypse.items.guns.components;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class SmallGunBody extends Component {

    public SmallGunBody(Apocalypse plugin) {
        super(plugin);
        minIronNuggets = 3;
        maxIronNuggets = 5;
    }

    @Override
    public String customName() {
        return "Корпус питсолета";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER_HORSE_ARMOR;
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
