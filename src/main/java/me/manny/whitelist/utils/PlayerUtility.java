package me.manny.whitelist.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class PlayerUtility {
    public static OfflinePlayer getOfflinePlayer(String name) {
        return Bukkit.getServer().getOfflinePlayer(name);
    }

    public static OfflinePlayer getOfflinePlayer(UUID uuid) {
        return Bukkit.getServer().getOfflinePlayer(uuid);
    }
}