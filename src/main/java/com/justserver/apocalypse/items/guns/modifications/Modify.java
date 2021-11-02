package com.justserver.apocalypse.items.guns.modifications;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract public class Modify extends Item {
    public Modify(Apocalypse plugin) {
        super(plugin);
    }

    abstract public List<String> getForGuns();

    public static List<String> getModifications(Apocalypse plugin, ItemStack gun){
        PersistentDataContainer dataContainer = gun.getItemMeta().getPersistentDataContainer();
        String modificationsString = dataContainer.get(new NamespacedKey(plugin, "modifications"), PersistentDataType.STRING);
        List<String> modifications;
        if(modificationsString == null){
            modifications = new ArrayList<>();
        }else{
            modifications = new ArrayList<String>(Arrays.asList(modificationsString.split(";")));
            modifications.removeIf((i) -> i.trim().equals(""));
        }
        return modifications;
    }

    public static PersistentDataContainer setModifications(Apocalypse plugin, ItemStack gun, List<String> modifications, PersistentDataContainer dataContainer){
        dataContainer.set(new NamespacedKey(plugin, "modifications"), PersistentDataType.STRING, String.join(";", modifications));
        return dataContainer;
    }

    public static boolean checkModifications(Apocalypse plugin, ItemStack gun, String name){
        if(getModifications(plugin, gun).contains(name)){
            return true;
        }
        return false;
    }

    @Override
    public ItemStack createItemStack(Apocalypse plugin, int damage) {
        ItemStack is = super.createItemStack(plugin);
        ItemMeta meta = is.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        //dataContainer.set(new NamespacedKey(plugin, "is_modify"), PersistentDataType.INTEGER, 1);
        return is;
    }
}
