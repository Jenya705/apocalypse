package com.justserver.apocalypse.base.workbenches;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.workbenches.crafts.*;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Workbench3 extends Workbench {
    public Workbench3(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public String customName() {
        return "Верстак 3 лвл";
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
        return Material.BLAST_FURNACE;
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
        return 3;
    }

    public List<Craft> crafts = Arrays.asList(
            new TNTCraft(plugin),
            new IronIngotCraft(plugin),
            new ChestCraft(plugin),
            new BricksFromBrickBlock(plugin),
            new PistolCraft(plugin),
            new AnvilCraft(plugin),
            new BulletCraft(plugin),
            new SilencerCraft(plugin),
            new KnifeCraft(plugin),
            new MaceratorCraft(plugin),
            new ParachuteCraft(plugin),
            new ShotgunCraft(plugin),
            new FlyingAxeCraft(plugin),
            new AK47Craft(plugin),
            new SVDCraft(plugin),
            new M4A4Craft(plugin)
    );

    @Override
    public List<Craft> getCrafts() {
        return crafts;
    }
}
