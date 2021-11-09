package com.github.jenya705.message;

import com.justserver.apocalypse.Apocalypse;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Jenya705
 */
public class CubicMessageBuilder implements MessageBuilder, PluginMessageListener {

    private final Map<UUID, TextColor> colors = new HashMap<>();

    public CubicMessageBuilder(Apocalypse apocalypse) {
        apocalypse.getServer().getMessenger().registerIncomingPluginChannel(
                apocalypse, "cubicore:color", this);
    }

    @Override
    public Component buildMessage(Player sender, Component message) {
        return Component
                .text(sender.getName())
                .append(Component
                        .text(" > ")
                        .decorate(TextDecoration.BOLD)
                        .color(colors.getOrDefault(sender.getUniqueId(), NamedTextColor.GRAY))
                        .append(message
                                .color(NamedTextColor.WHITE)
                                .decoration(TextDecoration.BOLD, false)
                        )
                );
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] bytes) {
        if (!channel.equals("cubicore:color")) return;
        String color = new String(bytes);
        colors.put(player.getUniqueId(), TextColor.fromHexString("#" + color));
    }
}
