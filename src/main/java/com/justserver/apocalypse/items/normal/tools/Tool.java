package com.justserver.apocalypse.items.normal.tools;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Tool extends Item {

    public Tool(Apocalypse plugin) {
        super(plugin);
        minIronNuggets = 5;
        maxIronNuggets = 7;
    }

    @Override
    public double getLeftDamage() {
        return 3;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public int getSlowdown() {
        return 10;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

    }

    @Override
    public ItemStack createItemStack(Apocalypse plugin, boolean rarityUpgraded) {
        ItemStack result = super.createItemStack(plugin, rarityUpgraded);
        ItemMeta meta = result.getItemMeta();
        meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.DURABILITY, 3, true);
        if(rarityUpgraded){
            if(getMaterial().equals(Material.IRON_PICKAXE)){
                meta.addEnchant(Enchantment.DIG_SPEED, 3, true);
            } else {
                meta.addEnchant(Enchantment.DURABILITY, 5, true);
                meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
            }
        }
        result.setItemMeta(meta);
        return result;
    }
}
