package com.justserver.apocalypse.items;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.guns.modifications.Modify;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.security.SecureRandom;
import java.util.*;


public abstract class Gun extends Item {
    private final double damage;
    private final double range;
    private final double speed;
    private final int initialAmmoCount;
    private final boolean isTriple;
    protected final ArrayList<UUID> cooldowns = new ArrayList<>();
    public Gun(Apocalypse plugin, boolean isTriple, double damage, double range, double speed, int initialAmmoCount) {
        super(plugin);
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.initialAmmoCount = initialAmmoCount;
        this.isTriple = isTriple;
    }

    public double getDamage() {
        return damage;
    }

    public double getRange() {
        return range;
    }
    public int getCooldown() {
        return (int)getSpeed();
    }

    public double getSpeed() {
        return speed;
    }
    public abstract int getRechargeTime();

    private final Random random = new Random();

    @Override
    public Material getMaterial() {
        return Material.CROSSBOW;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        if(plugin == null){
            plugin = Apocalypse.getPlugin(Apocalypse.class);
        }
        if(event.getAction().name().contains("RIGHT")){

            Player player = event.getPlayer();
            if(cooldowns.contains(player.getUniqueId()) && getCooldown() != 0){
                event.setCancelled(true);
                return;
            }
            if(!event.getHand().equals(EquipmentSlot.HAND)){
                event.setCancelled(true);
                return;
            }
            int currentAmmo = 0;
            try {

                currentAmmo = event.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "ammo_count"), PersistentDataType.INTEGER);
                if (currentAmmo == 0) {
                    return;
                }
            } catch (Exception exception){
                player.sendMessage(exception.toString());
                return;
            }

            event.setCancelled(true);
            ItemMeta meta = event.getItem().getItemMeta();
            if((currentAmmo - 1) == 0){
                ((CrossbowMeta)meta).setChargedProjectiles(new ArrayList<>());
            }
            if(!cooldowns.contains(player.getUniqueId()) && getCooldown() != 0){
                cooldowns.add(player.getUniqueId());
                Bukkit.getScheduler().runTaskLater(plugin, () -> cooldowns.remove(player.getUniqueId()), getCooldown());
            }

            int knockback = 8;
            if(Modify.checkModifications(plugin, event.getItem(), "GRIP")){
                knockback /= 2;
            }
            player.getWorld().playSound(player.getEyeLocation(), Sound.ITEM_CROSSBOW_SHOOT, 1f,0.3f);
            if(!Modify.checkModifications(plugin, event.getItem(), "SILENCER")){
                player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.7f,0.4f);
            }else{
                player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_IRON_GOLEM_DAMAGE, 1f,0.1f);
            }
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "ammo_count"), PersistentDataType.INTEGER, currentAmmo - 1);
            event.getItem().setItemMeta(meta);
            //player.getWorld().playSound(player.getEyeLocation(), Sound.ITEM_CROSSBOW_SHOOT, 1f,0.5f);
            shot(player, 0);
            if(isTriple){
                shot(player, 100);
                shot(player, -100);
            }
            Location newLoc = player.getLocation();
            newLoc.setPitch(player.getEyeLocation().getPitch() + Math.abs(random.nextFloat()));
            newLoc.setPitch(player.getEyeLocation().getPitch() + -Math.abs(random.nextFloat() * knockback));
            newLoc.setYaw(player.getEyeLocation().getYaw() + (random.nextFloat() * (random.nextBoolean() ? 1 : -1)));
            player.teleport(newLoc);
        }
    }

    public void shot(Player player, double angle){
        Location origin = player.getEyeLocation();
        Vector direction = origin.getDirection();
        if(angle != 0){
            direction.rotateAroundY(angle);
        }
        direction.multiply(getRange());
        direction.normalize();
        Location destination = null;
        for (int i = 0; i < getRange(); i++) {
            Location location = origin.add(direction);
            if(!location.getBlock().isPassable() || location.getBlock().getType().equals(Material.WATER)) {
                if(location.getBlock().getType().equals(Material.TNT)){
                    location.getBlock().setType(Material.AIR);
                    location.getWorld().spawn(location.clone().add(0.5, 0, 0.5), TNTPrimed.class);
                }
                spawnParticles(destination);
                break;
            }
            //location.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, location, 3);
            Collection<LivingEntity> nearby = location.getNearbyEntitiesByType(LivingEntity.class, 0.4);
            for(LivingEntity shot : nearby){

                if(shot instanceof Player){
                    if(shot.getUniqueId().equals(player.getUniqueId())) continue;
                    shot.damage(isHeadShot((Player)shot, location) ? getDamage() * 3 : getDamage(), player); // TODO если у игрока каска отменить хедшот
                } else {
                    shot.damage(getDamage(), player);
                }
                spawnParticles(location);
                return;
            }
            if(!Modify.checkModifications(plugin, player.getInventory().getItemInMainHand(), "SILENCER")){
                player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.8f,0.4f);
            }else{
                player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_IRON_GOLEM_DAMAGE, 0.1f,0.1f);
            }
            destination = location;
        }
        if(destination != null){
            spawnParticles(destination);
        }
    }

    public void spawnParticles(Location location){
        location.getWorld().spawnParticle(Particle.FLAME, location, 10, 0, 0, 0, 0.02);
    }

    public static boolean isHeadShot(Player victim, Location location) {

        Location locA = new Location(victim.getWorld(), victim.getEyeLocation().getX() -0.5, victim.getEyeLocation().getY() - 0.5, victim.getEyeLocation().getZ() - 0.5);
        Location locB = new Location(victim.getWorld(), victim.getEyeLocation().getX() +0.5, victim.getEyeLocation().getY() + 0.5, victim.getEyeLocation().getZ() + 0.5);
        return isInCuboid(locA, locB, location);
    }

    public static boolean isInCuboid(Location min, Location max, Location varying) {
        double[] locs = new double[2];
        locs[0] = min.getX();
        locs[1] = max.getX();
        Arrays.sort(locs);
        if (varying.getX() > locs[1] || varying.getX() < locs[0])
            return false;
        locs[0] = min.getY();
        locs[1] = max.getY();
        Arrays.sort(locs);
        if (varying.getY() > locs[1] || varying.getY() < locs[0])
            return false;
        locs[0] = min.getZ();
        locs[1] = max.getZ();
        Arrays.sort(locs);
        return !(varying.getZ() > locs[1]) && !(varying.getZ() < locs[0]);
    }

    @Override
    public ItemStack createItemStack(Apocalypse plugin) {
        ItemStack is = super.createItemStack(plugin);
        ItemMeta meta = is.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "ammo_count"), PersistentDataType.INTEGER, 0);
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "modifications"), PersistentDataType.STRING, "");
        meta.addEnchant(Enchantment.QUICK_CHARGE, getRechargeTime(), true);
        if(isTriple()){
            System.out.println(isTriple());
            meta.addEnchant(Enchantment.MULTISHOT, 1, false);
        }
        is.setItemMeta(meta);
        return is;
    }

    public int getInitialAmmoCount() {
        return initialAmmoCount;
    }

    public boolean isTriple() {return isTriple;}

    @Override
    public ItemStack createItemStack(Apocalypse plugin, int damage) {
        return createItemStack(plugin);
    }
}
