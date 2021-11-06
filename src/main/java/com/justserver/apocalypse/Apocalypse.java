package com.justserver.apocalypse;

import com.justserver.apocalypse.base.Base;
import com.justserver.apocalypse.base.BaseCommand;
import com.justserver.apocalypse.base.BaseHandler;
import com.justserver.apocalypse.commands.AddItemCommand;
import com.justserver.apocalypse.commands.DungeonCommand;
import com.justserver.apocalypse.commands.SetupCommand;
import com.justserver.apocalypse.dungeons.DungeonHandler;
import com.justserver.apocalypse.gui.GuiManager;
import com.justserver.apocalypse.gui.sign.SignMenuFactory;
import com.justserver.apocalypse.items.GunHandler;
<<<<<<< HEAD
import com.justserver.apocalypse.items.normal.Radio;
=======
import com.justserver.apocalypse.message.CubicMessageBuilder;
import com.justserver.apocalypse.message.DefaultMessageHandler;
>>>>>>> add CubicMessageBuilder
import com.justserver.apocalypse.overworld.OverworldHandler;
import com.justserver.apocalypse.protection.BlacklistedItemsHandler;
import com.justserver.apocalypse.setup.SetupManager;
import com.justserver.apocalypse.tasks.ChestLootTask;
import com.justserver.apocalypse.utils.CustomConfiguration;
<<<<<<< HEAD
import org.bukkit.*;
import org.bukkit.block.Chest;
=======
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
>>>>>>> add CubicMessageBuilder
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.*;

public final class Apocalypse extends JavaPlugin implements Listener {

    public CustomConfiguration bases;
    //  CustomConfiguration lockers = new CustomConfiguration(this, "lockers.yml"); DISABLED FOR RENOVATIONS
    public GuiManager guiManager = new GuiManager();
    public ArrayList<Base> loadedBases = new ArrayList<>();
    private static Apocalypse instance;
    private static final SetupManager setup = new SetupManager();
    public SignMenuFactory signMenuFactory;

    public List<Location> fires = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        this.signMenuFactory = new SignMenuFactory(this);
        bases = new CustomConfiguration(this, "bases.yml");
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                for(Location location : fires){
                    location.getBlock().setType(Material.AIR);
                }
            }
        }, 0, 300L);
        Registry.init(this);
        getCommand("startdungeon").setExecutor(new DungeonCommand(this));
        getCommand("base").setExecutor(new BaseCommand(this));
        getCommand("additem").setExecutor(new AddItemCommand());
        getCommand("setup").setExecutor(new SetupCommand());
        initEvents(true);
        for(String key : bases.config.getConfigurationSection("bases").getKeys(false)){
            Base base = new Base(this);
            base.id = bases.config.getString("bases." + key + ".id");
            if(base.id == null) continue;
            base.location = bases.config.getLocation("bases." + key + ".location");
            if(base.location == null) continue;
            String owner = bases.config.getString("bases." + key + ".owner");
            if(owner == null) continue;
            base.owner = UUID.fromString(owner);

            base.frequency = bases.config.getString("bases." + key + ".frequency");
            List<String> stringUUIDs = bases.config.getStringList("bases." + key + ".players");
            for(String stringUUID : stringUUIDs){
                base.players.add(UUID.fromString(stringUUID));
            }
            List blocks = bases.config.getList("bases." + key + ".blocks");
            if(blocks == null) continue;
            base.blocks = (ArrayList<HashMap<String, Object>>) blocks;
            base.duration = Long.parseLong(bases.config.get("bases." + key + ".duration").toString());
            //System.out.println(base.duration);
            loadedBases.add(base);
        }
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            try {
                for(Base loadedBase : loadedBases) {
                    if(loadedBase.duration == -1) continue;
                    if (loadedBase.duration <= 0) {
                        loadedBases.remove(loadedBase);
                        loadedBase.remove();

                        return;
                    }
                    loadedBase.duration -= 1000L;

                }
            } catch (ConcurrentModificationException ignored){}

        }, 0, 20);
        try {
        for (Player player : Bukkit.getOnlinePlayers()){

                Arrays.stream(player.getInventory().getContents())
                        .filter(Objects::nonNull)
                        .filter(itemStack -> Registry.getItemByItemstack(itemStack) instanceof Radio)
                        .forEach(itemStack -> loadedBases.stream()
                                .filter(base -> base.frequency.equals(itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(this, "frequency"), PersistentDataType.STRING)))
                                .forEach(base -> base.connectedPlayers.add(player)));


        }
        } catch (NullPointerException ignored){}
    }


    @Override
    public void onDisable() {

        for(Base base : loadedBases){
            base.saveBase();
        }
        World lastWorld = null;
        for(Map.Entry<UUID, ItemStack> entry : Registry.FLYING_AXE.getThrownAxes().entrySet()){
            if(Bukkit.getPlayer(entry.getKey()) != null){
                Bukkit.getPlayer(entry.getKey()).getInventory().addItem(entry.getValue());
            }
            lastWorld = Bukkit.getPlayer(entry.getKey()).getWorld();
        }
        if(lastWorld == null) return;
        for(Entity entity : lastWorld.getEntities()){
            if(entity instanceof ArmorStand){
                entity.remove();
            }
        }

        for(Map.Entry<UUID, ChestLootTask> entry : OverworldHandler.chestLootTasks.entrySet()){
            entry.getValue().cancel();
            entry.getValue().getChest().getBlockInventory().clear();
            Player player = Bukkit.getPlayer(entry.getKey());
            if(player != null){
                player.sendMessage(ChatColor.YELLOW + "Похоже, что админ перезагрузил сервер пока вы лутали сундук и он закрылся. Просто заново зайдите :)");
                player.closeInventory();
            }

        }
    }
    public void initEvents(boolean startup){
        for(Player player : Bukkit.getOnlinePlayers()){
            setup.exitSetup(player, startup);
        }
        uninit();
        getServer().getPluginManager().registerEvents(
                new DefaultMessageHandler(new CubicMessageBuilder(this)), this);
        Bukkit.getPluginManager().registerEvents(new DungeonHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new GunHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new BaseHandler(this), this);
        Bukkit.getPluginManager().registerEvents(guiManager, this);
        Bukkit.getPluginManager().registerEvents(new OverworldHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new BlacklistedItemsHandler(), this);
    }

    public void uninit(){
        HandlerList.unregisterAll((Plugin) this);
    }

    public static void initSetup(){
        instance.uninit();
        Bukkit.getPluginManager().registerEvents(setup, instance);
    }

    public static Apocalypse getInstance() {
        return instance;
    }

    public static SetupManager getSetup() {
        return setup;
    }
}
