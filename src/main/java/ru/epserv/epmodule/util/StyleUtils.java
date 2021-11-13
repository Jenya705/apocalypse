package ru.epserv.epmodule.util;

import net.kyori.adventure.text.*;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;


/**
 *
 * Adventure API utility class for shorter code,
 * may not contain the complete list of shortcut methods
 * as they're being added when they're needed.
 *
 * @author l_MrBoom_l
 *
 */
public class StyleUtils {

    @NotNull
    public static Component text(@Nullable Object... objects) {
        return text(components(objects));
    }

    @NotNull
    public static Component text(@Nullable Component... components) {
        return single(components);
    }

    @NotNull
    public static Component darkAqua(@Nullable Object... objects) {
        return darkAqua(components(objects));
    }

    @NotNull
    public static Component darkAqua(@Nullable Component... components) {
        return single(components).colorIfAbsent(NamedTextColor.DARK_AQUA);
    }

    @NotNull
    public static Component green(@Nullable Object... objects) {
        return green(components(objects));
    }

    @NotNull
    public static Component green(@Nullable Component... components) {
        return single(components).colorIfAbsent(NamedTextColor.GREEN);
    }

    @NotNull
    public static Component red(@Nullable Object... objects) {
        return red(components(objects));
    }

    @NotNull
    public static Component red(@Nullable Component... components) {
        return single(components).colorIfAbsent(NamedTextColor.RED);
    }

    @NotNull
    public static Component blue(@Nullable Object... objects) {
        return blue(components(objects));
    }

    @NotNull
    public static Component blue(@Nullable Component... components) {
        return single(components).colorIfAbsent(NamedTextColor.BLUE);
    }

    @NotNull
    public static Component aqua(@Nullable Object... objects) {
        return aqua(components(objects));
    }

    @NotNull
    public static Component aqua(@Nullable Component... components) {
        return single(components).colorIfAbsent(NamedTextColor.AQUA);
    }

    @NotNull
    public static Component gray(@Nullable Object... objects) {
        return gray(components(objects));
    }

    @NotNull
    public static Component gray(@Nullable Component... components) {
        return single(components).colorIfAbsent(NamedTextColor.GRAY);
    }

    @NotNull
    public static Component gold(@Nullable Object... objects) {
        return gold(components(objects));
    }

    @NotNull
    public static Component gold(@Nullable Component... components) {
        return single(components).colorIfAbsent(NamedTextColor.GOLD);
    }

    @NotNull
    public static Component darkGreen(@Nullable Object... objects) {
        return darkGreen(components(objects));
    }

    @NotNull
    public static Component darkGreen(@Nullable Component... components) {
        return single(components).colorIfAbsent(NamedTextColor.DARK_GREEN);
    }

    @NotNull
    public static Component yellow(@Nullable Object... objects) {
        return yellow(components(objects));
    }

    @NotNull
    public static Component yellow(@Nullable Component... components) {
        return single(components).colorIfAbsent(NamedTextColor.YELLOW);
    }

    @NotNull
    public static Component lightPurple(@Nullable Object... objects) {
        return lightPurple(components(objects));
    }

    @NotNull
    public static Component lightPurple(@Nullable Component... components) {
        return single(components).colorIfAbsent(NamedTextColor.LIGHT_PURPLE);
    }

    @NotNull
    public static Component darkPurple(@Nullable Object... objects) {
        return darkPurple(components(objects));
    }

    @NotNull
    public static Component darkPurple(@Nullable Component... components) {
        return single(components).colorIfAbsent(NamedTextColor.DARK_PURPLE);
    }

    @NotNull
    public static Component white(@Nullable Object... objects) {
        return white(components(objects));
    }

    @NotNull
    public static Component white(@Nullable Component... components) {
        return single(components).colorIfAbsent(NamedTextColor.WHITE);
    }

    @NotNull
    public static Component newline() {
        return Component.newline();
    }

    @NotNull
    public static Component space() {
        return Component.space();
    }

    @NotNull
    public static Component empty() {
        return Component.empty();
    }

    @NotNull
    public static TranslatableComponent translatable(@NotNull String key, @NotNull Object... objects) {
        return translatable(key, components(objects));
    }
    @NotNull
    public static TranslatableComponent translatable(@NotNull String key, @NotNull Component... components) {
        return Component.translatable(key, components);
    }

    @NotNull
    public static Component reset(@NotNull Object... objects) {
        return reset(components(objects));
    }

    @NotNull
    public static Component reset(@NotNull Component... components) {
        return single(components).color(null);
    }

    @NotNull
    public static Component underline(@NotNull Object... objects) {
        return underline(components(objects));
    }

    @NotNull
    public static Component underline(@NotNull Component... components) {
        return single(components).decorate(TextDecoration.UNDERLINED);
    }

    @NotNull
    public static Component italic(@NotNull Object... objects) {
        return italic(components(objects));
    }

    @NotNull
    public static Component italic(@NotNull Component... components) {
        return single(components).decorate(TextDecoration.ITALIC);
    }

    @NotNull
    public static Component strikethrough(@NotNull Object... objects) {
        return strikethrough(components(objects));
    }

    @NotNull
    public static Component strikethrough(@NotNull Component... components) {
        return single(components).decorate(TextDecoration.STRIKETHROUGH);
    }

    @NotNull
    public static Component obfuscate(@NotNull Object... objects) {
        return obfuscate(components(objects));
    }

    @NotNull
    public static Component obfuscate(@NotNull Component... components) {
        return single(components).decorate(TextDecoration.OBFUSCATED);
    }

    @NotNull
    public static Component bold(@NotNull Object... objects) {
        return bold(components(objects));
    }

    @NotNull
    public static Component bold(@NotNull Component... components) {
        return single(components).decorate(TextDecoration.BOLD);
    }

    @NotNull
    public static Component clickUrl(@NotNull String url, @NotNull Object... objects) {
        return clickUrl(url, components(objects));
    }

    @NotNull
    public static Component clickUrl(@NotNull String url, @NotNull Component... components) {
        return single(components).clickEvent(ClickEvent.openUrl(url));
    }

    @NotNull
    public static Component clickCommand(@NotNull String command, @NotNull Object... objects) {
        return clickCommand(command, components(objects));
    }

    @NotNull
    public static Component clickCommand(@NotNull String command, @NotNull Component... components) {
        return single(components).clickEvent(ClickEvent.runCommand(command));
    }

    @NotNull
    public static Component hoverText(@NotNull String text, @NotNull Object... objects) {
        return hoverText(text, components(objects));
    }

    @NotNull
    public static Component hoverText(@NotNull String text, @NotNull Component... components) {
        return single(components).hoverEvent(HoverEvent.showText(text(text)));
    }

    @NotNull
    public static Component hoverText(@NotNull Component text, @NotNull Object... objects) {
        return hoverText(text, components(objects));
    }

    @NotNull
    public static Component hoverText(@NotNull Component text, @NotNull Component... components) {
        return single(components).hoverEvent(HoverEvent.showText(text));
    }

    @NotNull
    public static Component join(@NotNull Component separator, @NotNull Collection<?> objects) {
        return Component.join(JoinConfiguration.separator(separator), objects.stream().map(obj -> obj instanceof Component c ? c : text(String.valueOf(obj))).toArray(Component[]::new));
    }

    @NotNull
    public static Component join(@NotNull Collection<?> objects) {
        return join(Component.empty(), objects);
    }

    @NotNull
    public static Component join(@NotNull Component separator, @NotNull Object... objects) {
        return Component.join(JoinConfiguration.separator(separator), components(objects));
    }

    @NotNull
    public static Component join(@NotNull Object... objects) {
        return join(Component.empty(), objects);
    }

    @NotNull
    public static Component@NotNull[] components(@Nullable Object@NotNull... objects) {
        return Arrays.stream(objects).filter(Objects::nonNull).map(StyleUtils::component).toArray(Component[]::new);
    }

    @NotNull
    public static Component component(@NotNull Object obj) {
        return obj instanceof Component c ? c : Component.text(String.valueOf(obj));
    }

    public static Component single(@Nullable Object... objects) {
        return objects.length == 0 ? empty() : (objects.length == 1 && objects[0] != null ? component(objects[0])
                : join(Arrays.stream(objects).filter(Objects::nonNull).toArray()));
    }

    public static Component single(@Nullable Component... components) {
        return components.length == 0 ? empty() : (components.length == 1 ? components[0] : join((Object[]) components));
    }

    public static Component single(@NotNull Collection<Component> components) {
        return single(components.toArray(Component[]::new));
    }

    public static Component regular(@NotNull Component component) {
        Component c = component;
        for (TextDecoration d : TextDecoration.values())
            c = c.decoration(d, false);
        return c;
    }

    @NotNull
    public static Component spaced(@Nullable Object @NotNull... objects) {
        return join(space(), (Object[]) components(objects));
    }

    @NotNull
    public static Component newlined(@Nullable Object @NotNull... objects) {
        return join(newline(), (Object[]) components(objects));
    }

    @NotNull
    public static SelectorComponent selector(@NotNull String pattern) {
        return Component.selector(pattern);
    }

    public enum StatusType {
        SUCCESS(green("+")),
        PROCESSING(gray("*")),
        ALREADY_DONE(green("+")),
        FAILURE(red("-")),

        ATTENTION(red("!"));

        private final @NotNull Component sign;

        StatusType(@NotNull Component sign) {
            this.sign = sign;
        }

        public Component sign() {
            return this.sign;
        }
    }

    @NotNull
    public static Component statusMessage(@NotNull StatusType statusType, @NotNull Component @NotNull... components) {
        return spaced(darkAqua("[", statusType.sign(), "]"), blue(components));
    }

    @NotNull
    public static Component statusMessage(@NotNull StatusType statusType, @Nullable Object @NotNull... objects) {
        return statusMessage(statusType, components(objects));
    }

}
