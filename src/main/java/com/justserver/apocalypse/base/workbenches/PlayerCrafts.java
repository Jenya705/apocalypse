package com.justserver.apocalypse.base.workbenches;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.workbenches.crafts.BulletCraft;
import com.justserver.apocalypse.base.workbenches.crafts.PistolCraft;
import com.justserver.apocalypse.base.workbenches.crafts.Workbench1Craft;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerCrafts extends Workbench{
    public PlayerCrafts(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    public String customName() {
        return null;
    }

    @Override
    public ItemRarity getRarity() {
        return null;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public Material getMaterial() {
        return null;
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
        return 0;
    }

    public List<Craft> crafts = new ArrayList<>(Arrays.asList(
            new Workbench1Craft(plugin)
    ));

    @Override
    public List<Craft> getCrafts() {
        return crafts;
    }

    @Override
    protected void init() {

    }
}
