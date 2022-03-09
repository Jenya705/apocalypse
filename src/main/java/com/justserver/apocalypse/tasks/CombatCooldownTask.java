package com.justserver.apocalypse.tasks;

import com.justserver.apocalypse.Apocalypse;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_17_R1.boss.CraftBossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CombatCooldownTask extends BukkitRunnable {
    private final Player forPlayer;
    public Player lastAttacker;
    private BossBar bossBar = null;
    public static HashMap<Player, CombatCooldownTask> tasks = new HashMap<>();
    public CombatCooldownTask(Player forPlayer, Player lastAttacker){
        this.forPlayer = forPlayer;
        this.lastAttacker = lastAttacker;
        this.bossBar = Bukkit.createBossBar("Вы в бою. Не выходите 20 секунд", BarColor.RED, BarStyle.SEGMENTED_20);
        this.bossBar.setProgress(1.0);
        bossBar.addPlayer(forPlayer);
        runTaskTimer(Apocalypse.getInstance(), 0, 20);
    }

    public Player getForPlayer() {
        return forPlayer;
    }
    int timer = 20;

    public void revoke(Player attacker){
        timer = 20;
        bossBar.setProgress(1.0);
        this.lastAttacker = attacker;
    }
    @Override
    public void run() {
         if(timer == 0){
             cancel();
             return;
         }
         timer--;
         bossBar.setTitle("Вы в бою. Не выходите " + timer + " секунд");
         if((bossBar.getProgress() - 0.05) < 0){
             bossBar.setProgress(0);
             bossBar.removeAll();
             tasks.remove(forPlayer);
             cancel();
             return;
         }
         bossBar.setProgress(bossBar.getProgress() - 0.05);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        bossBar.setProgress(0.0);
        bossBar.removeAll();
        tasks.remove(forPlayer);
    }
}
