package com.justserver.apocalypse.base;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.gui.BaseGui;
import it.unimi.dsi.fastutil.Hash;
import net.kyori.adventure.identity.Identity;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.TNT;
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
import org.bukkit.persistence.PersistentDataType;
import org.w3c.dom.Text;

import java.util.*;

public class BaseHandler implements Listener {

    public final Apocalypse plugin;

    public BaseHandler(Apocalypse plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void baseRegionBreak(BlockBreakEvent event){
        if(BlockTypes.canPlaceBlock.contains(event.getBlock().getType())) return;
        if(Base.getBaseByBlock(plugin, event.getBlock()) == null || !Base.getBaseByBlock(plugin, event.getBlock()).players.contains(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.DARK_RED + "Вы не можете ломать блоки на чужой базе");
        }
    }

    @EventHandler
    public void baseRegionPlace(BlockPlaceEvent event){
        if(BlockTypes.canPlaceBlock.contains(event.getBlock().getType())) return;
        if(Base.getBaseByBlock(plugin, event.getBlock()) == null || !Base.getBaseByBlock(plugin, event.getBlock()).players.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.DARK_RED + "Вы не можете ставить блоки на чужой базе");
            return;
        }
        if(event.getBlock().getType().equals(Material.CHEST)){
            event.getBlock().setType(Material.DISPENSER);
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event){
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if(Base.getBaseByBlock(plugin, block) != null){
            Base base = Base.getBaseByBlock(plugin, block);
            if(base.players.contains(event.getPlayer().getUniqueId())) {
                HashMap<String, Object> blockInBase = new HashMap<>();
                blockInBase.put("location", block.getLocation());
                blockInBase.put("health", BlockTypes.blocks.getOrDefault(event.getBlock().getType(), 1.0));
                base.blocks.add(blockInBase);
                base.saveBase();
            }
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event){
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if(Base.getBaseByBlock(plugin, block) != null){
            Base base = Base.getBaseByBlock(plugin, block);
            if(!base.blocks.stream().map((value) -> (Location) value.get("location")).anyMatch((it) -> it.equals(event.getBlock().getLocation()))){
                event.setCancelled(true);
            }
            if(base.getBlockByLocation(block.getLocation()) != null) {
                if(base.players.contains(event.getPlayer().getUniqueId())) {
                    base.blocks.remove(base.getBlockByLocation(block.getLocation()));
                    base.saveBase();
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().setWalkSpeed(0.2f);
    }

    @EventHandler
    public void checkBlockHealth(PlayerInteractEvent event){
        if(event.getHand().equals(EquipmentSlot.HAND) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            Base base = Base.getBaseByBlock(plugin, event.getClickedBlock());
            if(base == null) return;
            Block block = event.getClickedBlock();
            HashMap<String, Object> hashMap = base.getBlockByLocation(block.getLocation());
            if(hashMap != null){
                int health = (int) Math.ceil((Double) hashMap.get("health"));
                int maxHealth = 1;
                if(BlockTypes.blocks.containsKey(((Location) hashMap.get("location")).getBlock().getType())) {
                    maxHealth = (int) Math.ceil(BlockTypes.blocks.get(((Location) hashMap.get("location")).getBlock().getType()));
                }
                event.getPlayer().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + String.valueOf(health) + "/" + String.valueOf(maxHealth) + " прочности"));
            }
        }
    }

    @EventHandler
    public void openBaseGui(PlayerInteractEvent event){
        if(event.getHand().equals(EquipmentSlot.HAND) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            Base base = Base.getBaseByLocation(plugin, event.getClickedBlock().getLocation());
            if(base == null) return;
            event.setCancelled(true);
            if(base.players.contains(event.getPlayer().getUniqueId()))
                plugin.guiManager.setGui(event.getPlayer(), new BaseGui(base));

        }
    }

    @EventHandler
    public void baseInteract(PlayerInteractEvent event){
        if(event.getPlayer().isSneaking()) return;
        if(event.getHand().equals(EquipmentSlot.HAND) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            Base base = Base.getBaseByBlock(plugin, event.getClickedBlock());
            if(base == null) return;
            if(event.getClickedBlock() == null) return;
            Player player = event.getPlayer();
            if(!base.players.contains(player.getUniqueId()) && BlockTypes.interactBlocks.contains(event.getClickedBlock().getType())){
                event.setCancelled(true);
                player.sendMessage(ChatColor.DARK_RED + "Вы не можете использовать это на чужой базе");
            }
        }
    }

    @EventHandler
    public void onTntExplosive(ExplosionPrimeEvent event){
        event.setRadius(1.5f);
        Base base = Base.getBaseByBlock(plugin, event.getEntity().getLocation().getBlock());
        event.setCancelled(true);
        if(base == null) return;
        Location location = event.getEntity().getLocation();
        location.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, location, 0, 0, 0, 0);
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.1f);
        int minX = (int) (location.getBlockX() - event.getRadius());
        int minY = (int) (location.getBlockY() - event.getRadius());
        int minZ = (int) (location.getBlockZ() - event.getRadius());
        int maxX = (int) (location.getBlockX() + event.getRadius());
        int maxY = (int) (location.getBlockY() + event.getRadius());
        int maxZ = (int) (location.getBlockZ() + event.getRadius());
        float damage = 10f;
        for(int x = minX; x <= maxX; x++){
            for(int y = minY; y <= maxY; y++){
                for(int z = minZ; z <= maxZ; z++){
                    Block blockTnt = (new Location(location.getWorld(), x, y , z).getBlock());
                    if(blockTnt.getType().equals(Material.TNT)){
                        blockTnt.getLocation().getWorld().spawnEntity(blockTnt.getLocation(), EntityType.PRIMED_TNT);
                        blockTnt.setType(Material.AIR);
                    }
                    if(base.getBlockByLocation(new Location(location.getWorld(), x, y, z)) != null && damage > 0){
                        HashMap<String, Object> hashMap = base.getBlockByLocation(new Location(location.getWorld(), x, y, z));
                        int index = base.blocks.indexOf(hashMap);
                        hashMap.replace("health", (double) hashMap.get("health") - damage);
                        for(int i = 0; i < base.blocks.size(); i++){
                            HashMap<String, Object> block = base.blocks.get(i);
                            if((double) block.get("health") <= 0.0){
                                base.blocks.remove(block);
                                if(((Location) block.get("location")).getBlock().getState() instanceof InventoryHolder){
                                    for(ItemStack item : ((InventoryHolder) ((Location) block.get("location")).getBlock().getState()).getInventory()){
                                        if(item != null) ((Location) block.get("location")).getWorld().dropItemNaturally(((Location) block.get("location")), item);
                                    }

                                }
                                ((Location) block.get("location")).getBlock().setType(Material.AIR);
                                i--;
                            }
                        }
                        base.saveBase();
                    }
                }
            }
        }
    }
}
