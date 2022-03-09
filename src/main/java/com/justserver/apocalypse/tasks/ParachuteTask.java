package com.justserver.apocalypse.tasks;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ParachuteTask extends BukkitRunnable {
    private final Player forPlayer;
    public static final HashMap<Player, ParachuteTask> tasks = new HashMap<>();
    public static final HashMap<Player, Long> lastSneak = new HashMap<>();

    public ParachuteTask(Player forPlayer){
        this.forPlayer = forPlayer;
        runTaskTimer(Apocalypse.getInstance(), 0, 10);
        tasks.put(forPlayer, this);
        lastSneak.remove(forPlayer);
    }
    int timer = 0;
    @Override
    public void run() {
        if(timer > 10000){
            cancel();
            return;
        } else if(forPlayer.isOnGround() || forPlayer.getFallDistance() < 2){
            cancel();
            return;
        }
        forPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 1, false, false));
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        tasks.remove(forPlayer);
        forPlayer.removePotionEffect(PotionEffectType.SLOW_FALLING);
    }
}
