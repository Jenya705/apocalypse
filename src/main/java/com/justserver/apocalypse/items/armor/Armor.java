package com.justserver.apocalypse.items.armor;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
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
        return createItemStack(plugin, false);
    }

    @Override
    public ItemStack createItemStack(Apocalypse plugin, boolean rarityUpgraded) {
        ItemStack itemStack = super.createItemStack(plugin, rarityUpgraded);
        if (protectionLevel == 0 && durability == 0) return itemStack;
        ItemMeta meta = itemStack.getItemMeta();
        if (protectionLevel != 0) {
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, protectionLevel + (rarityUpgraded ? 1 : 0), true);
        }

        if (durability != 0) {
            meta.addEnchant(Enchantment.DURABILITY, durability, true);
        }
        meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
