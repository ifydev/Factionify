package me.ifydev.factionify.spigot.util;

import org.bukkit.ChatColor;

/**
 * @author Innectic
 * @since 07/28/2018
 */
public class ColorUtil {

    public static String makeReadable(String convert) {
        return ChatColor.translateAlternateColorCodes('&', convert);
    }
}
