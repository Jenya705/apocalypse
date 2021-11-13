package com.justserver.apocalypse.gui;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.dungeon.Recombobulator;
import com.justserver.apocalypse.items.guns.components.Component;
import com.justserver.apocalypse.items.guns.modifications.Modify;
import com.justserver.apocalypse.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class CustomAnvilGui extends Gui{

    public CustomAnvilGui(){
        inventory = Bukkit.createInventory(null, 54, getName());
        for(int i = 0; i < 54; i++){
            inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        inventory.setItem(0, new ItemBuilder(Material.PAPER).setName(ChatColor.GREEN + "Как это работает?").setLore(
                ChatColor.GRAY + "Положите в первый слот предмет",
                ChatColor.GRAY + "который хотите улучшить",
                ChatColor.GRAY + "а во второй слот - предмет улучшения",
                ChatColor.GRAY + "(напр. Рекомбулятор)"
        ).toItemStack());
        inventory.setItem(10, new ItemStack(Material.AIR));
        inventory.setItem(16, new ItemStack(Material.AIR));
        inventory.setItem(22, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName(ChatColor.GREEN + "Продолжить").toItemStack());
        inventory.setItem(40, new ItemStack(Material.AIR));
    }

    @Override
    public String getName() {
        return "Наковальня";
    }

    @Override
    public Gui handleClick(InventoryClickEvent event, Player player, ItemStack itemStack, InventoryView view, ClickType clickType) throws NoSuchFieldException, IllegalAccessException {
        if(isInventory(event.getView())) {
            return handle(player, event);
        }
        return new CustomAnvilGui();
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event, Player player, ItemStack itemStack, ClickType clickType) {
        event.setCancelled(false);
    }

    public Gui handle(Player player, InventoryClickEvent event){
        if(event.getClickedInventory() != null){
            if(event.getCurrentItem() != null) {
                if(Registry.getItemByItemstack(event.getCurrentItem()) != null){
                    event.setCancelled(false);
                    return new CustomAnvilGui();
                } else if (event.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
                    event.setCancelled(true);
                    ItemStack itemToUpgrade = event.getClickedInventory().getItem(10);
                    ItemStack upgradeItem = event.getClickedInventory().getItem(16);

                    if (itemToUpgrade == null || upgradeItem == null) {
                        return new ReturnGui();
                    }
                    Item possibleItem = Registry.getItemByItemstack(itemToUpgrade);
                    if (possibleItem == null || (possibleItem instanceof Component || possibleItem instanceof Modify || possibleItem instanceof Recombobulator)) {
                        player.sendMessage(ChatColor.RED + "Этот предмет нельзя улучшить");
                        return null;
                    }
                    if(itemToUpgrade.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Apocalypse.getInstance(), "rarity_upgraded"), PersistentDataType.INTEGER)){
                        player.sendMessage(ChatColor.RED + "Редкость предмета можно улучшить только 1 раз");
                        return new CustomAnvilGui();
                    }
                    if (Registry.getItemByItemstack(upgradeItem) instanceof Recombobulator) {
                        itemToUpgrade.setAmount(0);
                        upgradeItem.setAmount(0);
                        inventory.setItem(40, possibleItem.createItemStack(Apocalypse.getInstance(), true));
                    }
                } else event.setCancelled(true);
            }else {
                event.setCancelled(false);
            }
        }
        return new CustomAnvilGui();
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        ItemStack itemToUpgrade = event.getInventory().getItem(10);
        ItemStack upgradeItem = event.getInventory().getItem(16);
        ItemStack upgradedItem = event.getInventory().getItem(40);
        if(itemToUpgrade != null){
            event.getPlayer().getInventory().addItem(itemToUpgrade);
        }
        if(upgradeItem != null){
            event.getPlayer().getInventory().addItem(upgradeItem);
        }
        if(upgradedItem != null){
            event.getPlayer().getInventory().addItem(upgradedItem);
        }
    }
}
