package com.justserver.apocalypse.items.normal;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.gui.sign.SignMenuFactory;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Radio extends Item {
    public Radio(Apocalypse plugin) {
        super(plugin);
        minIronNuggets = 2;
        maxIronNuggets = 4;
    }

    @Override
    public String customName() {
        return "Рация";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
        if(event.getAction().name().contains("RIGHT")){
            String frequencyI = event.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "frequency"), PersistentDataType.STRING);

            SignMenuFactory.Menu menu = plugin.signMenuFactory.newMenu(Arrays.asList("", "===========", "Введите частоту", frequencyI.equals("1111") ? "" : "Текущая: " + frequencyI))
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
                            Integer.parseInt(frequency);
                            playerResponse.sendMessage(ChatColor.GREEN + "Частота рации установлена: " + frequency);
                            ItemMeta meta = event.getItem().getItemMeta();
                            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "frequency"), PersistentDataType.STRING, frequency);
                            meta.setLore(List.of(ChatColor.GOLD + "Чтобы узнать частоту, нажмите: " + ChatColor.YELLOW + "ПКМ", ChatColor.GRAY + "Вы будете получать сообщения по частоте", ChatColor.GRAY + "до тех пор, пока рация у вас в инвентаре",
                                    "", ChatColor.GRAY + "Чтобы отправить сообщение по линии нужно", ChatColor.GRAY + "взять нужную рацию в руки и отправить в чат сообщение", ChatColor.GRAY + "В формате @ваше_сообщение"));
                            event.getItem().setItemMeta(meta);
                            plugin.loadedBases.stream().filter(base -> base.frequency.equals(frequency)).forEach(base -> {base.connectedPlayers.remove(playerResponse); base.connectedPlayers.add(playerResponse);});
                        } catch (Exception ex){
                            playerResponse.sendMessage(ChatColor.RED + "Введенные данные не число");
                            return false;
                        }
                        return true;
                    });

            menu.open(event.getPlayer());
        }
    }

    @Override
    public Material getMaterial() {
        return Material.YELLOW_DYE;
    }

    @Override
    public double getLeftDamage() {
        return 0;
    }

    @Override
    public int getSlowdown() {
        return 0;
    }

    @Override
    public ItemStack createItemStack(Apocalypse plugin, int damage) {
        return this.createItemStack(plugin);
    }

    @Override
    public ItemStack createItemStack(Apocalypse plugin) {
        ItemStack itemStack = super.createItemStack(plugin);
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "frequency"), PersistentDataType.STRING, "1111");
        meta.setLore(List.of(ChatColor.GOLD + "Установите частоту рации используя " + ChatColor.YELLOW + "ПКМ", ChatColor.DARK_GRAY + "Используйте рацию только если хотите общатся", ChatColor.DARK_GRAY + "с тиммейтами или подслушивать других)", ChatColor.GRAY + "Каждая база может установить себе частоту", ChatColor.GRAY + "И также абсолютно каждая рация", ChatColor.GRAY + "может подключиться, если человек знает частоту"));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
