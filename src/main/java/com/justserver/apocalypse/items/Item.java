package com.justserver.apocalypse.items;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public abstract class Item extends ItemLoader implements IItem {
    public String getId(){
        return this.getClass().getSimpleName().toUpperCase();
    }

    public ItemStack createItemStack(Apocalypse plugin){
        ItemStack is = new ItemStack(getMaterial());
        ItemMeta meta = is.getItemMeta();
        meta.setLocalizedName(getRarity().getColor() + customName());
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "APO_ID"), PersistentDataType.STRING, getId());
        is.setItemMeta(meta);
        return is;
    }
}
