package com.justserver.apocalypse.items.guns;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.*;

public class FlyingAxe extends Item {

    public FlyingAxe(Apocalypse plugin) {
        super(plugin);
    }

    private final HashMap<UUID, ItemStack> thrownAxes = new HashMap<>();

    public HashMap<UUID, ItemStack> getThrownAxes() {
        return thrownAxes;
    }

    public void removePlayer(UUID uuid){
        thrownAxes.remove(uuid);
    }

    @Override
    public String customName() {
        return "Метательный Топор";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
            if(this.plugin == null) {
                this.plugin = Apocalypse.getPlugin(Apocalypse.class);
            }
        if(event.getAction().name().contains("RIGHT_CLICK") && Objects.equals(event.getHand(), EquipmentSlot.HAND)) {
            Player player = event.getPlayer();
            if(thrownAxes.containsKey(player.getUniqueId())){
                player.sendMessage(ChatColor.RED + "Вы не можете пускать несколько топоров");
                return;
            }
            ArmorStand armorStand = event.getPlayer().getWorld().spawn(player.getEyeLocation().add(0, -0.3, 0), ArmorStand.class);
            armorStand.setVisible(false);
            armorStand.setArms(true);
            armorStand.setMarker(true);
            armorStand.setGravity(false);
            armorStand.setInvulnerable(true);

            armorStand.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_AXE));
            int returnSlot = player.getInventory().getHeldItemSlot();
            final int damage = ((Damageable) player.getInventory().getItemInMainHand().getItemMeta()).getDamage() + 1;
            thrownAxes.put(player.getUniqueId(), player.getInventory().getItemInMainHand());
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

            Location destination = player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().multiply(20));
            Vector vector = destination.subtract(player.getEyeLocation()).toVector();
            new BukkitRunnable() {
                int distance = 20;
                int counter = 0;
                @Override
                public void run() {

                    EulerAngle rotation = armorStand.getRightArmPose();
                    EulerAngle newRotation = rotation.add(20, 0, 0);
                    armorStand.setRightArmPose(newRotation);
                    armorStand.teleport(armorStand.getLocation().clone().add(vector.normalize()));

                    if(armorStand.getTargetBlockExact(1) != null && !armorStand.getTargetBlockExact(1).isPassable()){
                        if(!armorStand.isDead()){
                            armorStand.remove();
                            if(player.getInventory().firstEmpty() != -1){
                                player.getInventory().setItem(returnSlot, Registry.FLYING_AXE.createItemStack(plugin, damage - 1));
                            } else {
                                player.getWorld().dropItemNaturally(player.getLocation(), Registry.FLYING_AXE.createItemStack(plugin, damage - 1));
                            }
                            thrownAxes.remove(player.getUniqueId());
                            cancel();
                        }
                    }

                    for(Entity entity : armorStand.getChunk().getEntities()){
                        if(!armorStand.isDead()) {
                            if(armorStand.getLocation().distanceSquared(entity.getLocation().add(0, entity.getHeight() / 2, 0)) <= entity.getHeight() / 2){
                                if(!entity.equals(player) && !entity.equals(armorStand)){
                                    if(entity instanceof LivingEntity){
                                        LivingEntity livingEntity = (LivingEntity) entity;
                                        livingEntity.damage(5.0, player);
                                        entity.getWorld().playSound(entity.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 1f, 0.3f);
                                        armorStand.remove();
                                        if(damage >= getMaterial().getMaxDurability()){
                                            entity.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 0.9f);
                                            return;
                                        }
                                        if(player.getInventory().firstEmpty() != -1){
                                            player.getInventory().setItem(returnSlot, Registry.FLYING_AXE.createItemStack(plugin, damage));
                                        } else {
                                            player.getWorld().dropItemNaturally(player.getLocation(), Registry.FLYING_AXE.createItemStack(plugin, damage));
                                        }
                                        thrownAxes.remove(player.getUniqueId());
                                        cancel();
                                    }
                                }
                            }
                        }
                    }
                    if(counter > distance){
                        armorStand.remove();
                        if(player.getInventory().firstEmpty() != -1){
                            player.getInventory().addItem(Registry.FLYING_AXE.createItemStack(plugin));
                        } else {
                            player.getWorld().dropItemNaturally(player.getLocation(), Registry.FLYING_AXE.createItemStack(plugin));
                        }
                        thrownAxes.remove(player.getUniqueId());
                        cancel();
                        return;
                    }
                    counter++;
                }
            }.runTaskTimer(this.plugin, 0L, 1L);
        }
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_AXE;
    }

    @Override
    public double getLeftDamage() {
        return 6;
    }

    @Override
    public int getSlowdown() {
        return 10;
    }

    @Override
    protected void init() {

    }
}
