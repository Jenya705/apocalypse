package com.justserver.apocalypse.gui;

import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import static ru.epserv.epmodule.util.StyleUtils.green;
import static ru.epserv.epmodule.util.StyleUtils.regular;

public class MeceratorGui extends Gui {
    public MeceratorGui() {
        inventory = Bukkit.createInventory(null, 27, getName());
        for (int i = 0; i < 27; i++) {
            if (i == 0) continue;
            inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        inventory.setItem(8, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName(regular(green("Продолжить"))).toItemStack());
    }

    @Override
    public String getName() {
        return "Переработчик";
    }

    /*
    ItemStack recycleItem = inventory.getItem(0);
        if(recycleItem == null) return;
        Item possibleItem = Registry.getItemByItemstack(recycleItem);
        if(possibleItem == null) {
            player.sendMessage(ChatColor.RED + "Этот предмет нельзя переработать");
            event.setCancelled(true);
            return;
        }
        if(possibleItem.minIronNuggets == 0 || possibleItem.maxIronNuggets == 0){
            event.setCancelled(true);
            return;
        }
        player.getWorld().dropItem(player.getLocation(), possibleItem.generateRandomNuggets(recycleItem.getAmount()));
        recycleItem.setAmount(0);
     */

    @Override
    public Gui handleClick(InventoryClickEvent event, Player player, ItemStack itemStack, InventoryView view, ClickType clickType) throws NoSuchFieldException, IllegalAccessException {
        if (isInventory(event.getView())) {
            return handle(player, event);
        }
        return new MeceratorGui();
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event, Player player, ItemStack itemStack, ClickType clickType) {
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            event.setCancelled(false);
            player.sendMessage(ChatColor.RED + "Этот предмет нельзя переработать");
            return;
        }
        event.setCancelled(Registry.getItemByItemstack(item) == null);
    }

    public Gui handle(Player player, InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getCurrentItem() != null) {
                if (Registry.getItemByItemstack(event.getCurrentItem()) != null) {
                    event.setCancelled(false);
                    return new MeceratorGui();
                } else if (event.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
                    event.setCancelled(true);
                    ItemStack recycleItem = event.getClickedInventory().getItem(00);

                    if (recycleItem == null) {
                        return new ReturnGui();
                    }
                    Item possibleItem = Registry.getItemByItemstack(recycleItem);
                    if (possibleItem == null) {
                        player.sendMessage(ChatColor.RED + "Этот предмет нельзя переработать");
                        event.setCancelled(true);
                        return new MeceratorGui();
                    }
                    if (possibleItem.minIronNuggets == 0 || possibleItem.maxIronNuggets == 0) {
                        event.setCancelled(true);
                        return new MeceratorGui();
                    }
                    player.getWorld().dropItem(player.getLocation(), possibleItem.generateRandomNuggets(recycleItem.getAmount()));
                    recycleItem.setAmount(0);
                } else event.setCancelled(true);
            } else {
                event.setCancelled(false);
            }
        }
        return new CustomAnvilGui();
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        ItemStack returnItem = event.getInventory().getItem(0);
        if (returnItem == null) return;
        event.getPlayer().getInventory().addItem(returnItem);
    }
}
