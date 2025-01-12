package com.justserver.apocalypse.base;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockTypes {
    public static HashMap<Material, Double> blocks = new HashMap<>();
    public static ArrayList<Material> interactBlocks = new ArrayList<>();
    public static ArrayList<Material> canPlaceBlock = new ArrayList<>();
    public static ArrayList<Material> canBreakBlocksOnBase = new ArrayList<>();

    static {
        // BLOCKS
        blocks.put(Material.OAK_PLANKS, 25.0);
        blocks.put(Material.BRICKS, 50.0);
        blocks.put(Material.OAK_DOOR, 25.0);
        blocks.put(Material.OBSIDIAN, 10.5);
        blocks.put(Material.STRING, 0.1);
        // Interact blocks
        interactBlocks.add(Material.OAK_DOOR);
        interactBlocks.add(Material.DISPENSER);
        interactBlocks.add(Material.STRING);

        // can place blocks
        canPlaceBlock.add(Material.TNT);
        canPlaceBlock.add(Material.SNOW_BLOCK);
        canPlaceBlock.add(Material.SNOW);
        canPlaceBlock.add(Material.WHITE_WOOL);
        canPlaceBlock.add(Material.CAMPFIRE);
        canPlaceBlock.add(Material.ANVIL);
        canPlaceBlock.add(Material.OBSIDIAN);
        canPlaceBlock.add(Material.STRING);
        canPlaceBlock.add(Material.ICE);

        canBreakBlocksOnBase.add(Material.DISPENSER);
        canBreakBlocksOnBase.add(Material.SMOKER);
        canBreakBlocksOnBase.add(Material.FURNACE);
        canBreakBlocksOnBase.add(Material.BLAST_FURNACE);
        canBreakBlocksOnBase.add(Material.OAK_PLANKS);
        canBreakBlocksOnBase.add(Material.OAK_DOOR);
        canBreakBlocksOnBase.add(Material.BRICKS);
        canBreakBlocksOnBase.add(Material.ANVIL);
        canBreakBlocksOnBase.add(Material.TNT);
        canBreakBlocksOnBase.add(Material.SNOW_BLOCK);
        canBreakBlocksOnBase.add(Material.WHITE_WOOL);
        canBreakBlocksOnBase.add(Material.SNOW);
        canBreakBlocksOnBase.add(Material.CAMPFIRE);
        canBreakBlocksOnBase.add(Material.STRING);
    }
}
