package com.justserver.apocalypse.gui;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.Base;
import com.justserver.apocalypse.base.BaseHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BaseGui extends Gui {
    @Override
    public String getName() {
        return "База";
    }

    public Base base;
    public HashMap<Material, Integer> price;

    public BaseGui(Base base){
        Inventory inventory = Bukkit.createInventory(null, 9, getName());
        for(int slot = 0; slot < inventory.getSize();slot++){
            inventory.setItem(slot, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        this.price = base.getPriceForDuration();

        ItemStack item;
        ItemMeta itemMeta;
        item = new ItemStack(Material.CLOCK);
        itemMeta = item.getItemMeta();
        int baseDuration = (int) Math.floor((base.duration.getEpochSecond() - Instant.now().getEpochSecond()) / 60);
        itemMeta.setDisplayName(ChatColor.GREEN + "Осталось у базы: " + baseDuration + " минут");
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("Для продления нужно:"));
        for(Map.Entry<Material, Integer> entry : this.price.entrySet()){
            lore.add(Component.text(ChatColor.GRAY + String.valueOf(entry.getValue()) + " ").append(Component.translatable(entry.getKey().translationKey()).color(NamedTextColor.GRAY)));
        }
        itemMeta.lore(lore);
        item.setItemMeta(itemMeta);
        inventory.setItem(4, item);
        this.inventory = inventory;
        this.base = base;
    }

    @Override
    public Gui handleClick(InventoryClickEvent event, Player player, ItemStack itemStack, InventoryView view, ClickType clickType) {
        boolean canRemove = true;
        for(Map.Entry<Material, Integer> entry : this.price.entrySet()){
            if(!player.getInventory().contains(entry.getKey(), entry.getValue())){
                canRemove = false;
            }
        }
        if(!canRemove) {
            player.sendMessage(ChatColor.DARK_RED + "У вас недостаточно ресурсов!");
            return null;
        }
        for(Map.Entry<Material, Integer> entry : this.price.entrySet()){
            for(ItemStack itemInInventory : player.getInventory()){
                if(itemInInventory != null && itemInInventory.getType().equals(entry.getKey())){
                    itemInInventory.setAmount(itemInInventory.getAmount() - entry.getValue());
                }
            }
        }
        this.base.duration = this.base.duration.plus(3, ChronoUnit.HOURS);
        this.base.saveBase();
        Apocalypse.getPlugin(Apocalypse.class).loadedBases.remove(this.base);
        Apocalypse.getPlugin(Apocalypse.class).loadedBases.add(this.base);
        player.sendMessage(ChatColor.GREEN + "Вы успешно продлили базу");
        return null;
    }

    @Override
    public Gui handleInventoryClick(InventoryClickEvent event, Player player, ItemStack itemStack, ClickType clickType) {
        return null;
    }
}
