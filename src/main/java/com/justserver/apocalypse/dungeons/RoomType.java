package com.justserver.apocalypse.dungeons;

import com.justserver.apocalypse.Apocalypse;
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
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public enum RoomType {
    FIRST();

    private final File roomFile;
    RoomType(){
        this.roomFile = new File(Apocalypse.getInstance().getDataFolder().getAbsolutePath() + File.separator + "maps" + File.separator + this.name().toLowerCase() + ".room");
    }

    public File getRoomFile() {
        return roomFile;
    }

    public void paste(Location location, BlockFace rotation) throws IOException, WorldEditException {
        ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(roomFile);
        if(clipboardFormat == null) {
            Bukkit.getLogger().severe("Cannot paste room: " + name());
            return;
        }
        FileInputStream schematicInput = new FileInputStream(roomFile);
        ClipboardReader clipboardReader = clipboardFormat.getReader(schematicInput);
        Clipboard clipboard = clipboardReader.read();
        EditSession session = WorldEdit.getInstance().newEditSession(new BukkitWorld(location.getWorld()));

        double rotationValue;
        switch (rotation){
            case EAST:
                rotationValue = 90.0;
                break;
            case SOUTH:
                rotationValue = -180;
                break;
            case WEST:
                rotationValue = -90.0;
                break;
            default:
                rotationValue = 0.0;
                break;
        }
        ClipboardHolder holder = new ClipboardHolder(clipboard);
        holder.setTransform(new AffineTransform().rotateY(rotationValue));
        Operation pasteOperation = holder.createPaste(session).to(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ())).ignoreAirBlocks(false).build();
        Operations.complete(pasteOperation);
        schematicInput.close();
        clipboardReader.close();
        session.close();
    }
}
