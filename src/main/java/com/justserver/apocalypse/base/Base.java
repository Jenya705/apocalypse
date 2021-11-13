package com.justserver.apocalypse.base;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.utils.CustomConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Base {
    private final Apocalypse plugin;
    public ArrayList<UUID> players = new ArrayList<>();
    public UUID owner;
    public String id;
    public Location location;
    public ArrayList<HashMap<String, Object>> blocks = new ArrayList<>();
    public long duration;
    public String frequency = "0000";
    public ArrayList<Player> connectedPlayers = new ArrayList<>();

    public Base(Apocalypse plugin) {
        this.plugin = plugin;
        this.duration = 30 * 60 * 1000;
    }

    public HashMap<String, Object> getBlockByLocation(Location location) {
        for (HashMap<String, Object> hashMap : this.blocks) {
            if (location.equals(hashMap.get("location"))) {
                return hashMap;
            }
        }
        return null;
    }

    public HashMap<Material, Integer> getPriceForDuration() {
        HashMap<Material, Integer> price = new HashMap<>();
        price.put(Material.OAK_PLANKS, 10);
        for (HashMap<String, Object> hashMap : this.blocks) {
            Location location = (Location) hashMap.get("location");
            if (location.getBlock().getType().equals(Material.BRICKS)) {
                if (price.containsKey(Material.BRICK)) {
                    price.put(Material.BRICK, price.get(Material.BRICK) + 1);
                } else {
                    price.put(Material.BRICK, 1);
                }
            } else if (location.getBlock().getType().equals(Material.OAK_PLANKS)) {
                if (price.containsKey(Material.OAK_PLANKS)) {
                    price.put(Material.OAK_PLANKS, price.get(Material.OAK_PLANKS) + 1);
                } else {
                    price.put(Material.OAK_PLANKS, 1);
                }
            }
        }
        for (Map.Entry<Material, Integer> entry : price.entrySet()) {
            price.put(entry.getKey(), (int) Math.ceil(entry.getValue() / 2f));
            if (price.get(entry.getKey()) == 0) {
                price.remove(entry.getKey());
            }
        }
        return price;
    }

    public static void delete(Base base) {
        Apocalypse plugin = Apocalypse.getInstance();
        if (plugin.bases.config.contains("bases." + base.id)) {
            base.location.getBlock().setType(Material.AIR);
            for (HashMap<String, Object> blockProperty : base.blocks) {
                Location blockLocation = (Location) blockProperty.get("location");
                if (BlockTypes.blocks.containsKey(blockLocation.getBlock().getType())) {
                    blockLocation.getBlock().breakNaturally(true);
                }
            }
            plugin.bases.config.set("bases." + base.id, null);
            plugin.bases.save();
            plugin.bases.reload();
        }
        plugin.unloadBase(base);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.unloadBase(base);
        }, 10);
    }

    @Deprecated
    public void remove() {
        for (HashMap<String, Object> hashMap : this.blocks) {
            if (hashMap.containsKey("location")) continue;
            ((Location) hashMap.get("location")).getBlock().setType(Material.AIR);
        }
        if (this.location == null) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (this.location != null) {
                    this.location.getBlock().setType(Material.AIR);
                }
                ArrayList<Base> foundBases = new ArrayList<>();
                plugin.loadedBases.stream().filter(base1 -> id.equals(base1.id)).forEach(base1 -> {
                    if (plugin.bases.config.contains("bases." + base1.id)) {
                        plugin.bases.config.set("bases." + base1.id, null);
                        plugin.bases.save();
                        plugin.bases.reload();
                        foundBases.add(base1);
                        base1.location.getBlock().setType(Material.AIR);
                    }
                });
                for (Base found : foundBases) {
                    for (HashMap<String, Object> blockProperty : found.blocks) {
                        Location blockLocation = (Location) blockProperty.get("location");
                        if (BlockTypes.blocks.containsKey(blockLocation.getBlock().getType())) {
                            blockLocation.getWorld().dropItemNaturally(blockLocation, new ItemStack(blockLocation.getBlock().getType()));
                            blockLocation.getBlock().setType(Material.AIR);
                        }
                    }
                    plugin.unloadBase(found);
                }
                plugin.bases.config.set("bases." + this.id, null);
                try {
                    plugin.unloadBase(this);
                } catch (ConcurrentModificationException ignored) {
                }
                plugin.bases.save();
            }, 100);
        } // ArrayList<Base> foundBases = new ArrayList<>();

//        ArrayList<Base> foundBases = new ArrayList<>();
//        plugin.loadedBases.stream().filter(base1 -> id.equals(base1.id)).forEach(base1 -> {
//            if(plugin.bases.config.contains("bases." + base1.id)){
//                plugin.bases.config.set("bases." + base1.id, null);
//                plugin.bases.save();
//                plugin.bases.reload();
//                foundBases.add(base1);
//                base1.location.getBlock().setType(Material.AIR);
//            }
//        });
//        for(Base found : foundBases){
//            for(HashMap<String, Object> blockProperty : found.blocks){
//                Location blockLocation = (Location) blockProperty.get("location");
//                if(BlockTypes.blocks.containsKey(blockLocation.getBlock().getType())){
//                    blockLocation.getBlock().setType(Material.AIR);
//                }
//            }
//            plugin.unloadBase(found);
//        }

//        plugin.bases.config.set("bases." + this.id, null);
//        plugin.unloadBase(this);
//        plugin.bases.save();
    }

    public Apocalypse getPlugin() {
        return plugin;
    }

    @SuppressWarnings("unchecked")
    public void saveBase() {
        CustomConfiguration config = plugin.bases;
        for (Field field : this.getClass().getFields()) {
            try {
                Object value = field.get(this);
                if (field.getName().equals("blocks")) {

                } else if (value instanceof ArrayList) {
                    ArrayList<String> stringValue = new ArrayList<>();
                    for (Object object : (ArrayList<Object>) value) {
                        stringValue.add(object.toString());
                    }
                    value = stringValue;
                } else if (value instanceof Location) {
                } else if (field.getType().equals(Instant.class)) {
                    value = ((Instant) value).getEpochSecond();
                } else if (!value.getClass().isPrimitive()) {
                    value = value.toString();
                } else {
                    value = value.getClass().cast(field.getClass());
                }

                config.config.set("bases." + this.id + "." + field.getName(), value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        config.save();
        plugin.unloadBase(this);
        plugin.loadedBases.add(this);
    }

    public void addPlayer(UUID uuid) {
        this.players.add(uuid);
        saveBase();
    }

    public void removePlayer(UUID uuid) {
        this.players.remove(uuid);
        saveBase();
    }

    public void setOwner(UUID uuid) {
        this.owner = uuid;
        saveBase();
    }

    public static Base getBaseById(Apocalypse plugin, String id) {
        for (Base base : plugin.loadedBases) {
            if (base.id.equals(id)) {
                return base;
            }
        }
        return null;
    }

    public static Base getBaseByLocation(Apocalypse plugin, Location location) {
        for (Base base : plugin.loadedBases) {
            if (base.location.equals(location)) {
                return base;
            }
        }
        return null;
    }

    public static Base getBaseByBlock(Apocalypse plugin, Block block) {
        for (Base base : plugin.loadedBases) {
            if (base.location == null) continue;
            int minX = base.location.getBlockX() - 15;
            int minY = base.location.getBlockY() - 15;
            int minZ = base.location.getBlockZ() - 15;
            int maxX = base.location.getBlockX() + 15;
            int maxY = base.location.getBlockY() + 15;
            int maxZ = base.location.getBlockZ() + 15;
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        if (block.getLocation().equals(new Location(block.getWorld(), x, y, z))) {
                            return base;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void createBase(Apocalypse plugin, Player owner) {
        Base base = new Base(plugin);
        base.id = UUID.randomUUID().toString();
        base.owner = owner.getUniqueId();
        base.players.add(owner.getUniqueId());
        base.location = owner.getLocation().getBlock().getLocation();
        base.location.getBlock().setType(Material.SMITHING_TABLE);
        base.saveBase();
        plugin.loadedBases.add(base);
        plugin.bases.reload();
    }

    public static List<Base> getBasesByPlayer(Apocalypse plugin, Player player) {
        return plugin.loadedBases.stream().filter(base -> base.owner.equals(player.getUniqueId())).collect(Collectors.toList());
    }
}
