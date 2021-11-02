package com.justserver.apocalypse.overworld.chests;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.Item;

public class HospitalChest extends Chest{
    public HospitalChest(Apocalypse plugin) {
        super(plugin,
                Registry.MEDKIT,Registry.MEDKIT,Registry.MEDKIT,Registry.MEDKIT,Registry.MEDKIT,Registry.MEDKIT,Registry.MEDKIT, Registry.MEDKIT, Registry.MEDKIT,
                Registry.FLYING_AXE, Registry.BULLETS, Registry.BULLETS
                );
    }
}
