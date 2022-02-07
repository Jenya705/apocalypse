package com.justserver.apocalypse.dungeons;

import com.justserver.apocalypse.utils.RelativeDirection;
import org.bukkit.util.Vector;

/**
 Custom vector to know how to turn next room
 @author MisterFunny01
 */
public class Doorway extends Vector {
    private final RelativeDirection relativeDirection;

    public Doorway(int x, int y, int z, RelativeDirection direction) {
        super(x, y, z);
        this.relativeDirection = direction;
    }

    public Doorway(double x, double y, double z, RelativeDirection direction) {
        super(x, y, z);
        this.relativeDirection = direction;
    }

    public Doorway(float x, float y, float z, RelativeDirection direction) {
        super(x, y, z);
        this.relativeDirection = direction;
    }

    public RelativeDirection getRelativeDirection() {
        return relativeDirection;
    }
}
