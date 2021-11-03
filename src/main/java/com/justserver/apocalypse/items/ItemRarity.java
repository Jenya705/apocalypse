package com.justserver.apocalypse.items;

import org.bukkit.ChatColor;
import static org.bukkit.ChatColor.*;

public enum ItemRarity {
    COMMON,UNCOMMON,RARE,EPIC,LEGENDARY;

    ChatColor getColor(){
        switch (this){
            case UNCOMMON:
                return GREEN;
            case RARE:
                return BLUE;
            case EPIC:
                return DARK_PURPLE;
            case LEGENDARY:
                return GOLD;
            default: return WHITE;
        }
    }
}
