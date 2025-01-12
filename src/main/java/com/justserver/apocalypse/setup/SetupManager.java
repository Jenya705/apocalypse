package com.justserver.apocalypse.setup;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.overworld.ChestType;
import com.justserver.apocalypse.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
    private final HashMap<UUID, ItemStack[]> savedContents = new HashMap<>();
    private final HashMap<UUID, ItemStack[]> savedArmor = new HashMap<>();

    public void enterSetup(Player player) {
        if (savedContents.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Вы уже в режиме настройки");
            return;
        }
        UUID uuid = player.getUniqueId();
        savedContents.put(uuid, player.getInventory().getContents());
        savedArmor.put(uuid, player.getInventory().getArmorContents());
        player.getInventory().clear();
        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().addItem(new ItemBuilder(Material.LIGHT_BLUE_DYE).setName("&bПометить полицейский сундук").toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.WHITE_DYE).setName("Пометить медицинский сундук").toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.BROWN_DYE).setName("&6Пометить обычный сундук").toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.GREEN_DYE).setName("&2Пометить военный сундук").toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.GRAY_DYE).setName("&8Пометить сундук фабрики").toItemStack());
        player.getInventory().addItem(new ItemBuilder(Material.RED_DYE).setName("&cПометить магазинный сундук").toItemStack());
//        int minX = -300;
//        int maxX = 300;
//        int minZ = -300;
//        int maxZ = 300;
//
//        for (int x = minX; x < maxX; x++) {
//            for (int z = minZ; z < maxZ; z++) {
//                for (int y = 93; y < 186; y++) {
//                    Block block = player.getWorld().getBlockAt(x, y, z);
//                    Material blockType = block.getType();
//                    if (blockType.equals(Material.BLACK_CONCRETE_POWDER)) {
//                        block.setType(Material.AIR);
//                        block.getWorld().spawnFallingBlock(block.getLocation().clone().add(0.5, 0, 0.5), Material.CHEST.createBlockData());
//                    } else if(blockType.equals(Material.BLACK_CONCRETE) || blockType.equals(Material.SMITHING_TABLE)){
//                        block.setType(Material.AIR);
//                    }
//                    if(y > 100 && (block.getWorld().getHighestBlockAt(block.getX(), block.getZ()).equals(block)) && block.getType().equals(Material.CHEST)) {
//                        block.setType(Material.AIR);
//                    }
//                }
//            }
//        }
    }

    public void exitSetup(Player player, boolean startup) {
        UUID uuid = player.getUniqueId();
        if (!savedContents.containsKey(uuid)) {
            if (!startup) {
                player.sendMessage(ChatColor.RED + "Вы не в режиме настройки");
            }

            return;
        }
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().setArmorContents(savedArmor.get(uuid));
        player.getInventory().setContents(savedContents.get(uuid));
        savedArmor.remove(uuid);
        savedContents.remove(uuid);
    }

//    @EventHandler
//    public void onChunkLoad(ChunkLoadEvent event){
//        int X = event.getChunk().getX() * 16;
//        int Z = event.getChunk().getZ() * 16;
//
//        for (int x = 0; x < 16; x++) {
//            for (int z = 0; z < 16; z++) {
//                for (int y = 93; y < 186; y++) {
//                    Block block = event.getChunk().getWorld().getBlockAt(X+x, y, Z+z);
//                    Material blockType = block.getType();
//                    if (blockType.equals(Material.BLACK_CONCRETE_POWDER)) {
//                        block.setType(Material.AIR);
//                        block.getWorld().spawnFallingBlock(block.getLocation().clone().add(0.5, 0, 0.5), Material.CHEST.createBlockData());
//                    } else if(blockType.equals(Material.BLACK_CONCRETE) || blockType.equals(Material.SMITHING_TABLE)){
//                        block.setType(Material.AIR);
//                    }
//                    if(y > 100 && (block.getWorld().getHighestBlockAt(block.getX(), block.getZ()).equals(block)) && block.getType().equals(Material.CHEST)) {
//                        block.setType(Material.AIR);
//                    }
//                }
//            }
//        }
//    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getClickedBlock() != null) {
            String name = event.getItem().getItemMeta().getDisplayName().toLowerCase();
            Block block = event.getClickedBlock();
            if (event.getItem().getType().equals(Material.CHEST)) return;
            if (block.getState() instanceof Chest) {
                event.setCancelled(true);
                Chest chest = (Chest) block.getState();
                ChestType chestType;

                if (name.contains("полицейский")) {
                    chestType = ChestType.POLICE;
                } else if (name.contains("военный")) {
                    chestType = ChestType.MILITARY;
                } else if (name.contains("фабрики")) {
                    chestType = ChestType.FACTORY;
                } else if (name.contains("медицинский")) {
                    chestType = ChestType.HOSPITAL;
                } else if (name.contains("магазинный")) {
                    chestType = ChestType.SHOP;
                } else {
                    chestType = ChestType.HOUSE;
                }

                chest.getPersistentDataContainer().set(new NamespacedKey(Apocalypse.getInstance(), "chest_type"), PersistentDataType.STRING, chestType.name());
                event.getPlayer().sendMessage("Set: " + chest.getPersistentDataContainer().has(new NamespacedKey(Apocalypse.getInstance(), "chest_type"), PersistentDataType.STRING));
                chest.update();
                event.getPlayer().sendMessage("Установлен тип: " + chestType);

            }

        }
    }
}
