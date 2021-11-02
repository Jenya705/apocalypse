package com.justserver.apocalypse.base;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockTypes {
    public static HashMap<Material, Double> blocks = new HashMap<>();
    static {
        blocks.put(Material.OAK_PLANKS, 25.0);
        blocks.put(Material.BRICKS, 50.0);
        blocks.put(Material.OAK_DOOR, 25.0);
    }
    public static ArrayList<Material> interactBlocks = new ArrayList<>();
    static {
        interactBlocks.add(Material.OAK_DOOR);
        interactBlocks.add(Material.DISPENSER);
    }
    public static ArrayList<Material> canPlaceBlock = new ArrayList<>();
    static {
        canPlaceBlock.add(Material.TNT);
    }
}
