package com.justserver.apocalypse.items;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BukkitItem extends Item{
    private final int count;
    private final Material material;
    private final ItemRarity rarity;

    public BukkitItem(Apocalypse plugin, Material material, int count, ItemRarity rarity) {

        super(plugin);
        this.material = material;
        this.count = count;
        this.rarity = rarity;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String customName() {
        return null;
    }

    @Override
    public ItemRarity getRarity() {
        return this.rarity;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public Material getMaterial() {
        return material;
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
    protected void init() {

    }

    @Override
    public ItemStack createItemStack(Apocalypse plugin) {
        return new ItemStack(material, count);
    }

    @Override
    public ItemStack createItemStack(Apocalypse plugin, int damage) {
        return this.createItemStack(plugin);
    }
}
