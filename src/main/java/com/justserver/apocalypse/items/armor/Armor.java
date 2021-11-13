package com.justserver.apocalypse.items.armor;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Armor extends Item {
    private final int protectionLevel, durability;

    public Armor(Apocalypse plugin, int protectionLevel, int durability) {
        super(plugin);

        this.protectionLevel = protectionLevel;
        this.durability = durability;
    }

    @Override
    public ItemStack createItemStack(Apocalypse plugin) {
        ItemStack itemStack = super.createItemStack(plugin);
        if (protectionLevel == 0 && durability == 0) return itemStack;
        ItemMeta meta = itemStack.getItemMeta();
        if (protectionLevel != 0) {
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLevel, true);
        }

        if (durability != 0) {
            meta.addEnchant(Enchantment.DURABILITY, protectionLevel, true);
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public ItemStack createItemStack(Apocalypse plugin, int damage) {
        return createItemStack(plugin);
    }
}
