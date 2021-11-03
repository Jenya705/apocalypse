package com.justserver.apocalypse;

import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.armor.*;
import com.justserver.apocalypse.items.guns.*;
import com.justserver.apocalypse.items.guns.modifications.Grip;
import com.justserver.apocalypse.items.guns.modifications.Scope;
import com.justserver.apocalypse.items.guns.modifications.Silencer;
import com.justserver.apocalypse.items.normal.Knife;
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
    public final static Pistol PISTOL = new Pistol(plugin);
    public final static Bullets BULLETS = new Bullets(plugin);
    public final static Shotgun SHOTGUN = new Shotgun(plugin);
    public final static Scope SCOPE = new Scope(plugin);
    public final static Grip GRIP = new Grip(plugin);
    public final static Silencer SILENCER = new Silencer(plugin);
    public final static AK_47 AK_47 = new AK_47(plugin);
    public final static SVD SVD = new SVD(plugin);
    public final static M4A4 M4A4 = new M4A4(plugin);
    public final static Knife KNIFE = new Knife(plugin);
    public final static FlismyHelmet FLISMY_HELMET = new FlismyHelmet(plugin);
    public final static FlismyChestplate FLISMY_CHESTPLATE = new FlismyChestplate(plugin);
    public final static ThockHelmet THOCK_HELMET = new ThockHelmet(plugin);
    public final static ThockChestplate THOCK_CHESTPLATE = new ThockChestplate(plugin);
    public final static StrongHelmet STRONG_HELMET = new StrongHelmet(plugin);
    public final static StrongChestplate STRONG_CHESTPLATE = new StrongChestplate(plugin);

    public static void init(Apocalypse _plugin){
        plugin = _plugin;
        if(plugin == null){
            plugin = Apocalypse.getPlugin(Apocalypse.class);
        }
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
