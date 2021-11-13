package com.justserver.apocalypse.items;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Item extends ItemLoader implements IItem {
    protected Apocalypse plugin;
    protected int count = 1;
    protected final String id;
    public int minIronNuggets = 0;
    public int maxIronNuggets = 0;

    public Item(Apocalypse plugin){
        this.plugin = plugin;
        if(plugin == null){
            this.plugin = Apocalypse.getInstance();
        }
        String preId = getClass().getSimpleName().toUpperCase();
        for(Field field : Registry.class.getFields()){
            field.setAccessible(true);
            if(field.getType().equals(getClass())){
                preId = field.getName();
                break;
            }
        }
        this.id = preId;
        init();
    }
    public String getId(){
        return id;
    }

    public List<String> getDescription() {return new ArrayList<>();};

    public ItemStack createItemStack(Apocalypse plugin){
//        ItemStack is = new ItemStack(getMaterial(), count);
//        ItemMeta meta = is.getItemMeta();
//        meta.setDisplayName(getRarity().getColor() + customName());
//        meta.setCustomModelData(getId().hashCode());
//        int slowdown = getSlowdown();
//        ArrayList<String> lore = new ArrayList<>();
//        if(getLeftDamage() != 0){
//            lore.add(ChatColor.GRAY + "Урон: " + ChatColor.RED + "+" + getLeftDamage());
//        }
//        if(slowdown != 0){
//            if(slowdown > 0){
//                lore.add(ChatColor.RED + "-" + slowdown + "% скорости");
//            } else {
//                lore.add(ChatColor.GREEN + "+" + slowdown + "% скорости");
//            }
//        }
//
//        if(!getDescription().isEmpty()){
//            lore.add("");
//            for(String descriptionLine : getDescription()){
//                lore.add(ChatColor.GRAY + descriptionLine);
//            }
//        }
//
//        lore.add("");
//        lore.add(getRarity().translate().toUpperCase());
//        meta.setLore(lore);
//        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "APO_ID"), PersistentDataType.STRING, getId());
//        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//        is.setItemMeta(meta);
        return createItemStack(plugin, false);
    }

    public static boolean rarityUpgraded(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Apocalypse.getInstance(), "rarity_upgraded"), PersistentDataType.INTEGER);
    }

    public ItemStack createItemStack(Apocalypse plugin, boolean rarityUpgraded){
        ItemStack is = new ItemStack(getMaterial(), count);
        ItemMeta meta = is.getItemMeta();
        meta.setCustomModelData(getId().hashCode());
        int slowdown = (int) Math.floor(getSlowdown() / (rarityUpgraded ? 1.3 : 1));
        ArrayList<String> lore = new ArrayList<>();
        if(getLeftDamage() != 0){
            lore.add(ChatColor.GRAY + "Урон: " + ChatColor.RED + "+" + getLeftDamage() * Math.floor(rarityUpgraded ? 1.3 : 1));
        }
        if(slowdown != 0){
            if(slowdown > 0){
                lore.add(ChatColor.RED + "-" + slowdown + "% скорости");
            } else {
                lore.add(ChatColor.GREEN + "+" + slowdown + "% скорости");
            }
        }

        if(!getDescription().isEmpty()){
            lore.add("");
            for(String descriptionLine : getDescription()){
                lore.add(ChatColor.GRAY + descriptionLine);
            }
        }

        lore.add("");
        ItemRarity rarity = getRarity();
        if(rarityUpgraded){
            ItemRarity nextRarity = switch (getRarity()){
                case UNCOMMON -> ItemRarity.RARE;
                case RARE -> ItemRarity.EPIC;
                case EPIC -> ItemRarity.LEGENDARY;
                case LEGENDARY -> ItemRarity.MYTHIC;
                case MYTHIC, DUNGEON -> ItemRarity.SUPREME;
                default -> ItemRarity.UNCOMMON;
            };
            rarity = nextRarity;
            lore.add(nextRarity.getColor() + "" + ChatColor.MAGIC + "A " + nextRarity.translate().toUpperCase() + " " +ChatColor.MAGIC +"A ");
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "rarity_upgraded"), PersistentDataType.INTEGER, 1);
        } else {
            lore.add(getRarity().translate().toUpperCase());
        }
        meta.setDisplayName(rarity.getColor() + customName());
        if(rarity.equals(ItemRarity.LEGENDARY) || rarity.equals(ItemRarity.MYTHIC) || rarity.equals(ItemRarity.DUNGEON) || rarity.equals(ItemRarity.SUPREME)){
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "time_acquired"), PersistentDataType.LONG, System.currentTimeMillis());
        }
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "APO_ID"), PersistentDataType.STRING, getId());

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(meta);
        return is;
    }

    public ItemStack createItemStack(Apocalypse plugin, int damage){
        ItemStack is = createItemStack(plugin);
        ItemMeta meta = is.getItemMeta();
        ((Damageable)meta).setDamage(damage);

        is.setItemMeta(meta);
        return is;
    }

    @Override
    protected void init() {

    }
    private static final SecureRandom random = new SecureRandom();

    public ItemStack generateRandomNuggets(int amount){
        return new ItemStack(Material.IRON_NUGGET, generateInRange(minIronNuggets, maxIronNuggets) * amount);
    }

    private int generateInRange(int min, int max){
        if(min == max) return min;
        return random.nextInt(max - min) + min;
    }
}
