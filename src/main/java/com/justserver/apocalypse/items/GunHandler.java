package com.justserver.apocalypse.items;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.gui.ModifiactionsGui;
import com.justserver.apocalypse.items.guns.modifications.Modify;
import com.justserver.apocalypse.items.guns.modifications.Scope;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class GunHandler implements Listener {

    public final Apocalypse plugin;

    public GunHandler(Apocalypse plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void modifyGun(PlayerInteractEvent event) throws NoSuchFieldException, IllegalAccessException {
        if (event.getHand().equals(EquipmentSlot.HAND) && event.getAction().equals(Action.LEFT_CLICK_AIR)) {
            Player player = event.getPlayer();
            if (event.getItem() == null) return;
            if (Registry.getItemByItemstack(event.getItem()) instanceof Gun && player.isSneaking() && !player.hasPotionEffect(PotionEffectType.SLOW)) {
                plugin.guiManager.setGui(player, new ModifiactionsGui(player.getInventory().getItemInMainHand(), plugin));
            } else if (Registry.getItemByItemstack(player.getInventory().getItemInMainHand()) instanceof Gun) {
                List<String> modifications = Modify.getModifications(plugin, player.getInventory().getItemInMainHand());
                for (String id : modifications) {
                    Modify modify = (Modify) Registry.class.getDeclaredField(id).get(Registry.class);
                    modify.onInteract(event);
                }
            }
        }
    }

    @EventHandler
    public void modifyOff(PlayerItemHeldEvent event) {
        if (Scope.inScope.contains(event.getPlayer())) {
            Scope.inScope.remove(event.getPlayer());
            event.getPlayer().removePotionEffect(PotionEffectType.SLOW);
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
        if (Registry.getItemByItemstack(player.getInventory().getItemInMainHand()) instanceof Gun) {
            ItemStack item = player.getInventory().getItemInMainHand();
        }
    }
}
