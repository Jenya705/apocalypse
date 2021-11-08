package com.justserver.apocalypse.base.workbenches;

import com.justserver.apocalypse.items.Item;

public class CraftItem {
    public final Item item;
    public final int count;

    public CraftItem(Item item, int count){
        this.item = item;
        System.out.println(item);
        this.count = count;
    }
}
