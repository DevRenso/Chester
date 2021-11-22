package dev.deqrenso.chester.utils;

import org.bukkit.ChatColor;

public class StringUtils {
    public static String CC(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
