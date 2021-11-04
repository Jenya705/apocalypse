package com.justserver.apocalypse.gui;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.base.workbenches.Craft;
import com.justserver.apocalypse.base.workbenches.CraftItem;
import com.justserver.apocalypse.base.workbenches.Workbench;
import com.justserver.apocalypse.utils.InventoryUtils;
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

public class WorkbenchGui extends Gui{
    public final Integer level;
    public final Workbench workbench;
    public final Apocalypse plugin;

    public WorkbenchGui(Apocalypse plugin, Workbench workbench) throws NoSuchFieldException, IllegalAccessException {
        this.level = workbench.getLevel();
        this.plugin = plugin;
        Inventory inventory = Bukkit.createInventory(null, 9, getName());
        for(int slot = 0; slot < inventory.getSize();slot++){
            inventory.setItem(slot, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        for(int slot = 0; slot < workbench.getCrafts().size();slot++){
            ItemStack item = workbench.getCrafts().get(slot).getCraftResult().createItemStack(plugin);
            ItemMeta meta = item.getItemMeta();
            ArrayList<Component> lore = new ArrayList<>();
            for(CraftItem itemInCraft : workbench.getCrafts().get(slot).needItems()){
                int count = itemInCraft.count;
                lore.add(Component.text(ChatColor.GRAY + String.valueOf(count) + " ").append(Component.translatable(itemInCraft.item.getMaterial().getTranslationKey()).color(NamedTextColor.GRAY)));
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
        return "Верстак " + level + " лвл";
    }

    @Override
    public Gui handleClick(InventoryClickEvent event, Player player, ItemStack itemStack, InventoryView view, ClickType clickType) {
        Craft craft = workbench.getCrafts().get(event.getSlot());
        for(CraftItem item : craft.needItems()){
            if(!InventoryUtils.hasItem(player.getInventory(), item.item.createItemStack(plugin), item.count)){
                player.sendMessage(ChatColor.DARK_RED + "У вас недостаточно ресурсов");
                return null;
            }
        }
        for(CraftItem item : craft.needItems()){
            InventoryUtils.removeItem(player.getInventory(), item.item.createItemStack(plugin), item.count);
        }
        try {
            player.getInventory().addItem(craft.getCraftResult().createItemStack(plugin));
        } catch (NoSuchFieldException | IllegalAccessException ignored) {

        }
        player.sendMessage(ChatColor.GREEN + "Вы успешно создали предмет");
        return this;
    }

    @Override
    public Gui handleInventoryClick(InventoryClickEvent event, Player player, ItemStack itemStack, ClickType clickType) {
        return null;
    }
}
