package me.manny;

import lombok.Getter;
import me.manny.whitelist.command.CommandFramework;
import me.manny.whitelist.command.args.WhitelistCommands;
import me.manny.whitelist.language.Language;
import me.manny.whitelist.listener.WhitelistListener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Whitelist extends JavaPlugin {

    @Getter
    private static Whitelist instance;
    private CommandFramework commandFramework;

    @Override
    public void onEnable() {
        instance = this;
        Language.hook(this);
        this.commandFramework = new CommandFramework(this);
        this.commandFramework.registerCommands(new WhitelistCommands(this));
        this.getServer().getPluginManager().registerEvents(new WhitelistListener(this), this);
    }
}