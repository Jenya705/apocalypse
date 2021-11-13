package com.justserver.apocalypse.base.workbenches;

import com.justserver.apocalypse.items.Item;

public record CraftItem(Item item, int count) {

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }
}
