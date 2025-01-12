package com.justserver.apocalypse.base.workbenches;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.workbenches.crafts.*;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

public class Workbench1 extends Workbench {
    public Workbench1(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public String customName() {
        return "Верстак 1 лвл";
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
        return Material.FURNACE;
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
        return 1;
    }

    public List<Craft> crafts = Arrays.asList(
            new TNTCraft(plugin),
            new BricksFromBrickBlock(plugin),
            new IronIngotCraft(plugin),
            new ChestCraft(plugin),
            new PistolCraft(plugin),
            new BulletCraft(plugin),
            new SilencerCraft(plugin),
            new KnifeCraft(plugin),
            new MaceratorCraft(plugin),
            new Workbench2Craft(plugin)
    );


    @Override
    public List<Craft> getCrafts() {
        return crafts;
    }


}
