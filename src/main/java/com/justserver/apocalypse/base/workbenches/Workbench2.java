package com.justserver.apocalypse.base.workbenches;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.workbenches.crafts.*;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Workbench2 extends Workbench{
    public Workbench2(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public String customName() {
        return "Верстак 2 лвл";
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
        return Material.SMOKER;
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
    public Integer getLevel() {
        return 2;
    }

    public List<Craft> crafts = new ArrayList<>(Arrays.asList(
            new ShotgunCraft(plugin),
            new FlyingAxeCraft(plugin),
            new Workbench3Craft(plugin)
    ));

    @Override
    public List<Craft> getCrafts() {
        return crafts;
    }
}
