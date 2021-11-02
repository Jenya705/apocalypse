package com.justserver.apocalypse.overworld;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.items.Item;

import java.util.ArrayList;
import java.util.Collections;

public class Chest {
    public ArrayList<Item> whatSpawns = new ArrayList<>();
    public Chest(Apocalypse plugin, Item... whatSpawns){
        Collections.addAll(this.whatSpawns, whatSpawns);
    }
}
