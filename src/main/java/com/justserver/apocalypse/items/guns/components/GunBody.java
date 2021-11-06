package com.justserver.apocalypse.items.guns.components;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class GunBody extends Component{
    public GunBody(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public String customName() {
        return "Корпус оружия";
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
        return Material.DIAMOND_HORSE_ARMOR;
    }

    @Override
    public double getLeftDamage() {
        return 0;
    }

    @Override
    public int getSlowdown() {
        return 0;
    }

    @Override
    protected void init() {

    }
}
