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
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.datafix.DataConverterRegistry;
import net.minecraft.world.EnumDifficulty;
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
import java.util.*;
import java.util.function.Consumer;

public class DungeonServer {
    private final DedicatedServer console;

    public DungeonServer() throws NoSuchFieldException {
        CraftServer server = (CraftServer) Bukkit.getServer();
        console = server.getServer();
    }

    public void createWorld(WorldCreator creator, Consumer<World> returnWorld) {
        String name = creator.name();
        ChunkGenerator generator = creator.generator();
        BiomeProvider biomeProvider = creator.biomeProvider();
        File folder = new File(Bukkit.getWorldContainer(), name);
        World world = Bukkit.getWorld(name);

        if (world != null) {
            returnWorld.accept(world);
            return;
        }

        if ((folder.exists()) && (!folder.isDirectory())) {
            throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
        }

        ResourceKey<WorldDimension> actualDimension = WorldDimension.b;

        Convertable.ConversionSession worldSession;
        try {
            worldSession = Convertable.a(Bukkit.getWorldContainer().toPath()).c(name, actualDimension);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        //MinecraftServer.convertWorld(worldSession); // Run conversion now

        RegistryReadOps<NBTBase> readOps = RegistryReadOps.a(DynamicOpsNBT.a, this.console.aB.i(), this.console.l);
        WorldDataServer worldData = (WorldDataServer) worldSession.a(readOps, this.console.datapackconfiguration);
        if (worldData == null) {
            Properties properties = new Properties();
            properties.put("generator-settings", creator.generatorSettings());
            properties.put("level-seed", Objects.toString(creator.seed()));
            properties.put("generate-structures", Objects.toString(creator.generateStructures()));
            properties.put("level-type", creator.type().getName());
            GeneratorSettings generatorsettings = GeneratorSettings.a(this.console.getCustomRegistry(), properties);
            WorldSettings worldSettings = new WorldSettings(name, EnumGamemode.a, false, EnumDifficulty.b, false, new GameRules(), this.console.datapackconfiguration);
            worldData = new WorldDataServer(worldSettings, generatorsettings, Lifecycle.stable());
        }

        worldData.checkName(name);
        worldData.a(this.console.getServerModName(), this.console.getModded().isPresent());
        long j = BiomeManager.a(creator.seed());
        List<MobSpawner> list = new ArrayList<>();
        RegistryMaterials<WorldDimension> registryMaterials = worldData.getGeneratorSettings().d();
        WorldDimension worlddimension = registryMaterials.a(actualDimension);
        DimensionManager dimensionmanager;
        net.minecraft.world.level.chunk.ChunkGenerator chunkGenerator;
        if (worlddimension == null) {
            dimensionmanager = this.console.l.d(IRegistry.P).d(DimensionManager.k);
            chunkGenerator = GeneratorSettings.a(this.console.l.d(IRegistry.aO), this.console.l.d(IRegistry.aH), (new Random()).nextLong());
        } else {
            dimensionmanager = worlddimension.b();
            chunkGenerator = worlddimension.c();
        }

        WorldInfo worldInfo = new CraftWorldInfo(worldData, worldSession, creator.environment(), dimensionmanager);
        if (biomeProvider == null && generator != null) {
            biomeProvider = generator.getDefaultBiomeProvider(worldInfo);
        }

        if (biomeProvider != null) {
            WorldChunkManager worldChunkManager = new CustomWorldChunkManager(worldInfo, biomeProvider, this.console.l.b(IRegistry.aO));
            if (chunkGenerator instanceof ChunkGeneratorAbstract) {
                chunkGenerator = new ChunkGeneratorAbstract(worldChunkManager, chunkGenerator.e, ((ChunkGeneratorAbstract) chunkGenerator).g);
            }
        }

        if (this.console.options.has("forceUpgrade")) {
            net.minecraft.server.Main.convertWorldButItWorks(actualDimension, net.minecraft.world.level.World.getDimensionKey(dimensionmanager), worldSession, DataConverterRegistry.a(), this.console.options.has("eraseCache"));
        }

        //String levelName = console.getDedicatedServerProperties().p;
        ResourceKey<net.minecraft.world.level.World> worldKey = ResourceKey.a(IRegistry.Q, new MinecraftKey(creator.key().getNamespace().toLowerCase(Locale.ENGLISH), creator.key().getKey().toLowerCase(Locale.ENGLISH)));
        if (generator == null || biomeProvider == null) {
            Bukkit.getLogger().info("Using custom biome provider");
        }
        WorldServer internal = new WorldServer(this.console, this.console.az, worldSession, worldData, worldKey, dimensionmanager, console.L.create(11), chunkGenerator, worldData.getGeneratorSettings().isDebugWorld(), j, creator.environment() == World.Environment.NORMAL ? list : ImmutableList.of(), true, creator.environment(), generator, biomeProvider);
        internal.getWorld().setKeepSpawnInMemory(false);
        this.console.initWorld(internal, worldData, worldData, worldData.getGeneratorSettings());
        internal.setSpawnFlags(false, false);
        this.console.R.put(internal.getDimensionKey(), internal);
        internal.G.tick();
        Bukkit.getPluginManager().callEvent(new WorldLoadEvent(internal.getWorld()));
        returnWorld.accept(internal.getWorld());
    }
}
