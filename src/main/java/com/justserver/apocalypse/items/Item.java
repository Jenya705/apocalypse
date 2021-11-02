package com.justserver.apocalypse.items;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public abstract class Item extends ItemLoader implements IItem {
    protected Apocalypse plugin;

    public Item(Apocalypse plugin){
        this.plugin = plugin;
    }
    public String getId(){
        return this.getClass().getSimpleName().toUpperCase();
    }

    public ItemStack createItemStack(Apocalypse plugin){
        ItemStack is = new ItemStack(getMaterial());
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(getRarity().getColor() + customName());
        meta.setCustomModelData(getId().hashCode());
        int slowdown = getSlowdown();
        if(slowdown != 0){
            ArrayList<String> lore = new ArrayList<>();
            if(slowdown > 0){
                lore.add(ChatColor.RED + "-" + slowdown + "% скорости");
            } else {
                lore.add(ChatColor.GREEN + "+" + slowdown + "% скорости");
            }
            meta.setLore(lore);
        }
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "APO_ID"), PersistentDataType.STRING, getId());
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(meta);
        return is;
    }

    public ItemStack createItemStack(Apocalypse plugin, int damage){
        ItemStack is = new ItemStack(getMaterial());
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(getRarity().getColor() + customName());
        meta.setCustomModelData(getId().hashCode());
        int slowdown = getSlowdown();
        if(slowdown != 0){
            ArrayList<String> lore = new ArrayList<>();
            if(slowdown > 0){
                lore.add(ChatColor.RED + "-" + slowdown + "% скорости");
            } else {
                lore.add(ChatColor.GREEN + "+" + slowdown + "% скорости");
            }
            meta.setLore(lore);
        }
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "APO_ID"), PersistentDataType.STRING, getId());
        ((Damageable)meta).setDamage(damage);

        is.setItemMeta(meta);
        return is;
    }
}
