package com.justserver.apocalypse.dungeons;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.dungeons.rooms.StartRoom;
import com.justserver.apocalypse.utils.RelativeDirection;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 Dungeon rooms registry with all coordinates and responsible room handler classes
 @author MisterFunny01
 */
public enum RoomType {
    FIRST(StartRoom.class, null, new Doorway(5, 3, 16, RelativeDirection.FORWARD)),
    L(DungeonRoom.class, new Vector(4, 3, 0), new Doorway(0, 3, 4, RelativeDirection.LEFT)),
    //DOORWAY3X2(DungeonRoom.class, new Vector(2, 2, 0), new Doorway(7, 2, 8, RelativeDirection.FORWARD), new Doorway(11, 2, 4, RelativeDirection.RIGHT)),
    ZIGZAG(DungeonRoom.class, new Vector(5, 2, 0), new Doorway(9, 2, 10, RelativeDirection.FORWARD))
    ;
    private final File roomFile;
    private final Schematic schematic;
    private final Doorway[] outDoorways;
    private final Vector inDoorway;
    private final Class<? extends DungeonRoom> roomClass;

    RoomType(Class<? extends DungeonRoom> roomClass, Vector inDoorway, Doorway... outDoorways) {
        Schematic schematic1;
        this.roomFile = new File(Apocalypse.getInstance().getDataFolder().getAbsolutePath() + File.separator + "maps" + File.separator + this.name().toLowerCase() + ".room");
        try {
            schematic1 = Schematic.loadSchematic(roomFile);
        } catch (IOException e) {
            schematic1 = null;
        }
        this.schematic = schematic1;
        this.inDoorway = inDoorway;
        this.outDoorways = outDoorways;
        this.roomClass = roomClass;
    }

    public File getRoomFile() {
        return roomFile;
    }

    public DungeonRoom paste(Location location, BlockFace rotation) throws IOException, WorldEditException {
        ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(roomFile);
        if (clipboardFormat == null) {
            Bukkit.getLogger().severe("Cannot paste room: " + name() + "! Room file exists?:" + roomFile.exists() + ". Path: " + roomFile.getCanonicalPath());
            return null;
        }
        FileInputStream schematicInput = new FileInputStream(roomFile);
        ClipboardReader clipboardReader = clipboardFormat.getReader(schematicInput);

        Clipboard clipboard = clipboardReader.read();
        EditSession session = WorldEdit.getInstance().newEditSession(new BukkitWorld(location.getWorld()));

        double rotationValue = switch (rotation) {
            case EAST -> 270;
            case SOUTH -> 180;
            case WEST -> 90.0;
            default -> 0.0;
        };
        ClipboardHolder holder = new ClipboardHolder(clipboard);
        holder.setTransform(new AffineTransform().rotateY(rotationValue));
        Operation pasteOperation = holder.createPaste(session).to(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ())).ignoreAirBlocks(false).build();
        Operations.complete(pasteOperation);
        BlockVector3 min = holder.getClipboard().getRegion().getMinimumPoint();
        BlockVector3 max = holder.getClipboard().getRegion().getMaximumPoint();
        //Bukkit.getLogger().info(new Location(location.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ()).getBlock().getType() + " " + new Location(location.getWorld(), max.getBlockX(), max.getBlockY(), max.getBlockZ()).getBlock().getType());
        schematicInput.close();
        clipboardReader.close();
        session.close();
        try {
            return roomClass.getDeclaredConstructor(RoomType.class, Location.class).newInstance(this, location);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Schematic getSchematic() {
        return schematic;
    }

    public Vector getInDoorway() {
        return inDoorway;
    }

    public Doorway[] getOutDoorways() {
        return outDoorways;
    }
}
