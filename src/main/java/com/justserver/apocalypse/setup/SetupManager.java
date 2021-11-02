package com.justserver.apocalypse.setup;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class SetupManager implements Listener {
    private HashMap<UUID, ItemStack[]> savedContents = new HashMap<>();
    private HashMap<UUID, ItemStack[]> savedArmor = new HashMap<>();

    public void enterSetup(Player player){
        if(savedContents.containsKey(player.getUniqueId())){
            player.sendMessage(ChatColor.RED + "Вы уже в режиме настройки");
            return;
        }
        UUID uuid = player.getUniqueId();
        savedContents.put(uuid, player.getInventory().getContents());
        savedArmor.put(uuid, player.getInventory().getArmorContents());
        player.getInventory().clear();
        player.getInventory().addItem(new ItemBuilder(Material.LIGHT_BLUE_DYE).setName("&bПометить полицейский сундук").toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.WHITE_DYE).setName("Пометить медицинский сундук").toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.BROWN_DYE).setName("&6Пометить обычный сундук").toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.GREEN_DYE).setName("&2Пометить военный сундук").toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.GRAY_DYE).setName("&8Пометить сундук фабрики").toItemStack());
    }

    public void exitSetup(Player player){
        UUID uuid = player.getUniqueId();
        if(!savedContents.containsKey(uuid)){
            player.sendMessage(ChatColor.RED + "Вы не в режиме настройки");
            return;
        }
        player.getInventory().clear();
        player.getInventory().setArmorContents(savedArmor.get(uuid));
        player.getInventory().setContents(savedContents.get(uuid));
        savedArmor.remove(uuid);
        savedContents.remove(uuid);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getItem() != null && event.getClickedBlock() != null){
            String name = event.getItem().getItemMeta().getDisplayName().toLowerCase();
            Block block = event.getClickedBlock();
            if(block.getState() instanceof Chest){
                Chest chest = (Chest) block.getState();
                String chestType;

                if(name.contains("полицейский")){
                    chestType = "police";
                } else if(name.contains("военный")){
                    chestType = "military";
                } else if(name.contains("фабрики")){
                    chestType = "factory";
                } else if(name.contains("медицинский")){
                    chestType = "hospital";
                }
                else {chestType = "default";}
                chest.getPersistentDataContainer().set(new NamespacedKey(Apocalypse.getInstance(), "chest_type"), PersistentDataType.STRING, chestType);
                event.getPlayer().sendMessage("Установлен тип: " + chestType);
            }

        }
    }
}
