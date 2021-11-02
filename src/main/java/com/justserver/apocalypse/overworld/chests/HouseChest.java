package com.justserver.apocalypse.overworld;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.Item;

public class HouseChest extends Chest{
    public HouseChest(Apocalypse plugin) {
        super(plugin,
                Registry.PISTOL,
                Registry.GRIP,
                Registry.FLYING_AXE,
                
        );
    }
}
