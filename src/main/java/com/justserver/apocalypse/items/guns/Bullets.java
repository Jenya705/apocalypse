package com.justserver.apocalypse.items.guns;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class Bullets extends Item {
    public Bullets(Apocalypse plugin){
        super(plugin);
        count = 1;
    }
    @Override
    public String getId() {
        return "BULLETS";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public Material getMaterial() {
        return Material.ARROW;
    }

    @Override
    public int getSlowdown() {
        return 0;
    }

    @Override
    public double getLeftDamage() {
        return 0;
    }

    @Override
    public String customName() {
        return "Боеприпасы";
    }

    @Override
    protected void init() {

    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }
}
