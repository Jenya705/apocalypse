package com.justserver.apocalypse.items.guns;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;
import com.justserver.apocalypse.items.ItemRarity;
import net.minecraft.world.level.block.BlockDoor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class FlyingAxe extends Item {

    public FlyingAxe(Apocalypse plugin) {
        super(plugin);
        minIronNuggets = 6;
        maxIronNuggets = 10;
    }

    private final HashMap<UUID, ItemStack> thrownAxes = new HashMap<>();

    public HashMap<UUID, ItemStack> getThrownAxes() {
        return thrownAxes;
    }

    public void removePlayer(UUID uuid) {
        thrownAxes.remove(uuid);
    }

    @Override
    public String customName() {
        return "Метательный Топор";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        if (this.plugin == null) {
            this.plugin = Apocalypse.getPlugin(Apocalypse.class);
        }
        if(event.getItem() == null) return;
        if (event.getAction().name().contains("RIGHT_CLICK") && Objects.equals(event.getHand(), EquipmentSlot.HAND)) {
            Player player = event.getPlayer();
            if (thrownAxes.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Вы не можете пускать несколько топоров");
                return;
            }
            Damageable damageable = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
            int damage = damageable.getDamage() + 10;
            damageable.setDamage(damage);
            if (damage >= getMaterial().getMaxDurability()) {
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 0.9f);
                event.getItem().setType(Material.AIR);
                return;
            }
            event.getItem().setItemMeta(damageable);
            ArmorStand armorStand = event.getPlayer().getWorld().spawn(player.getEyeLocation().add(0, -0.3, 0), ArmorStand.class);
            armorStand.setVisible(false);
            armorStand.setArms(true);
            armorStand.setMarker(true);
            armorStand.setGravity(false);
            armorStand.setInvulnerable(true);

            armorStand.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_AXE));

            thrownAxes.put(player.getUniqueId(), player.getInventory().getItemInMainHand());

            Location destination = player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().multiply(20));
            Vector vector = destination.subtract(player.getEyeLocation()).toVector();
            new BukkitRunnable() {
                final int distance = 40;
                int counter = 0;

                @Override
                public void run() {

                    EulerAngle rotation = armorStand.getRightArmPose();
                    EulerAngle newRotation = rotation.add(20, 0, 0);
                    armorStand.setRightArmPose(newRotation);
                    armorStand.teleport(armorStand.getLocation().clone().add(vector.normalize()));

                    if (checkBlock(armorStand.getTargetBlockExact(1))) {
                        if (!armorStand.isDead()) {
                            armorStand.remove();
                            thrownAxes.remove(player.getUniqueId());
                            cancel();
                        }
                    }

                    for (Entity entity : armorStand.getChunk().getEntities()) {
                        if (!armorStand.isDead()) {
                            if (armorStand.getLocation().distanceSquared(entity.getLocation().add(0, entity.getHeight() / 2, 0)) <= entity.getHeight() / 2) {
                                if (!entity.equals(player) && !entity.equals(armorStand)) {
                                    if (entity instanceof LivingEntity livingEntity) {
                                        livingEntity.damage(4.0 * (Item.rarityUpgraded(event.getItem()) ? 1.3 : 1), player);
                                        entity.getWorld().playSound(entity.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 1f, 0.3f);
                                        armorStand.remove();
                                        thrownAxes.remove(player.getUniqueId());
                                        cancel();
                                    }
                                }
                            }
                        }
                    }
                    if (counter > distance) {
                        armorStand.remove();
                        thrownAxes.remove(player.getUniqueId());
                        cancel();
                        return;
                    }
                    counter++;
                }
            }.runTaskTimer(this.plugin, 0L, 1L);
        }
    }

    public boolean checkBlock(Block block){
        if(block instanceof Door door){
            return !door.isOpen();
        }
        return block != null && !block.isPassable();
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


}
