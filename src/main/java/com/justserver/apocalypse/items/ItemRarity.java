package com.justserver.apocalypse.items;

import org.bukkit.ChatColor;

import static org.bukkit.ChatColor.*;

public enum ItemRarity {
    COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHIC, DUNGEON, SUPREME;

    ChatColor getColor() {
        return switch (this) {
            case UNCOMMON -> GREEN;
            case RARE -> BLUE;
            case EPIC -> DARK_PURPLE;
            case LEGENDARY -> GOLD;
            case MYTHIC -> LIGHT_PURPLE;
            case DUNGEON -> RED;
            case SUPREME -> DARK_RED;
            default -> WHITE;
        };
    }

    String translate() {
        return switch (this) {
            case UNCOMMON -> GREEN + "" + BOLD + "Необычный";
            case RARE -> BLUE + "" + BOLD + "Редкий";
            case EPIC -> DARK_PURPLE + "" + BOLD + "Эпический";
            case LEGENDARY -> GOLD + "" + BOLD + "Легендарный";
            case MYTHIC -> LIGHT_PURPLE + "" + BOLD + "Мифический";
            case DUNGEON -> RED + "" + BOLD + "Данженский";
            case SUPREME -> DARK_RED + "" + BOLD + "Ультра";
            default -> WHITE + "" + BOLD + "Обычный";
        };
    }
}
