package com.justserver.apocalypse.overworld;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.Gun;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import com.justserver.apocalypse.items.guns.FlyingAxe;
import com.justserver.apocalypse.items.guns.modifications.Modify;
import com.justserver.apocalypse.tasks.ChestLootTask;
import com.justserver.apocalypse.utils.ItemBuilder;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.*;

public class OverworldHandler implements Listener {
    private final Apocalypse plugin;
    public final ArrayList<Location> lootedChests = new ArrayList<>();
    private final ArrayList<ItemRarity> randomTable = new ArrayList<>();
    public static final HashMap<UUID, ChestLootTask> chestLootTasks = new HashMap<>();
    public OverworldHandler(Apocalypse apocalypse) {
        this.plugin = apocalypse;
        Bukkit.getScheduler().runTaskTimer(apocalypse, () -> {
            for(Location chestLocation : lootedChests){
                Block chestBlock = chestLocation.getBlock();
                Chest chest = (Chest) chestBlock.getState();
                chest.getBlockInventory().clear();
                chest.update();
            }
            lootedChests.clear();
        }, 0, 20 * 60 * 5);
        for(int i = 0; i < 5; i++){
            randomTable.add(ItemRarity.LEGENDARY);
        }
        for(int i = 0; i < 40; i++){
            randomTable.add(ItemRarity.COMMON);
        }
        for(int i = 0; i < 30; i++){
            randomTable.add(ItemRarity.UNCOMMON);
        }
        for(int i = 0; i < 15; i++){
            randomTable.add(ItemRarity.RARE);
        }
        for(int i = 0; i < 10; i++){
            randomTable.add(ItemRarity.EPIC);
        }
        System.out.println(randomTable.size());
    }

//    @EventHandler
//    public void onJoin(PlayerJoinEvent event){
//        event.getPlayer().getInventory().addItem(Registry.FLYING_AXE.createItemStack(plugin));
//    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        plugin.guiManager.clear(event.getPlayer());
        if(Registry.FLYING_AXE.getThrownAxes().containsKey(event.getPlayer().getUniqueId())){
            event.getPlayer().getInventory().addItem(Registry.FLYING_AXE.getThrownAxes().get(event.getPlayer().getUniqueId()));
            Registry.FLYING_AXE.removePlayer(event.getPlayer().getUniqueId());
            chestLootTasks.remove(event.getPlayer().getUniqueId());
        }
    }
    private final SecureRandom random = new SecureRandom();
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getClickedBlock() != null){

            if(event.getClickedBlock().getType().equals(Material.CHEST)){
                //System.out.println("Pepega");
                Chest chest = (Chest) event.getClickedBlock().getState();
                if(chest.getPersistentDataContainer().has(new NamespacedKey(Apocalypse.getInstance(), "chest_type"), PersistentDataType.STRING)){
                    //System.out.println("NOOOOOOO");
                    ChestType chestType = ChestType.valueOf(chest.getPersistentDataContainer().get(new NamespacedKey(plugin, "chest_type"), PersistentDataType.STRING));
                    if(lootedChests.contains(event.getClickedBlock().getLocation())) return;
                    if(chestLootTasks.values().parallelStream().anyMatch(task -> task.getChest().equals(chest))){
                        event.getPlayer().sendMessage(ChatColor.RED + "Этот сундук уже лутают");
                        return;
                    }
                    for(int i = 0; i < 27; i++){
                        chest.getBlockInventory().setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("&cОжидайте").toItemStack());
                    }
                    ChestLootTask lootTask = new ChestLootTask(chest, this, () -> {
                        lootedChests.add(event.getClickedBlock().getLocation());
                        int lootCount = random.nextInt(4) + 1;
                        Item[] whatSpawnsPre = chestType.getWhatSpawns();
                        List<Item> whatSpawns = Arrays.asList(whatSpawnsPre);
                        Collections.shuffle(whatSpawns);
                        ArrayList<Item> alreadyHas = new ArrayList<>();
                        boolean gunSpawned = false;
                        for(int i = 0; i < lootCount; i++){
                            ItemRarity selectedRarity = randomTable.get(random.nextInt(100));
                            Item spawned = null;
                            spawner: for(Item spawn : whatSpawns){
                                if(spawn.getRarity().equals(selectedRarity) && !alreadyHas.contains(spawn)){
//                                    for(ItemStack hasItem : event.getPlayer().getInventory()){
//                                        if(Registry.getItemByItemstack(hasItem) != null && random.nextInt(20) > 17){
//                                            continue spawner;
//                                        }
//                                    }
                                    spawned = spawn;
                                    break;
                                }
                            }
                            alreadyHas.add(spawned);
                            if(spawned == null){
                                spawned = whatSpawns.get(random.nextInt(whatSpawns.size()));
                            }
                            //ItemStack spawnedItem = spawned.createItemStack(plugin);
                            //spawnedItem.setAmount(sp);

                            if(spawned instanceof Gun || spawned instanceof Modify || spawned instanceof FlyingAxe){
                                if(gunSpawned) continue;
                                gunSpawned = true;
                            }
                            chest.getBlockInventory().setItem(random.nextInt(chest.getBlockInventory().getSize()), spawned.createItemStack(plugin));
                        }
                    });
                    lootTask.runTaskTimer(plugin, 7, 7);
                    chestLootTasks.put(event.getPlayer().getUniqueId(), lootTask);

                }
                return;
            } else if(event.getClickedBlock().getType().equals(Material.ANVIL)){
                event.setCancelled(true);
                return;
            }
        }
        //System.out.println("Intraction start");
        if(event.getItem() == null) return;
        if(event.getItem().getItemMeta() == null) return;
        if(!Objects.equals(event.getHand(), EquipmentSlot.HAND)) return;
        //System.out.println("Before registry");
        Item possibleItem = Registry.getItemByItemstack(event.getItem());
        //System.out.println("Got item :" + possibleItem);
        if(possibleItem == null) return;
        //System.out.println("Interaction end");
        possibleItem.onInteract(event);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            ItemStack itemStack = ((Player) event.getDamager()).getInventory().getItemInMainHand();
            itemStack = (itemStack.getType().equals(Material.AIR) ? null : itemStack);
            if(itemStack == null) return;
            Item possibleItem = Registry.getItemByItemstack(itemStack);
            if(possibleItem == null) return;
            if(possibleItem.getLeftDamage() != 0){
                event.setDamage(possibleItem.getLeftDamage() / ((Player) event.getDamager()).getAttackCooldown());
            }
        }
    }

    @EventHandler
    public void onGuiClose(InventoryCloseEvent event){
        if(event.getInventory().getHolder() != null){
            if(event.getInventory().getHolder() instanceof Chest){
                Chest chest = (Chest) event.getInventory().getHolder();

                if(chestLootTasks.containsKey(event.getPlayer().getUniqueId())){
                    chestLootTasks.get(event.getPlayer().getUniqueId()).cancel();
                    chestLootTasks.remove(event.getPlayer().getUniqueId());
                    chest.getBlockInventory().clear();
                    chest.update();
                }
            }
        }
    }

    @EventHandler
    public void onChangeEquipment(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(event.getNewSlot());
        if(itemStack == null){
            player.setWalkSpeed(0.2f);
            return;
        }
        itemStack = (itemStack.getType().equals(Material.AIR) ? null : itemStack);
        if(itemStack == null) {
            player.setWalkSpeed(0.2f);
            return;
        }
        Item possibleItem = Registry.getItemByItemstack(itemStack);
        if(possibleItem == null) {
            player.setWalkSpeed(0.2f);
            return;
        }
        player.setWalkSpeed(0.2f - (0.2f * (possibleItem.getSlowdown() / 100f)));
    }

    @EventHandler
    public void onCrossbowRecharge(EntityLoadCrossbowEvent event){
        if(event.getEntity() instanceof Player){
            ItemStack itemStack = ((Player) event.getEntity()).getInventory().getItemInMainHand();
            itemStack = (itemStack.getType().equals(Material.AIR) ? null : itemStack);
            if(itemStack == null) return;
            Item possibleItem = Registry.getItemByItemstack(itemStack);
            if(possibleItem == null) return;
            if(possibleItem instanceof Gun){
                ItemMeta meta = itemStack.getItemMeta();
                PersistentDataContainer data = meta.getPersistentDataContainer();
                int currentAmmo = data.get(new NamespacedKey(plugin, "ammo_count"), PersistentDataType.INTEGER);
                if(currentAmmo != 0) {event.setCancelled(true); return;}
                data.set(new NamespacedKey(plugin, "ammo_count"), PersistentDataType.INTEGER, ((Gun) possibleItem).getInitialAmmoCount());
                itemStack.setItemMeta(meta);
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event){
        if(event.getRecipe().getResult().getType().equals(Material.CRAFTING_TABLE)){
            event.setCancelled(true);
        }
    }
}
