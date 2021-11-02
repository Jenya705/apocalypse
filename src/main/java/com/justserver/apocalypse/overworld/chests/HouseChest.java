package com.justserver.apocalypse.overworld.chests;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;

public class HouseChest extends Chest{
    public HouseChest(Apocalypse plugin) {
        super(plugin,
                Registry.PISTOL,
                Registry.GRIP,
                Registry.FLYING_AXE,
                Registry.MEDKIT, Registry.MEDKIT, Registry.MEDKIT
        );
    }
}
