package com.justserver.apocalypse.gui;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.workbenches.Craft;
import com.justserver.apocalypse.base.workbenches.CraftItem;
import com.justserver.apocalypse.base.workbenches.Workbench;
import com.justserver.apocalypse.items.BukkitItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class WorkbenchGui extends Gui {
    public final Integer level;
    public final Workbench workbench;
    public final Apocalypse plugin;

    public WorkbenchGui(Apocalypse plugin, Workbench workbench) throws NoSuchFieldException, IllegalAccessException {
        this.level = workbench.getLevel();
        this.plugin = plugin;
        Inventory inventory = Bukkit.createInventory(null, 27, getName());
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        for (int slot = 0; slot < workbench.getCrafts().size(); slot++) {
            ItemStack item = workbench.getCrafts().get(slot).getCraftResult().createItemStack(plugin);
            ItemMeta meta = item.getItemMeta();
            ArrayList<Component> lore = new ArrayList<>();
            for (CraftItem craftItem : workbench.getCrafts().get(slot).getNeedItems()) {
                if (craftItem == null) continue;
                if (craftItem.getItem() == null) continue;
                String name = craftItem.getItem().getMaterial().getTranslationKey();

                if (!(craftItem.getItem() instanceof BukkitItem)) {
                    name = craftItem.getItem().customName();
                }
                lore.add(Component.text(ChatColor.GRAY + String.valueOf(craftItem.getCount()) + " ").append(Component.translatable(name).color(NamedTextColor.GRAY)));
            }
            meta.lore(lore);
            item.setItemMeta(meta);
            inventory.setItem(slot, item);
        }
        this.inventory = inventory;
        this.workbench = workbench;
    }

    @Override
    public String getName() {
        String name = "Верстак " + level + " лвл";
        if (level == 0) {
            name = "Крафты";
        }
        return name;
    }

    @Override
    public Gui handleClick(InventoryClickEvent event, Player player, ItemStack itemStack, InventoryView view, ClickType clickType) {
        Craft craft = workbench.getCrafts().get(event.getSlot());
        boolean canRemove = true;
        for (CraftItem itemInCraft : craft.getNeedItems()) {
            if (!player.getInventory().contains(itemInCraft.getItem().getMaterial(), itemInCraft.getCount())) {
                canRemove = false;
            }
        }
        if (!canRemove) {
            player.sendMessage(ChatColor.DARK_RED + "У вас недостаточно ресурсов!");
            return null;
        }
        for (CraftItem itemInCraft : craft.getNeedItems()) {
            if (itemInCraft == null) continue;
            if (itemInCraft.getItem() == null) continue;
            for (ItemStack item : player.getInventory()) {
                if (item == null) continue;
                if (item.getType().equals(itemInCraft.getItem().getMaterial())) {
                    if (item.getAmount() < itemInCraft.getCount()) {
                        item.setAmount(0);
                    } else {
                        item.setAmount(item.getAmount() - itemInCraft.getCount());
                    }
                    break;
                }
            }
        }
        player.getInventory().addItem(craft.getCraftResult().createItemStack(plugin));
        player.sendMessage(ChatColor.GREEN + "Вы успешно создали предмет");
        return this;
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event, Player player, ItemStack itemStack, ClickType clickType) {
    }
}
