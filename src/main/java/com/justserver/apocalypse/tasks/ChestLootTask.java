package com.justserver.apocalypse.tasks;

import com.justserver.apocalypse.overworld.OverworldHandler;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class ChestLootTask extends BukkitRunnable {
    private final Chest chest;
    private final OverworldHandler handler;
    private final Runnable whatAfter;
    private int counter = 0;
    public ChestLootTask(Chest chest, OverworldHandler handler, Runnable whatAfter){
        this.chest = chest;
        this.handler = handler;
        this.whatAfter = whatAfter;
    }

    @Override
    public void run() {
        if(counter == 27){
            handler.lootedChests.add(chest.getLocation());
            for(Map.Entry<UUID, ChestLootTask> entry : handler.chestLootTasks.entrySet()){
                if(entry.getValue().equals(this)){
                    handler.chestLootTasks.remove(entry.getKey());
                }
            }
            whatAfter.run();
            cancel();
            return;
        }
        chest.getBlockInventory().setItem(counter, null);
        counter++;
    }
}
