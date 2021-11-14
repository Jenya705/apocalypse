package com.justserver.apocalypse.items.guns.modifications;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scope extends Modify {

    public static ArrayList<Player> inScope = new ArrayList<>();

    public Scope(Apocalypse plugin) {
        super(plugin);
        minIronNuggets = 6;
        maxIronNuggets = 7;
    }

    private final ArrayList<String> guns = new ArrayList<>(Arrays.asList("PISTOL", "M4A4", "SVD"));

    @Override
    public List<String> getForGuns() {
        return guns;
    }


    @Override
    public String customName() {
        return "Прицел";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        super.onInteract(event);
        Player player = event.getPlayer();
        if (!inScope.contains(player)) {
            inScope.add(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255, true, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.SLOW);
            inScope.remove(player);
        }
    }

    @Override
    public Material getMaterial() {
        return Material.SPYGLASS;
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
}
