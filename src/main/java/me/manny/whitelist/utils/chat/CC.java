package me.manny.whitelist.utils.chat;

import org.bukkit.ChatColor;

public class CC {
    public static String GRAY = ChatColor.GRAY.toString();
    public static String GREEN = ChatColor.GREEN.toString();
    public static String PURPLE = ChatColor.LIGHT_PURPLE.toString();
    public static String RED = ChatColor.RED.toString();
    public static String WHITE = ChatColor.WHITE.toString();
    public static String YELLOW = ChatColor.YELLOW.toString();

    public static String BD_GREEN = ChatColor.DARK_GREEN + ChatColor.BOLD.toString();
    public static String BD_PURPLE = ChatColor.DARK_PURPLE + ChatColor.BOLD.toString();

    public static String LINE = ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString();

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}