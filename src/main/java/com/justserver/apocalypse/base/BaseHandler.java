package com.justserver.apocalypse.base;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.base.workbenches.Workbench;
import com.justserver.apocalypse.gui.BaseGui;
import com.justserver.apocalypse.gui.WorkbenchGui;
import com.justserver.apocalypse.items.Item;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public record BaseHandler(Apocalypse plugin) implements Listener {

//    public final Apocalypse plugin;
//
//    //public BaseHandler(Apocalypse plugin){
//        this.plugin = plugin;
//    }

    @EventHandler
    public void baseRegionBreak(BlockBreakEvent event) throws NoSuchFieldException, IllegalAccessException {
        if (event.getPlayer().getWorld().getName().contains("dungeon")) return;
        if (BlockTypes.canPlaceBlock.contains(event.getBlock().getType())) return;
        Base base = Base.getBaseByBlock(plugin, event.getBlock());
        Material blockType = event.getBlock().getType();
        if ((blockType.name().contains("FURNACE") || blockType.equals(Material.SMOKER))) {

            if (base == null) {
                checkWorkbench(event, blockType);
            } else if (base.players.contains(event.getPlayer().getUniqueId())) {
                checkWorkbench(event, blockType);
            }
            return;
        }
        if (base == null || !base.players.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.DARK_RED + "Вы не можете ломать блоки на чужой базе");
        }
    }

    private void checkWorkbench(BlockBreakEvent event, Material blockType) throws IllegalAccessException, NoSuchFieldException {
        String id = switch (blockType) {
            case SMOKER -> "WORKBENCH_2";
            case BLAST_FURNACE -> "WORKBENCH_3";
            default -> "WORKBENCH_1";
        };
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
        Item needed = (Item) Registry.class.getDeclaredField(id).get(Registry.class);
        event.getPlayer().getInventory().addItem(needed.createItemStack(plugin));
    }

    @EventHandler
    public void baseRegionPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getWorld().getName().contains("dungeon")) return;
        if (BlockTypes.canPlaceBlock.contains(event.getBlock().getType())) return;
        Block block = event.getBlock();
        Base base = Base.getBaseByBlock(plugin, block);
        if (base == null || !base.players.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.DARK_RED + "Вы не можете ставить блоки на чужой базе");
            return;
        }
        if (base.blocks.stream().map((value) -> (Location) value.get("location")).noneMatch((it) -> it.equals(block.getLocation())) && !BlockTypes.canBreakBlocksOnBase.contains(block.getType())) {
            event.setCancelled(true);
        }
        if (event.getBlock().getType().equals(Material.CHEST)) {
            block.setType(Material.DISPENSER);
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        Item possibleItem = Registry.getItemByItemstack(event.getItemInHand());
        if (Registry.getItemByItemstack(event.getItemInHand()) != null) {
            if (possibleItem instanceof Workbench) return;
            event.setCancelled(true);
            return;
        }
        if (event.getPlayer().getWorld().getName().contains("dungeon")) return;
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (Base.getBaseByBlock(plugin, block) != null) {
            Base base = Base.getBaseByBlock(plugin, block);
            if (base == null) return;
            if (base.players.contains(event.getPlayer().getUniqueId())) {
                //Material blockType = event.getBlock().getType();

                if (event.getBlock().getType().equals(Material.FIRE)) {
                    plugin.fires.add(event.getBlock().getLocation());
                    return;
                }
                HashMap<String, Object> blockInBase = new HashMap<>();
                blockInBase.put("location", block.getLocation());
                blockInBase.put("health", BlockTypes.blocks.getOrDefault(event.getBlock().getType(), 1.0));
                base.blocks.add(blockInBase);
                base.saveBase();
            }
        }
    }

    @EventHandler
    public void placeWorkbench(BlockPlaceEvent event) {
        if (Registry.getItemByItemstack(event.getItemInHand()) instanceof Workbench) {
            TileState state = (TileState) event.getBlock().getState();
            ItemStack item = event.getItemInHand();
            PersistentDataContainer dataContainer = state.getPersistentDataContainer();
            dataContainer.set(new NamespacedKey(plugin, "workbench"), PersistentDataType.INTEGER, 1);
            dataContainer.set(new NamespacedKey(plugin, "id"), PersistentDataType.STRING, Registry.getItemByItemstack(item).getId());
            state.update(true);
        }
    }

    @EventHandler
    public void openWorkbench(PlayerInteractEvent event) throws NoSuchFieldException, IllegalAccessException {
        if (event.getHand() != null && event.getHand().equals(EquipmentSlot.HAND) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getClickedBlock().getState() instanceof TileState state) {
                PersistentDataContainer dataContainer = state.getPersistentDataContainer();
                if (dataContainer.has(new NamespacedKey(plugin, "workbench"), PersistentDataType.INTEGER)) {
                    String id = dataContainer.get(new NamespacedKey(plugin, "id"), PersistentDataType.STRING);
                    Workbench workbench = (Workbench) Registry.getItemById(id);
                    plugin.guiManager.setGui(event.getPlayer(), new WorkbenchGui(plugin, workbench));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        if (event.getPlayer().getWorld().getName().contains("dungeon")) return;
        if (!BlockTypes.canBreakBlocksOnBase.contains(event.getBlock().getType())) {
            event.setCancelled(true);
            return;
        }
        if (event.getBlock().getType().equals(Material.CHEST)) {
            event.setCancelled(true);
            return;
        }
        Block block = event.getBlock();
        Player player = event.getPlayer();
        Base base = Base.getBaseByBlock(plugin, block);
        if (base != null) {
            if (base.getBlockByLocation(block.getLocation()) != null) {
                if (base.players.contains(event.getPlayer().getUniqueId())) {
                    base.blocks.remove(base.getBlockByLocation(block.getLocation()));
                    base.saveBase();
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setWalkSpeed(0.2f);
    }

    @EventHandler
    public void checkBlockHealth(PlayerInteractEvent event) {
        if (event.getPlayer().getWorld().getName().contains("dungeon")) return;
        if (event.getHand() != null && event.getHand().equals(EquipmentSlot.HAND) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Base base = Base.getBaseByBlock(plugin, event.getClickedBlock());
            if (base == null) return;
            Block block = event.getClickedBlock();
            HashMap<String, Object> hashMap = base.getBlockByLocation(block.getLocation());
            if (hashMap != null) {
                int health = (int) Math.ceil((Double) hashMap.get("health"));
                int maxHealth = 1;
                if (BlockTypes.blocks.containsKey(((Location) hashMap.get("location")).getBlock().getType())) {
                    maxHealth = (int) Math.ceil(BlockTypes.blocks.get(((Location) hashMap.get("location")).getBlock().getType()));
                }
                event.getPlayer().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + String.valueOf(health) + "/" + maxHealth + " прочности"));
            }
        }
    }

    @EventHandler
    public void openBaseGui(PlayerInteractEvent event) {
        if (event.getPlayer().getWorld().getName().contains("dungeon")) return;
        if (event.getHand() != null && event.getHand().equals(EquipmentSlot.HAND) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getClickedBlock() == null) return;
            Base base = Base.getBaseByLocation(plugin, event.getClickedBlock().getLocation());
            if (base == null) return;
            event.setCancelled(true);
            if (base.players.contains(event.getPlayer().getUniqueId()))
                plugin.guiManager.setGui(event.getPlayer(), new BaseGui(base));

        }
    }

    @EventHandler
    public void baseInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getWorld().getName().contains("dungeon")) return;
        if (event.getPlayer().isSneaking()) {
            event.setCancelled(true);
        }
        if (event.getHand() != null && event.getHand().equals(EquipmentSlot.HAND) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Base base = Base.getBaseByBlock(plugin, event.getClickedBlock());
            if (base == null) return;
            if (event.getClickedBlock() == null) return;
            Player player = event.getPlayer();
            if (!base.players.contains(player.getUniqueId()) && BlockTypes.interactBlocks.contains(event.getClickedBlock().getType())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.DARK_RED + "Вы не можете использовать это на чужой базе");
            }
        }
    }

    @EventHandler
    public void onTntExplosive(ExplosionPrimeEvent event) {
        event.setRadius(1.5f);
        Base base = Base.getBaseByBlock(plugin, event.getEntity().getLocation().getBlock());
        event.setCancelled(true);
        if (base == null) return;
        Location location = event.getEntity().getLocation();
        location.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, location, 5, 0, 0, 0);
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.1f);
        int minX = (int) (location.getBlockX() - event.getRadius());
        int minY = (int) (location.getBlockY() - event.getRadius());
        int minZ = (int) (location.getBlockZ() - event.getRadius());
        int maxX = (int) (location.getBlockX() + event.getRadius());
        int maxY = (int) (location.getBlockY() + event.getRadius());
        int maxZ = (int) (location.getBlockZ() + event.getRadius());
        float damage = 15f;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block blockTnt = (new Location(location.getWorld(), x, y, z).getBlock());
                    if (blockTnt.getType().equals(Material.TNT)) {
                        blockTnt.getLocation().getWorld().spawnEntity(blockTnt.getLocation(), EntityType.PRIMED_TNT);
                        blockTnt.setType(Material.AIR);
                    }
                    if (base.getBlockByLocation(new Location(location.getWorld(), x, y, z)) != null) {
                        HashMap<String, Object> hashMap = base.getBlockByLocation(new Location(location.getWorld(), x, y, z));
                        hashMap.replace("health", (double) hashMap.get("health") - damage);
                        for (int i = 0; i < base.blocks.size(); i++) {
                            HashMap<String, Object> block = base.blocks.get(i);
                            if ((double) block.get("health") <= 0.0) {
                                base.blocks.remove(block);
                                if (((Location) block.get("location")).getBlock().getState() instanceof InventoryHolder) {
                                    for (ItemStack item : ((InventoryHolder) ((Location) block.get("location")).getBlock().getState()).getInventory()) {
                                        if (item != null)
                                            ((Location) block.get("location")).getWorld().dropItemNaturally(((Location) block.get("location")), item);
                                    }

                                }
                                ((Location) block.get("location")).getBlock().setType(Material.AIR);
                                i--;
                            }
                        }

                    }
                }
            }
        }
        base.saveBase();
    }
}
