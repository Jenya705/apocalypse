package com.justserver.apocalypse.overworld;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Cooldowns;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.base.Base;
import com.justserver.apocalypse.base.workbenches.PlayerCrafts;
import com.justserver.apocalypse.gui.CustomAnvilGui;
import com.justserver.apocalypse.gui.MeceratorGui;
import com.justserver.apocalypse.gui.WorkbenchGui;
import com.justserver.apocalypse.items.Gun;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import com.justserver.apocalypse.items.guns.FlyingAxe;
import com.justserver.apocalypse.items.guns.modifications.Modify;
import com.justserver.apocalypse.items.normal.Macerator;
import com.justserver.apocalypse.items.normal.Radio;
import com.justserver.apocalypse.tasks.ChestLootTask;
import com.justserver.apocalypse.tasks.CombatCooldownTask;
import com.justserver.apocalypse.tasks.ParachuteTask;
import com.justserver.apocalypse.utils.ItemBuilder;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import su.plo.voice.line.Line;
import su.plo.voice.socket.SocketServerUDP;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class OverworldHandler implements Listener {
    private final Apocalypse plugin;
    public final ArrayList<Location> lootedChests = new ArrayList<>();
    private final ArrayList<ItemRarity> randomTable = new ArrayList<>();
    public static final HashMap<UUID, ChestLootTask> chestLootTasks = new HashMap<>();
    public HashMap<Location, ChestType> clickedChests = new HashMap<>();
    public static volatile HashMap<UUID, Set<Item>> indexedItems = new HashMap<>();

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
            clickedChests.clear();
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
        Bukkit.getLogger().info(randomTable.size() + "");
//        Calendar due = Calendar.getInstance();
//        due.set(Calendar.MILLISECOND, 0);
//        due.set(Calendar.SECOND, 0);
//        due.set(Calendar.MINUTE, 0);
//        due.set(Calendar.HOUR_OF_DAY, 18);
//        if (due.before(Calendar.getInstance())) {
//            due.add(Calendar.HOUR, 24);
//        }
//        long next = due.getTimeInMillis() - new Date().getTime();
//        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//        executor.scheduleAtFixedRate(() -> Bukkit.getScheduler().runTask(plugin, () -> {
//            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "ЭИРДРОП С ТОПОВЫМ ЛУТОМ НА X: 0 Z: 0");
//            Location chestLocation = Bukkit.getWorld("world").getHighestBlockAt(0, 0).getLocation().clone().add(0, 1, 0);
//            chestLocation.getBlock().setType(Material.CHEST);
//            Chest chest = (Chest) chestLocation.getBlock().getState();
//            chest.getPersistentDataContainer().set(new NamespacedKey(plugin, "chest_type"), PersistentDataType.STRING, "airdrop");
//            int randomTop = random.nextInt(4);
//            Item randomWeapon = Registry.AK_47;
//            if (randomTop == 1) {
//                randomWeapon = Registry.SVD;
//            } else if (randomTop == 2) {
//                randomWeapon = Registry.M4A4;
//            }
//            chest.getBlockInventory().addItem(randomWeapon.createItemStack(plugin));
//            chest.getBlockInventory().addItem(new ItemStack(Material.IRON_INGOT, random.nextInt(32) + 31), new ItemStack(Material.BRICK, random.nextInt(32) + 31), new ItemStack(Material.OAK_PLANKS, 64));
//            if (random.nextInt(4) == 3) {
//                chest.getBlockInventory().addItem(Registry.WORKBENCH_3.createItemStack(plugin));
//            }
//            try {
//                if (random.nextInt(3) == 2) {
//                    chest.getBlockInventory().addItem(Registry.RECOMBOBULATOR.createItemStack(plugin));
//                }
//                chest.getBlockInventory().addItem(
//                        ((Item) Registry.class.getFields()[random.nextInt(Registry.size())].get(Registry.class)
//                        ).createItemStack(plugin)); // страшный код ну да ладно
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }), next, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            randomTeleport(event.getPlayer());
        }
        index(plugin, event.getPlayer(), true);
        Arrays.stream(event.getPlayer().getInventory().getContents())
                .filter(Objects::nonNull)
                .filter(itemStack -> Registry.getItemByItemstack(itemStack) instanceof Radio)
                .forEach(itemStack -> plugin.loadedBases.stream()
                        .filter(base -> base.frequency.equals(itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "frequency"), PersistentDataType.STRING)))
                        .forEach(base -> base.connectedPlayers.add(event.getPlayer())));
    }

    @EventHandler
    public void onRespawn(PlayerPostRespawnEvent event) {
        List<Base> playerBases = Base.getBasesByPlayer(plugin, event.getPlayer());
        if (playerBases.size() > 0) {
            if (playerBases.size() == 1) {
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
        for(Line line : Base.frequencyToLineMap.values()){
            line.subscribedPlayers.remove(event.getPlayer());
        }
        CombatCooldownTask task = CombatCooldownTask.tasks.get(event.getPlayer());
        if(task != null){
            event.getPlayer().damage(1000000, task.lastAttacker);
            task.cancel();
            CombatCooldownTask.tasks.remove(event.getPlayer());
        }
        ParachuteTask task1 = ParachuteTask.tasks.get(event.getPlayer());
        if (task1 != null) {
            task1.cancel();
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.loadedBases.forEach(base -> base.connectedPlayers.remove(event.getPlayer())));
    }

    public void randomTeleport(Player player) {

        int x = random.nextInt(20) * (random.nextBoolean() ? -1 : 1),
                z = random.nextInt(20) * (random.nextBoolean() ? -1 : 1);
        x += 0.5;
        z += 0.5;
        Location preLocation = player.getWorld().getSpawnLocation().clone();
        preLocation.setY(190);
        Cooldowns.noFall.add(player.getUniqueId());
        preLocation.setX(player.getWorld().getSpawnLocation().getX() + x);
        preLocation.setZ(player.getWorld().getSpawnLocation().getZ() + z);
       // preLocation.setY(player.getWorld().getSpawnLocation().get);
        player.teleport(preLocation);
    }

    private final SecureRandom random = new SecureRandom();
    //private final ChestType[] ALLOWED_RANDOM = {ChestType.FACTORY, ChestType.HOUSE, ChestType.MILITARY, ChestType.POLICE};

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event){
        ItemStack leggings = event.getPlayer().getEquipment().getLeggings();
        if(leggings == null || leggings.getType().equals(Material.AIR))return;
        Long lastSneak = ParachuteTask.lastSneak.get(event.getPlayer());
        if(lastSneak == null){
            ParachuteTask.lastSneak.put(event.getPlayer(), System.currentTimeMillis());
            return;
        }
        if((System.currentTimeMillis() - lastSneak) <= 500 && event.getPlayer().getFallDistance() > 5){
            new ParachuteTask(event.getPlayer());
        } else {
            ParachuteTask.lastSneak.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
            if (event.getClickedBlock().getType().equals(Material.CHEST) && !event.getPlayer().isSneaking()) {
                if(Base.getBaseByBlock(plugin, event.getClickedBlock()) != null){
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "Вы не можете лутать сундуки в зоне базы");
                    return;
                }
                Chest chest = (Chest) event.getClickedBlock().getState();
                ChestType chestType;
                boolean isAirdrop = false;
                if (clickedChests.containsKey(chest.getLocation())) {
                    chestType = clickedChests.get(chest.getLocation());
                } else {
                    if (chest.getPersistentDataContainer().has(new NamespacedKey(Apocalypse.getInstance(), "chest_type"), PersistentDataType.STRING)) {
                        String chestTypeString = chest.getPersistentDataContainer().get(new NamespacedKey(Apocalypse.getInstance(), "chest_type"), PersistentDataType.STRING);
                        if(chestTypeString.equals("airdrop")){
                            isAirdrop = true;
                            chestType = ChestType.MILITARY;
                        } else {
                            chestType = ChestType.valueOf(chest.getPersistentDataContainer().get(new NamespacedKey(plugin, "chest_type"), PersistentDataType.STRING));
                        }
                    } else {
                        int chance = random.nextInt(100);
                        if(chest.getLocation().getY() >= 140){
                            chance -= 20;
                        }

                        if(chance <= 0){
                            chance = 1;
                        }
                        switch (randomTable.get(chance)) {
                            case RARE -> chestType = ChestType.FACTORY;
                            case EPIC -> chestType = ChestType.POLICE;
                            case LEGENDARY -> chestType = ChestType.MILITARY;
                            default -> chestType = ChestType.HOUSE;
                        }
                        clickedChests.put(chest.getLocation(), chestType);
                        //chestType = ALLOWED_RANDOM[random.nextInt(ALLOWED_RANDOM.length)];
                    }
                }
                if(!isAirdrop && random.nextInt(200) == 199){
                    Bukkit.broadcastMessage(ChatColor.RED + event.getPlayer().getName() + " попался в ловушку ахахахах лох");
                    event.getPlayer().getWorld().spawn(event.getPlayer().getLocation(), TNTPrimed.class);
                    event.getPlayer().getWorld().spawn(event.getPlayer().getLocation(), TNTPrimed.class);
                    event.setCancelled(true);
                    lootedChests.add(event.getClickedBlock().getLocation());
                    return;
                }
                ItemStack[] contents = chest.getBlockInventory().getContents();
                chest.setCustomName(chestType.translate());
                chest.update();
                if (lootedChests.contains(event.getClickedBlock().getLocation())) return;
                for (ChestLootTask task : chestLootTasks.values()) {
                    if (task.getChest().equals(chest)) {
                        event.getPlayer().sendMessage(ChatColor.RED + "Этот сундук уже лутают"); // что
                        event.setCancelled(true);
                        return;
                    }
                }

                for (int i = 0; i < 27; i++) {
                    chest.getBlockInventory().setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("&cОжидайте").toItemStack());
                }
                boolean finalIsAirdrop = isAirdrop;
                ChestLootTask lootTask = new ChestLootTask(chest, this, () -> {
                    lootedChests.add(event.getClickedBlock().getLocation());
                    if(finalIsAirdrop){
                        chest.getBlockInventory().setContents(contents);
                    } else {
                        if (random.nextInt(300) == 299) {
                            chest.getBlockInventory().addItem(Registry.RECOMBOBULATOR.createItemStack(plugin));
                        }
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
                                    spawned = spawn;
                                    break;
                                }
                            }
                            alreadyHas.add(spawned);
                            if (spawned == null) {
                                spawned = whatSpawns.get(random.nextInt(whatSpawns.size()));
                            }

                            if (spawned instanceof Gun || spawned instanceof Modify || spawned instanceof FlyingAxe) {
                                if (gunSpawned) {
                                    i--;
                                    continue;
                                }
                                gunSpawned = true;
                            }
                            Set<Item> whatHas = indexedItems.get(event.getPlayer().getUniqueId());
                            if(whatHas != null) {
                                if (whatHas.contains(spawned) && random.nextInt(10) == 9) {
                                    i--;
                                    continue;
                                }
                            }
                            chest.getBlockInventory().setItem(random.nextInt(chest.getBlockInventory().getSize()), spawned.createItemStack(plugin));
                        }
                    }
                });
                lootTask.runTaskTimer(plugin, 7, 7);
                chestLootTasks.put(event.getPlayer().getUniqueId(), lootTask);
                return;
            } else if (event.getClickedBlock().getType().equals(Material.ANVIL)) {
                if (event.getHand().equals(EquipmentSlot.OFF_HAND) || event.getAction().name().contains("LEFT")) return;
                event.setCancelled(true);
                plugin.guiManager.setGui(event.getPlayer(), new CustomAnvilGui());
                return;
            } else if (event.getClickedBlock().getType().equals(Material.OBSIDIAN)) {
                if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
                plugin.guiManager.setGui(event.getPlayer(), new MeceratorGui());
                return;
            } else if (event.getClickedBlock().getType().equals(Material.BARREL)) {
                event.setCancelled(true);
                return;
            }
        }
        if (event.getItem() == null) return;
        if (event.getItem().getItemMeta() == null) return;
        if (!Objects.equals(event.getHand(), EquipmentSlot.HAND)) return;
        Item possibleItem = Registry.getItemByItemstack(event.getItem());
        if (possibleItem == null) return;
        possibleItem.onInteract(event);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player && event.getDamager() instanceof Player attacker) {
            CombatCooldownTask playerTask = CombatCooldownTask.tasks.get(player);
            if(playerTask == null){
                CombatCooldownTask.tasks.put(player, new CombatCooldownTask(player, attacker));
            } else {
                playerTask.revoke(attacker);
            }

            CombatCooldownTask attackerTask = CombatCooldownTask.tasks.get(attacker);
            if(attackerTask == null){
                CombatCooldownTask.tasks.put(attacker, new CombatCooldownTask(attacker, player));
            } else {
                attackerTask.revoke(player);
            }

            ItemStack itemStack = ((Player) event.getDamager()).getInventory().getItemInMainHand();
            itemStack = (itemStack.getType().equals(Material.AIR) ? null : itemStack);
            if (itemStack == null) return;
            Item possibleItem = Registry.getItemByItemstack(itemStack);
            if (possibleItem == null) return;
            if (possibleItem.getLeftDamage() != 0 && !((Player) event.getEntity()).isBlocking()) {
                if (possibleItem.getLeftDamage() != 0) {
                    double finalModifier = 1.0;
                    for(Base playerBase : Base.getPlayersBase(attacker)){
                        if(playerBase.players.contains(player.getUniqueId())){
                            System.out.println("Decreasing damage");
                            finalModifier = 0.3;
                            break;
                        }
                    }
                    boolean rarityUpgraded = itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "rarity_upgraded"), PersistentDataType.INTEGER);
                    event.setDamage((possibleItem.getLeftDamage() * (rarityUpgraded ? 1.3 : 1) * ((Player) event.getDamager()).getAttackCooldown()) * finalModifier);
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        CombatCooldownTask task = CombatCooldownTask.tasks.get(event.getPlayer());
        if(task != null){
            task.cancel();
        }
        try {
            ParachuteTask.tasks.get(event.getPlayer()).cancel();
        } catch (NullPointerException ignored){}
    }

    @EventHandler
    public void onDamageByEnv(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL) && Cooldowns.noFall.contains(event.getEntity().getUniqueId())){
                event.setCancelled(true);
                Cooldowns.noFall.remove(event.getEntity().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onGuiClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() != null) {
            if (event.getInventory().getHolder() instanceof Chest chest) {
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
    public void openCraftGui(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().name().contains("RIGHT") && player.isSneaking() && event.getHand().equals(EquipmentSlot.HAND) && !event.hasItem()) {
            if (event.getClickedBlock() != null) {
                Material clickedBlock = event.getClickedBlock().getType();
                if (clickedBlock.equals(Material.CHEST)) {
                    return;
                } else if(clickedBlock.equals(Material.OBSIDIAN)){
                    Base base = Base.getBaseByBlock(plugin, event.getClickedBlock());
                    if (base != null) {
                        if(!base.players.contains(event.getPlayer().getUniqueId())){
                            player.sendMessage(ChatColor.RED + "Вы не можете взаимодействовать с этим тут");
                            event.setCancelled(true);
                            return;
                        }
                    }
                    event.getClickedBlock().setType(Material.AIR);
                    player.getWorld().dropItemNaturally(player.getLocation(), Registry.MACERATOR.createItemStack(plugin));
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
                    player.closeInventory();
                    return;
                }
            }
            try {
                plugin.guiManager.setGui(event.getPlayer(), new WorkbenchGui(plugin, new PlayerCrafts(plugin)));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().startsWith("@")) {
            event.setCancelled(true);
            if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                event.getPlayer().sendMessage(ChatColor.RED + "У вас нет рации в руке");
                return;
            }

            if (Registry.getItemByItemstack(event.getPlayer().getInventory().getItemInMainHand()) instanceof Radio) {
                event.setCancelled(true);
                String message = event.getMessage().substring(1);
                plugin.loadedBases.stream().filter(base -> base.frequency.equals(
                        event.getPlayer().getInventory()
                                .getItemInMainHand()
                                .getItemMeta()
                                .getPersistentDataContainer()
                                .get(new NamespacedKey(plugin, "frequency"), PersistentDataType.STRING)
                )).forEach(base -> base.connectedPlayers.forEach(player -> player.sendMessage(ChatColor.GOLD + "[Рация] "
                                        + ChatColor.DARK_GRAY + event.getPlayer().getName()
                                        + ": "
                                        + ChatColor.WHITE + message)));
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "У вас нет рации в руке");
            }
        }
    }

    @EventHandler
    public void onChangeEquipment(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(event.getNewSlot());
        if (itemStack == null) {
            SocketServerUDP.clients.get(player).mainLine = null;
            player.setWalkSpeed(0.2f);
            return;
        }
        itemStack = (itemStack.getType().equals(Material.AIR) ? null : itemStack);
        if (itemStack == null) {
            SocketServerUDP.clients.get(player).mainLine = null;
            player.setWalkSpeed(0.2f);
            return;
        }
        boolean rarityUpgraded = itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "rarity_upgraded"), PersistentDataType.INTEGER);
        Item possibleItem = Registry.getItemByItemstack(itemStack);
        if (possibleItem == null) {
            SocketServerUDP.clients.get(player).mainLine = null;
            player.setWalkSpeed(0.2f);
            return;
        }
        if(possibleItem instanceof Radio){

            for (Map.Entry<String, Line> entry : Base.frequencyToLineMap.entrySet()){
                    if(entry.getKey().equals(itemStack
                            .getItemMeta()
                            .getPersistentDataContainer()
                            .get(new NamespacedKey(plugin, "frequency"), PersistentDataType.STRING))) {
                        entry.getValue().subscribedPlayers.add(player);
                        if(SocketServerUDP.clients.get(player) != null){
                            SocketServerUDP.clients.get(player).mainLine = entry.getValue();
                        } else {
                            player.sendActionBar("Вы еще не подключены к Plasmo Voice");
                        }
                        break;
                    }
            }
        } else {
            if(SocketServerUDP.clients.get(player) != null){
                SocketServerUDP.clients.get(player).mainLine = null;
            } else {
                player.sendActionBar("Вы еще не подключены к Plasmo Voice");
            }
        }
        player.setWalkSpeed((float) (0.2f - (0.2f * ((possibleItem.getSlowdown() / 100f) / (rarityUpgraded ? 1.3 : 1)))));
        index(plugin, player, false);
    }

    public void index(Apocalypse plugin, Player player, boolean checkHand){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for(Map.Entry<String, Line> entry : Base.frequencyToLineMap.entrySet()){
                entry.getValue().subscribedPlayers.remove(player);
            }

            Set<Item> newList = new HashSet<>();
            for(ItemStack itemStack1 : player.getInventory()){
                if(itemStack1 == null) continue;
                if(itemStack1.getItemMeta() == null) continue;
                Item pItem = Registry.getItemByItemstack(itemStack1);
                if(pItem == null) continue;
                if(pItem instanceof Radio){
                    String frequency = itemStack1.getItemMeta().getPersistentDataContainer()
                            .get(new NamespacedKey(plugin, "frequency"), PersistentDataType.STRING);
                    Line line = Base.frequencyToLineMap.get(frequency);
                    if(line == null){
                        line = new Line();
                        line.subscribedPlayers.add(player);
                        Base.frequencyToLineMap.put(frequency, line);
                    } else {
                        line.subscribedPlayers.add(player);
                    }
                    Base.frequencyToLineMap.replace(frequency, line);
                }
                newList.add(pItem);
            }
            if(indexedItems.containsKey(player.getUniqueId())){
                indexedItems.replace(player.getUniqueId(), newList);
            } else {
                indexedItems.put(player.getUniqueId(), newList);
            }
        });
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        index(plugin, event.getPlayer(), true);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event){
        if(event instanceof Player player){
            index(plugin, player, true);
        }
    }
    @EventHandler
    public void onCrossbowRecharge(EntityLoadCrossbowEvent event) {
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

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        ItemStack result = event.getInventory().getResult();
        if(result == null) return;
        if(result.getType().equals(Material.SNOW_BLOCK) || result.getType().equals(Material.BRICKS) || result.getType().equals(Material.WHITE_WOOL)) return;
        event.getInventory().setResult(null);
    }
}
