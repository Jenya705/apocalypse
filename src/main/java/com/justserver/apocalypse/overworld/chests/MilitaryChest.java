package com.justserver.apocalypse.overworld.chests;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import com.justserver.apocalypse.items.Item;

public class MilitaryChest extends Chest {

    public MilitaryChest(Apocalypse plugin) {
        super(plugin,
                Registry.AK_47,
                Registry.SVD,
                Registry.PISTOL,
                Registry.M4A4,
                Registry.SHOTGUN,
                Registry.BULLETS,
                Registry.SCOPE,
                Registry.GRIP,
                Registry.SILENCER,
                Registry.MEDKIT, Registry.MEDKIT, Registry.MEDKIT, Registry.MEDKIT, Registry.MEDKIT
                );
    }
}
