package com.justserver.apocalypse.overworld;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.base.Base;
import com.justserver.apocalypse.base.workbenches.PlayerCrafts;
import com.justserver.apocalypse.gui.MeceratorGui;
import com.justserver.apocalypse.gui.WorkbenchGui;
import com.justserver.apocalypse.items.Gun;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import com.justserver.apocalypse.items.guns.FlyingAxe;
import com.justserver.apocalypse.items.guns.modifications.Modify;
import com.justserver.apocalypse.items.normal.Radio;
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
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.N;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OverworldHandler implements Listener {
    private final Apocalypse plugin;
    public final ArrayList<Location> lootedChests = new ArrayList<>();
    private final ArrayList<ItemRarity> randomTable = new ArrayList<>();
    public static final HashMap<UUID, ChestLootTask> chestLootTasks = new HashMap<>();

    public OverworldHandler(Apocalypse apocalypse) {
        this.plugin = apocalypse;
        Bukkit.getScheduler().runTaskTimer(apocalypse, () -> {
            for (Location chestLocation : lootedChests) {
                Block chestBlock = chestLocation.getBlock();
                Chest chest = (Chest) chestBlock.getState();
                chest.getBlockInventory().clear();
                chest.update();
            }
            lootedChests.clear();
        }, 0, 20 * 60 * 5);
        for (int i = 0; i < 1; i++) {
            randomTable.add(ItemRarity.LEGENDARY);
        }
        for (int i = 0; i < 52; i++) {
            randomTable.add(ItemRarity.COMMON);
        }
        for (int i = 0; i < 28; i++) {
            randomTable.add(ItemRarity.UNCOMMON);
        }
        for (int i = 0; i < 14; i++) {
            randomTable.add(ItemRarity.RARE);
        }
        for (int i = 0; i < 5; i++) {
            randomTable.add(ItemRarity.EPIC);
        }
        System.out.println(randomTable.size());
        Calendar due = Calendar.getInstance();
        due.set(Calendar.MILLISECOND, 0);
        due.set(Calendar.SECOND, 0);
        due.set(Calendar.MINUTE,0);
        due.set(Calendar.HOUR_OF_DAY, 18);
        if (due.before(Calendar.getInstance())) {
            due.add(Calendar.HOUR, 1);
        }
        long next =  due.getTimeInMillis() - new Date().getTime();
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                Bukkit.broadcastMessage("Тест: эирдроп");
            });
        }, next, 60*60*1000, TimeUnit.MILLISECONDS);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(!event.getPlayer().hasPlayedBefore()){
            randomTeleport(event.getPlayer());
        }
        Arrays.stream(event.getPlayer().getInventory().getContents())
                .filter(Objects::nonNull)
                .filter(itemStack -> Registry.getItemByItemstack(itemStack) instanceof Radio)
                .forEach(itemStack -> plugin.loadedBases.stream()
                        .filter(base -> base.frequency.equals(itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "frequency"), PersistentDataType.STRING)))
                        .forEach(base -> base.connectedPlayers.add(event.getPlayer())));
    }

    @EventHandler
    public void onRespawn(PlayerPostRespawnEvent event){
        List<Base> playerBases = Base.getBasesByPlayer(plugin, event.getPlayer());
        if(playerBases.size() > 0){
            if(playerBases.size() == 1){
                event.getPlayer().teleport(playerBases.get(0).location);
            } else {
                event.getPlayer().teleport(playerBases.get(random.nextInt(playerBases.size())).location);
            }
        } else {
            randomTeleport(event.getPlayer());
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.guiManager.clear(event.getPlayer());
        if (Registry.FLYING_AXE.getThrownAxes().containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().getInventory().addItem(Registry.FLYING_AXE.getThrownAxes().get(event.getPlayer().getUniqueId()));
            Registry.FLYING_AXE.removePlayer(event.getPlayer().getUniqueId());
            chestLootTasks.remove(event.getPlayer().getUniqueId());
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.loadedBases.forEach(base -> base.connectedPlayers.remove(event.getPlayer())));
    }

    public void randomTeleport(Player player){
        int x = random.nextInt(20) * (random.nextBoolean() ? -1 : 1), z = random.nextInt(20) * (random.nextBoolean() ? -1 : 1);
        player.teleport(new Location(player.getWorld(), x, player.getWorld().getHighestBlockYAt(x, z) + 1, z));
    }

    private final SecureRandom random = new SecureRandom();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        //System.out.println("PEPEGA PEPGPEPGPEpgSPDGPSPDG");
        if (event.getClickedBlock() != null) {

            if (event.getClickedBlock().getType().equals(Material.CHEST)) {
                //System.out.println("Pepega");
                Chest chest = (Chest) event.getClickedBlock().getState();
                if (chest.getPersistentDataContainer().has(new NamespacedKey(Apocalypse.getInstance(), "chest_type"), PersistentDataType.STRING)) {
                    //System.out.println("NOOOOOOO");
                    ChestType chestType = ChestType.valueOf(chest.getPersistentDataContainer().get(new NamespacedKey(plugin, "chest_type"), PersistentDataType.STRING));
                    chest.setCustomName(chest.getPersistentDataContainer().get(new NamespacedKey(plugin, "chest_type"), PersistentDataType.STRING));
                    chest.update();
                    if (lootedChests.contains(event.getClickedBlock().getLocation())) return;
                    for(ChestLootTask task : chestLootTasks.values()){
                        if(task.getChest().equals(chest)){
                            event.getPlayer().sendMessage(ChatColor.RED + "Этот сундук уже лутают");
                            event.setCancelled(true);
                            return;
                        }
                    }
//                    if (chestLootTasks.containsValue(chest)) {
//
//                    }
                    for (int i = 0; i < 27; i++) {
                        chest.getBlockInventory().setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("&cОжидайте").toItemStack());
                    }
                    ChestLootTask lootTask = new ChestLootTask(chest, this, () -> {
                        lootedChests.add(event.getClickedBlock().getLocation());
                        int lootCount = random.nextInt(5) + 1;
                        Item[] whatSpawnsPre = chestType.getWhatSpawns();
                        List<Item> whatSpawns = Arrays.asList(whatSpawnsPre);
                        Collections.shuffle(whatSpawns);
                        ArrayList<Item> alreadyHas = new ArrayList<>();
                        boolean gunSpawned = false;
                        for (int i = 0; i < lootCount; i++) {
                            ItemRarity selectedRarity = randomTable.get(random.nextInt(100));
                            Item spawned = null;
                            for (Item spawn : whatSpawns) {
                                if (spawn.getRarity().equals(selectedRarity) && !alreadyHas.contains(spawn)) {
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
                            if (spawned == null) {
                                spawned = whatSpawns.get(random.nextInt(whatSpawns.size()));
                            }

                            if (spawned instanceof Gun || spawned instanceof Modify || spawned instanceof FlyingAxe) {
                                if (gunSpawned) continue;
                                gunSpawned = true;
                            }
                            chest.getBlockInventory().setItem(random.nextInt(chest.getBlockInventory().getSize()), spawned.createItemStack(plugin));
                        }
                    });
                    lootTask.runTaskTimer(plugin, 7, 7);
                    chestLootTasks.put(event.getPlayer().getUniqueId(), lootTask);

                } else {
                    event.setCancelled(true);
                }
                return;
            } else if (event.getClickedBlock().getType().equals(Material.ANVIL)) {
                event.setCancelled(true);
                return;
            } else if(event.getClickedBlock().getType().equals(Material.OBSIDIAN)){
                if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
                if(plugin.guiManager.playerToGuiMap.values().stream().anyMatch(gui -> gui instanceof MeceratorGui)){
                    event.getPlayer().sendMessage("Переработчиком уже пользуются");
                } else {
                    plugin.guiManager.setGui(event.getPlayer(), new MeceratorGui());
                }

                return;
            } else if(event.getClickedBlock().getType().equals(Material.BARREL)){
                event.setCancelled(true);
                return;
            }
        }
        //System.out.println("Intraction start");
        if (event.getItem() == null) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!Objects.equals(event.getHand(), EquipmentSlot.HAND)) return;
        //System.out.println("Before registry");
        Item possibleItem = Registry.getItemByItemstack(event.getItem());
        //System.out.println("Got item :" + possibleItem);
        if (possibleItem == null) return;
        //System.out.println("Interaction end");

        possibleItem.onInteract(event);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        //double cooldown = ((Player)event.getDamager()).getAttackCooldown();
        //System.out.println(cooldown);
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            ItemStack itemStack = ((Player) event.getDamager()).getInventory().getItemInMainHand();
            itemStack = (itemStack.getType().equals(Material.AIR) ? null : itemStack);
            if (itemStack == null) return;
            Item possibleItem = Registry.getItemByItemstack(itemStack);
            if (possibleItem == null) return;
            if (possibleItem.getLeftDamage() != 0 && !((Player) event.getEntity()).isBlocking()) {
                // double cooldown = ((Player) event.getDamager()).getAttackCooldown();
                //System.out.println(cooldown);
                if (possibleItem.getLeftDamage() != 0) {
                    event.setDamage(possibleItem.getLeftDamage() * ((Player) event.getDamager()).getAttackCooldown());
                }
            }
        }
    }

    @EventHandler
    public void onGuiClose (InventoryCloseEvent event){
        if (event.getInventory().getHolder() != null) {
            if (event.getInventory().getHolder() instanceof Chest) {
                Chest chest = (Chest) event.getInventory().getHolder();

                if (chestLootTasks.containsKey(event.getPlayer().getUniqueId())) {
                    chestLootTasks.get(event.getPlayer().getUniqueId()).cancel();
                    chestLootTasks.remove(event.getPlayer().getUniqueId());
                    chest.getBlockInventory().clear();
                    chest.update();
                }
            }
        }
    }

    @EventHandler
    public void openCraftGui(PlayerInteractEvent event){
        //System.out.println("Amegus");
        if(event.getAction().name().contains("RIGHT") && event.getPlayer().isSneaking() && event.getHand().equals(EquipmentSlot.HAND) && !event.hasItem()){
            if(event.getItem() != null){
                if(Registry.getItemByItemstack(event.getItem()) != null) return;
            }
            try {
                plugin.guiManager.setGui(event.getPlayer(), new WorkbenchGui(plugin, new PlayerCrafts(plugin)));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        if(event.getMessage().startsWith("@")){
            if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)){
                event.getPlayer().sendMessage(ChatColor.RED + "У вас нет рации в руке");
                event.setCancelled(true);
                event.setMessage("");
                return;
            }

            if(Registry.getItemByItemstack(event.getPlayer().getInventory().getItemInMainHand()) instanceof Radio){
                event.setCancelled(true);
                String message = event.getMessage().substring(1);
                plugin.loadedBases.stream().filter(base -> base.frequency.equals(
                        event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "frequency"), PersistentDataType.STRING)
                )).forEach(base -> base.connectedPlayers.forEach(player -> player.sendMessage(ChatColor.GOLD + "[Рация] "+ ChatColor.DARK_GRAY + event.getPlayer().getName() + ": " + ChatColor.WHITE + message)));
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "У вас нет рации в руке");
            }

        }
    }

    @EventHandler
    public void onChangeEquipment (PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(event.getNewSlot());
        if (itemStack == null) {
            player.setWalkSpeed(0.2f);
            return;
        }
        itemStack = (itemStack.getType().equals(Material.AIR) ? null : itemStack);
        if (itemStack == null) {
            player.setWalkSpeed(0.2f);
            return;
        }
        Item possibleItem = Registry.getItemByItemstack(itemStack);
        if (possibleItem == null) {
            player.setWalkSpeed(0.2f);
            return;
        }
        player.setWalkSpeed(0.2f - (0.2f * (possibleItem.getSlowdown() / 100f)));
    }

    @EventHandler
    public void onCrossbowRecharge (EntityLoadCrossbowEvent event){
        if (event.getEntity() instanceof Player) {
            ItemStack itemStack = ((Player) event.getEntity()).getInventory().getItemInMainHand();
            itemStack = (itemStack.getType().equals(Material.AIR) ? null : itemStack);
            if (itemStack == null) return;
            Item possibleItem = Registry.getItemByItemstack(itemStack);
            if (possibleItem == null) return;
            if (possibleItem instanceof Gun) {
                ItemMeta meta = itemStack.getItemMeta();
                PersistentDataContainer data = meta.getPersistentDataContainer();
                int currentAmmo = data.get(new NamespacedKey(plugin, "ammo_count"), PersistentDataType.INTEGER);
                if (currentAmmo != 0) {
                    event.setCancelled(true);
                    return;
                }
                data.set(new NamespacedKey(plugin, "ammo_count"), PersistentDataType.INTEGER, ((Gun) possibleItem).getInitialAmmoCount());
                itemStack.setItemMeta(meta);
            }
        }
    }
    //private final ArrayList<Material> bannedResults = new ArrayList<>()
    @EventHandler
    public void onCraft (CraftItemEvent event){
        Material type = event.getRecipe().getResult().getType();
        if (type.equals(Material.CRAFTING_TABLE) || type.name().contains("LEGGINGS") || type.name().contains("BOOTS") || type.equals(Material.OAK_PLANKS)) {
            event.setCancelled(true);
        }
    }
}
