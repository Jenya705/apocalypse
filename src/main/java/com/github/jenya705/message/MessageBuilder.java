package com.github.jenya705.message;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

/**
 * @author Jenya705
 */
public interface MessageBuilder {

    Component buildMessage(Player sender, Component message);

}
