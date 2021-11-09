package com.justserver.apocalypse.items.guns.modifications;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Silencer extends Modify{
    public Silencer(Apocalypse plugin) {
        super(plugin);
        minIronNuggets = 3;
        maxIronNuggets = 6;
    }

    @Override
    public String customName() {
        return "Глушитель";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        super.onInteract(event);
    }

    @Override
    public Material getMaterial() {
        return Material.BUCKET;
    }

    @Override
    public double getLeftDamage() {
        return 0;
    }

    @Override
    public int getSlowdown() {
        return 0;
    }

    

    private final ArrayList<String> guns = new ArrayList<>(Arrays.asList("PISTOL", "SVD", "AK_47", "M4A4"));

    @Override
    public List<String> getForGuns() {
        return guns;
    }
}
