package com.justserver.apocalypse;

import com.justserver.apocalypse.base.workbenches.Workbench1;
import com.justserver.apocalypse.base.workbenches.Workbench2;
import com.justserver.apocalypse.base.workbenches.Workbench3;
import com.justserver.apocalypse.items.Gun;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.armor.*;
import com.justserver.apocalypse.items.dungeon.Recombobulator;
import com.justserver.apocalypse.items.guns.*;
import com.justserver.apocalypse.items.guns.components.*;
import com.justserver.apocalypse.items.guns.modifications.Grip;
import com.justserver.apocalypse.items.guns.modifications.Modify;
import com.justserver.apocalypse.items.guns.modifications.Scope;
import com.justserver.apocalypse.items.guns.modifications.Silencer;
import com.justserver.apocalypse.items.normal.Knife;
import com.justserver.apocalypse.items.normal.Macerator;
import com.justserver.apocalypse.items.normal.Medkit;
import com.justserver.apocalypse.items.normal.Radio;
import com.justserver.apocalypse.items.normal.tools.FlintAndSteel;
import com.justserver.apocalypse.items.normal.tools.Pickaxe;
import com.justserver.apocalypse.items.normal.tools.Shovel;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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
    public final static Butt BUTT = new Butt(plugin);
    public final static GunBody GUN_BODY = new GunBody(plugin);
    public final static MediumGunBody MEDIUM_GUN_BODY = new MediumGunBody(plugin);
    public final static SmallGunBody SMALL_GUN_BODY = new SmallGunBody(plugin);
    public final static Muzzle MUZZLE = new Muzzle(plugin);
    public final static Parachute PARACHUTE = new Parachute(plugin);
    public final static Radio RADIO = new Radio(plugin);
    public final static Recombobulator RECOMBOBULATOR = new Recombobulator(plugin);
    public static final Pickaxe PICKAXE = new Pickaxe(plugin);
    public static final Shovel SHOVEL = new Shovel(plugin);
    public static final FlintAndSteel FLINT_AND_STEEL = new FlintAndSteel(plugin);
    public static final Macerator MACERATOR = new Macerator(plugin);
    public static Workbench1 WORKBENCH_1;
    public static Workbench2 WORKBENCH_2;
    public static Workbench3 WORKBENCH_3;
    /**
     * Faster than just check field names
     */
    private static Map<String, Item> itemsMap = new HashMap<>();

    public static void init(Apocalypse _plugin) {
        plugin = _plugin;
        if (plugin == null) {
            plugin = Apocalypse.getPlugin(Apocalypse.class);
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> {// bukkit api have very cool static loading and sometimes fields used here are null (so use enums :D)
            WORKBENCH_1 = new Workbench1(plugin);
            WORKBENCH_2 = new Workbench2(plugin);
            WORKBENCH_3 = new Workbench3(plugin);
            System.out.println("Workbenches initialized successfully");
            try {
                for (Field field : Registry.class.getFields()) {
                    Item item = (Item) field.get(null);
                    itemsMap.put(item.getId(), item);
                    System.out.println("Custom model data " + item.customName() + ": " + item.getId().hashCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
                // failed to fill items
                itemsMap = null;
            }
        }, 20);
    }

    public static Item getItemByItemstack(ItemStack itemStack) {
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "APO_ID"), PersistentDataType.STRING))
            return null;
        ItemMeta meta = itemStack.getItemMeta();
        String id = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "APO_ID"), PersistentDataType.STRING);
        return getItemById(id);
    }

    public static Item getItemById(String id) {
        if (itemsMap != null) { // if not failed
            return itemsMap.get(id);
        }
        for (Field field : Registry.class.getFields()) {
            try {
                Item item = (Item) field.get(Registry.class);
                if (item.getId().equalsIgnoreCase(id)) {
                    return item;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int size() {
        return Registry.class.getFields().length;
    }
}
