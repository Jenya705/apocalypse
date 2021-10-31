package com.justserver.apocalypse;

import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.guns.FlyingAxe;
import com.justserver.apocalypse.items.normal.Medkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;

public class Registry {
    private static Apocalypse plugin = null;
    public final static Medkit MEDKIT = new Medkit(plugin);
    public final static FlyingAxe FLYING_AXE = new FlyingAxe(plugin);

    public static void init(Apocalypse _plugin){
        plugin = _plugin;
    }
    
    public static Item getItemByItemstack(ItemStack itemStack){
        if(!itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "APO_ID"), PersistentDataType.STRING)) return null;
        ItemMeta meta = itemStack.getItemMeta();
        String id = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "APO_ID"), PersistentDataType.STRING);
        for(Field field : Registry.class.getFields()){
            try {
                Item item = (Item) field.get(Registry.class);
                if(item.getId().equalsIgnoreCase(id)){
                    return item;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
