package com.justserver.apocalypse.gui;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.Base;
import com.justserver.apocalypse.base.BaseHandler;
import com.justserver.apocalypse.gui.sign.SignMenuFactory;
import com.justserver.apocalypse.utils.ItemBuilder;
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

        ItemStack item = new ItemStack(Material.CLOCK);;
        ItemMeta itemMeta = item.getItemMeta();
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
        inventory.setItem(8, new ItemBuilder(Material.YELLOW_DYE).setName("&7Установить частоту рации для этой базы").addLoreLine("&7Настройте рацию, чтобы общатся").addLoreLine("&7со всеми членами базы на любом расстоянии").addLoreLine("&cНо вот только если кто-то другой получит вашу рацию").addLoreLine("&cили угадает частоту, он получит доступ к вашему чату!").addLoreLine("&4ИСПОЛЬЗУЙТЕ АККУРАТНО!").toItemStack());
        this.inventory = inventory;
        this.base = base;
    }


    @Override
    public Gui handleClick(InventoryClickEvent event, Player player, ItemStack itemStack, InventoryView view, ClickType clickType) {
        if(itemStack.getItemMeta().getDisplayName().contains("частоту")){
            Bukkit.getScheduler().runTaskLater(base.getPlugin(), () -> {
            SignMenuFactory.Menu menu = base.getPlugin().signMenuFactory.newMenu(Arrays.asList("", "===========", "Введите частоту", base.frequency.equals("1111") ? "" : "Текущая: " + base.frequency))
            .reopenIfFail(true)
            .response((playerResponse, strings) -> {
                try {
                    String frequency = strings[0];
                    if(frequency.trim().equals("")){
                        return true;
                    } else if(frequency.length() != 4){
                        playerResponse.sendMessage(ChatColor.RED + "Частота должна состоять из 4 символов");
                        return false;
                    } else if(frequency.equals("0000")){
                        playerResponse.sendMessage(ChatColor.RED + "Эта частота зарезервирована для глобального чата");
                        return false;
                    }
                    playerResponse.sendMessage(ChatColor.GREEN + "Частота приемника базы установлена: " + frequency);
                    Integer.parseInt(frequency);
                    base.frequency = frequency;
                    base.saveBase();
                } catch (Exception ex){
                    playerResponse.sendMessage(ChatColor.RED + "Введенные данные не число");
                    return false;
                }
                return true;
            });

            menu.open(player);}, 3);
            return null;
        }
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
        Apocalypse.getInstance().loadedBases.remove(this.base);
        Apocalypse.getInstance().loadedBases.add(this.base);
        player.sendMessage(ChatColor.GREEN + "Вы успешно продлили базу");
        return null;
    }

    @Override
    public Gui handleInventoryClick(InventoryClickEvent event, Player player, ItemStack itemStack, ClickType clickType) {
        return null;
    }
}
