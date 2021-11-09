package com.justserver.apocalypse.base;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockTypes {
    public static HashMap<Material, Double> blocks = new HashMap<>();
    public static ArrayList<Material> interactBlocks = new ArrayList<>();
    public static ArrayList<Material> canPlaceBlock = new ArrayList<>();
    static {
        // BLOCKS
        blocks.put(Material.OAK_PLANKS, 25.0);
        blocks.put(Material.BRICKS, 50.0);
        blocks.put(Material.OAK_DOOR, 25.0);
        // Interact blocks
        interactBlocks.add(Material.OAK_DOOR);
        interactBlocks.add(Material.DISPENSER);
        // can place blocks
        canPlaceBlock.add(Material.TNT);
        canPlaceBlock.add(Material.CHEST);
        canPlaceBlock.add(Material.DISPENSER);
    }
}
