package me.manny.whitelist.language;

import me.manny.Whitelist;
import me.manny.whitelist.utils.chat.CC;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum Language {

    ERROR_INVALID_PLAYER("ERROR.INVALID_PLAYER", "&4ERROR&7: &cCould not find player '<player>'."),
    ERROR_NOT_ENOUGH_TOKENS("ERROR.NOT_ENOUGH_TOKENS", "&4ERROR&7: &cYou do not have enough whitelist tokens."),
    ERROR_ALREADY_WHITELISTED("ERROR.PLAYER_ALREADY_WHITELISTED", "&4ERROR&7: &c<player> is already whitelisted."),
    ERROR_NOT_WHITELISTED("ERROR.PLAYER_NOT_WHITELISTED", "&4ERROR&7: &c'<player>' is not whitelisted."),

    CONSOLE_USER_ADDED_TO_WHITELIST("USER.CONSOLE_ADDED_TO_WHITELIST", "&aYou bypassed and added &2<player> &ato the whitelist using your &lCONSOLE POWERS&a."),
    ADMIN_USER_ADDED_TO_WHITELIST("USER.ADMIN_ADDED_TO_WHITELIST", "&aYou bypassed and added &2<player> &ato the whitelist using your &lADMIN POWERS&a."),
    PLAYER_USER_ADDED_TO_WHITELIST("USER.PLAYER_ADDED_TO_WHITELIST", "&aYou used one of your tokens to whitelist &2<player>&a. You have &2<tokens> &atokens remaining."),

    TOKENS_CHECK("TOKENS.CHECK", "&2<player> &ahas &2<tokens>x &awhitelist tokens."),
    WHITELIST_SAVE("WHITELIST_SAVE", "&aSuccessfully saved all data in &2<time>ms&a."),

    ADMIN_ADDED_TOKENS("ADMIN.ADDED_TOKENS", "&aYou added &2<tokens> &awhitelist tokens to &2<player>&a!"),
    ADMIN_SET_TOKENS("ADMIN.SET_TOKENS", "&aYou set &2<player>'s &awhitelist tokens to &2<tokens>&a!"),
    ADMIN_REMOVE_TOKENS("ADMIN.REMOVE_TOKENS", "&aYou removed &2<tokens> &awhitelist tokens from &2<player>&a!"),
    ADMIN_USER_REMOVED_FROM_WHITELIST("ADMIN.USER_REMOVED_FROM_WHITELIST", "&aYou removed &2<player> &afrom the whitelisted players list."),

    WHITELIST_KICK_MESSAGE("WHITELIST_KICK_MESSAGE", "&cYou are not whitelisted on this server! Purchase a whitelist at &4www.manny.com");

    private String path;
    private String value;
    private static YamlConfiguration language;

    Language(String path, String value) {
        this.path = path;
        this.value = value;
    }

    public static void setLangFile(YamlConfiguration config) {
        language = config;
    }

    public String toString() {
        return CC.translate(language.getString(this.path, value));
    }

    public String getPath() {
        return this.path;
    }

    public String getValue() {
        return this.value;
    }

    public static void hook(Whitelist plugin) {
        File language = new File(plugin.getDataFolder(), "language.yml");

        if (!language.exists()) {
            try {
                plugin.getDataFolder().mkdir();
                language.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Error: " + e.getMessage());
            }
        }

        try {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(language);
            for (Language string : Language.values()) {
                if (configuration.getString(string.getPath()) == null) {
                    configuration.set(string.getPath(), string.getValue());
                }
            }

            configuration.save(language);

            Language.setLangFile(configuration);
        } catch (IOException e) {
            plugin.getLogger().severe("Error: " + e.getMessage());
        }
    }
}