package com.justserver.apocalypse.items.normal;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import com.justserver.apocalypse.tasks.MedkitUseTask;
import org.bukkit.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class Medkit extends Item {

    public Medkit(Apocalypse plugin) {
        super(plugin);
    }

    @Override
    protected void init() {

    }

    @Override
    public String customName() {
        return "Аптечка";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public double getLeftDamage() {
        return 1;
    }

    @Override
    public int getSlowdown() {
        return 0;
    }

    @Override
    public Material getMaterial() {
        return Material.POTION;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public ItemStack createItemStack(Apocalypse plugin, int damage) {
        return this.createItemStack(plugin);
    }

    @Override
    public ItemStack createItemStack(Apocalypse plugin) {
        ItemStack itemStack = super.createItemStack(plugin);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 5), true);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
