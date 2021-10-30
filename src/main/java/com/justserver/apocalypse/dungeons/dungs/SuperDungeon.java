package com.justserver.apocalypse.dungeons.dungs;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SuperDungeon extends Dungeon{
    public SuperDungeon(Apocalypse plugin){
        super(plugin);
        name = "SuperDungeon";
        spawn = new Location(null, 153, 76, 249);
    }
    public void finish(Player player) {
        player.getInventory().addItem(new ItemStack(Material.DIAMOND));
    }

}
