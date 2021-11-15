package com.justserver.apocalypse.dungeons;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.IRegistry;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.RegistryReadOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.datafix.DataConverterRegistry;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.npc.MobSpawnerCat;
import net.minecraft.world.entity.npc.MobSpawnerTrader;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.MobSpawner;
import net.minecraft.world.level.WorldSettings;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.dimension.DimensionManager;
import net.minecraft.world.level.dimension.WorldDimension;
import net.minecraft.world.level.levelgen.ChunkGeneratorAbstract;
import net.minecraft.world.level.levelgen.GeneratorSettings;
import net.minecraft.world.level.levelgen.MobSpawnerPatrol;
import net.minecraft.world.level.levelgen.MobSpawnerPhantom;
import net.minecraft.world.level.storage.Convertable;
import net.minecraft.world.level.storage.WorldDataServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.generator.CraftWorldInfo;
import org.bukkit.craftbukkit.v1_17_R1.generator.CustomWorldChunkManager;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class DungeonServer {
    private DedicatedServer console = null;
    public DungeonServer() throws NoSuchFieldException {
        CraftServer server = (CraftServer) Bukkit.getServer();
        console = server.getServer();
    }

    public World createWorld(WorldCreator creator) {
        String name = creator.name();
        ChunkGenerator generator = creator.generator();
        BiomeProvider biomeProvider = creator.biomeProvider();
        File folder = new File(Bukkit.getWorldContainer(), name);
        World world = Bukkit.getWorld(name);

        if (world != null) {
            return world;
        }

        if ((folder.exists()) && (!folder.isDirectory())) {
            throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
        }

        if (generator == null) {
            generator = creator.generator();
        }

        if (biomeProvider == null) {
            biomeProvider = creator.biomeProvider();
        }

        ResourceKey<WorldDimension> actualDimension = WorldDimension.b;

        Convertable.ConversionSession worldSession;
        try {
            worldSession = Convertable.a(Bukkit.getWorldContainer().toPath()).c(name, actualDimension);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        MinecraftServer.convertWorld(worldSession); // Run conversion now

        boolean hardcore = false;
        RegistryReadOps<NBTBase> registryreadops = RegistryReadOps.a(DynamicOpsNBT.a, this.console.aB.i(), this.console.l);
        WorldDataServer worlddata = (WorldDataServer)worldSession.a(registryreadops, this.console.datapackconfiguration);
        if (worlddata == null) {
            Properties properties = new Properties();
            properties.put("generator-settings", Objects.toString(creator.generatorSettings()));
            properties.put("level-seed", Objects.toString(creator.seed()));
            properties.put("generate-structures", Objects.toString(creator.generateStructures()));
            properties.put("level-type", Objects.toString(creator.type().getName()));
            GeneratorSettings generatorsettings = GeneratorSettings.a(this.console.getCustomRegistry(), properties);
            WorldSettings worldSettings = new WorldSettings(name, EnumGamemode.a, false, EnumDifficulty.b, false, new GameRules(), this.console.datapackconfiguration);
            worlddata = new WorldDataServer(worldSettings, generatorsettings, Lifecycle.stable());
        }

        worlddata.checkName(name);
        worlddata.a(this.console.getServerModName(), this.console.getModded().isPresent());
        long j = BiomeManager.a(creator.seed());
        List<MobSpawner> list = ImmutableList.of(new MobSpawnerPhantom(), new MobSpawnerPatrol(), new MobSpawnerCat(), new VillageSiege(), new MobSpawnerTrader(worlddata));
        RegistryMaterials<WorldDimension> registrymaterials = worlddata.getGeneratorSettings().d();
        WorldDimension worlddimension = (WorldDimension)registrymaterials.a(actualDimension);
        DimensionManager dimensionmanager;
        Object chunkgenerator;
        if (worlddimension == null) {
            dimensionmanager = (DimensionManager)this.console.l.d(IRegistry.P).d(DimensionManager.k);
            chunkgenerator = GeneratorSettings.a(this.console.l.d(IRegistry.aO), this.console.l.d(IRegistry.aH), (new Random()).nextLong());
        } else {
            dimensionmanager = worlddimension.b();
            chunkgenerator = worlddimension.c();
        }

        WorldInfo worldInfo = new CraftWorldInfo(worlddata, worldSession, creator.environment(), dimensionmanager);
        if (biomeProvider == null && generator != null) {
            biomeProvider = generator.getDefaultBiomeProvider(worldInfo);
        }

        if (biomeProvider != null) {
            WorldChunkManager worldChunkManager = new CustomWorldChunkManager(worldInfo, biomeProvider, this.console.l.b(IRegistry.aO));
            if (chunkgenerator instanceof ChunkGeneratorAbstract) {
                chunkgenerator = new ChunkGeneratorAbstract(worldChunkManager, ((net.minecraft.world.level.chunk.ChunkGenerator)chunkgenerator).e, ((ChunkGeneratorAbstract)chunkgenerator).g);
            }
        }

        if (this.console.options.has("forceUpgrade")) {
            net.minecraft.server.Main.convertWorldButItWorks(actualDimension, net.minecraft.world.level.World.getDimensionKey(dimensionmanager), worldSession, DataConverterRegistry.a(), this.console.options.has("eraseCache"));
        }

        String levelName = console.getDedicatedServerProperties().p;
        ResourceKey<net.minecraft.world.level.World> worldKey;
        if (name.equals(levelName + "_nether")) {
            worldKey = net.minecraft.world.level.World.g;
        } else if (name.equals(levelName + "_the_end")) {
            worldKey = net.minecraft.world.level.World.h;
        } else {
            worldKey = ResourceKey.a(IRegistry.Q, new MinecraftKey(creator.key().getNamespace().toLowerCase(Locale.ENGLISH), creator.key().getKey().toLowerCase(Locale.ENGLISH)));
        }

        WorldServer internal = new WorldServer(this.console, this.console.az, worldSession, worlddata, worldKey, dimensionmanager, console.L.create(11), (net.minecraft.world.level.chunk.ChunkGenerator)chunkgenerator, worlddata.getGeneratorSettings().isDebugWorld(), j, creator.environment() == World.Environment.NORMAL ? list : ImmutableList.of(), true, creator.environment(), generator, biomeProvider);
        this.console.initWorld(internal, worlddata, worlddata, worlddata.getGeneratorSettings());
        internal.setSpawnFlags(true, true);
        this.console.R.put(internal.getDimensionKey(), internal);
        console.loadSpawn(internal.getChunkProvider().a.z, internal);
        internal.G.tick();
        Bukkit.getPluginManager().callEvent(new WorldLoadEvent(internal.getWorld()));
        return internal.getWorld();
    }
}
