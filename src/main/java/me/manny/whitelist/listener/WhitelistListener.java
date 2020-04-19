package me.manny.whitelist.listener;

import me.manny.Whitelist;
import me.manny.whitelist.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;

public class WhitelistListener implements Listener {

    private Whitelist plugin;

    public WhitelistListener(Whitelist plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File playerData = new File(this.plugin.getDataFolder() + "/players/" + player.getName() + ".yml");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerData);
        if (playerData.exists()) return;
        configuration.set("tokens", Integer.parseInt("0"));
        try {
            configuration.save(playerData);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLogin(final AsyncPlayerPreLoginEvent event) {
        if (!Bukkit.hasWhitelist()) return;

        for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
            if (player.hasPlayedBefore()) {
                if (player.getUniqueId().equals(event.getUniqueId())) return;
            } else {
                if (player.getName().equals(event.getName())) return;
            }
        }
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Language.WHITELIST_KICK_MESSAGE.toString().replace("<player>", event.getName()));
    }
}