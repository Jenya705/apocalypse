package com.github.jenya705.message;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author Jenya705
 */
public class DefaultMessageHandler implements Listener {

    private final MessageBuilder messageBuilder;

    public DefaultMessageHandler(MessageBuilder messageBuilder) {
        this.messageBuilder = messageBuilder;
    }

    @EventHandler
    public void chat(AsyncChatEvent event) {
        Component message = messageBuilder.buildMessage(event.getPlayer(), event.message());
        event.renderer((player, component, component1, audience) -> message);
    }

}
