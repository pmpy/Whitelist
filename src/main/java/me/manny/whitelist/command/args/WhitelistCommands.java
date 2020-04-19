package me.manny.whitelist.command.args;

import me.manny.Whitelist;
import me.manny.whitelist.language.Language;
import me.manny.whitelist.utils.PlayerUtility;
import me.manny.whitelist.utils.chat.CC;
import me.manny.whitelist.utils.command.Command;
import me.manny.whitelist.utils.command.CommandArgs;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class WhitelistCommands {

    private Whitelist plugin;

    public WhitelistCommands(Whitelist plugin) {
        this.plugin = plugin;
    }

    @Command(name = "whitelist", aliases = "wl")
    public void whitelist(CommandArgs command) {
        CommandSender sender = command.getSender();
        String label = command.getLabel().replace(".", " ");
        sender.sendMessage(CC.LINE + "----------------------");
        sender.sendMessage(CC.BD_GREEN + "User Commands");
        sender.sendMessage(CC.GREEN + "/" + label + " add <user>");
        sender.sendMessage(CC.GREEN + "/" + label + " tokens <user>");
        if (sender.hasPermission("whitelist.staff") || sender.isOp()) {
            sender.sendMessage(" ");
            sender.sendMessage(CC.BD_PURPLE + "Admin Commands");
            sender.sendMessage(CC.PURPLE + "/" + label + " addtokens <user> <amount>");
            sender.sendMessage(CC.PURPLE + "/" + label + " settokens <user> <amount>");
            sender.sendMessage(CC.PURPLE + "/" + label + " removetokens <user> <amount>");
            sender.sendMessage(CC.PURPLE + "/" + label + " removeplayer <user>");
            sender.sendMessage(CC.PURPLE + "/" + label + " listplayers");
            sender.sendMessage(CC.PURPLE + "/" + label + " toggle");
            sender.sendMessage(CC.PURPLE + "/" + label + " status");
            sender.sendMessage(CC.PURPLE + "/" + label + " save");
        }
        sender.sendMessage(CC.LINE + "----------------------");
    }

    @Command(name = "whitelist.add", aliases = "wl.add")
    public void whitelistAdd(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        String label = command.getLabel().replace(".", " ");

        if (args.length != 1) {
            sender.sendMessage(CC.RED + "Usage: /" + label + " <user>");
            return;
        }

        if (!(sender instanceof Player)) {
            PlayerUtility.getOfflinePlayer(args[0]).setWhitelisted(true);
            Bukkit.reloadWhitelist();
            sender.sendMessage(Language.CONSOLE_USER_ADDED_TO_WHITELIST.toString().replace("<player>", args[0]));
        } else {
            File dir = new File(this.plugin.getDataFolder() + "/players/" + sender.getName() + ".yml");
            YamlConfiguration data = YamlConfiguration.loadConfiguration(dir);

            try {
                data.save(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (sender.hasPermission("whitelist.bypass") || sender.isOp()) {
                OfflinePlayer target = PlayerUtility.getOfflinePlayer(args[0]);
                if (Bukkit.getWhitelistedPlayers().contains(target)) {
                    sender.sendMessage(Language.ERROR_ALREADY_WHITELISTED.toString().replace("<player>", args[0]));
                    return;
                }

                target.setWhitelisted(true);
                Bukkit.reloadWhitelist();
                sender.sendMessage(Language.ADMIN_USER_ADDED_TO_WHITELIST.toString().replace("<player>", args[0]));
                return;
            }
            if (data.getInt("tokens") <= 0) {
                sender.sendMessage(Language.ERROR_NOT_ENOUGH_TOKENS.toString());
            } else if (data.getInt("tokens") > 0) {
                OfflinePlayer target = PlayerUtility.getOfflinePlayer(args[0]);
                if (Bukkit.getWhitelistedPlayers().contains(target)) {
                    sender.sendMessage(Language.ERROR_ALREADY_WHITELISTED.toString().replace("<player>", args[0]));
                    return;
                }
                data.set("tokens", data.getInt("tokens") - 1);
                try {
                    data.save(dir);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Critical error, cant write the file");
                }
                target.setWhitelisted(true);
                Bukkit.reloadWhitelist();
                String tokens = String.valueOf(data.getInt("tokens"));
                sender.sendMessage(Language.PLAYER_USER_ADDED_TO_WHITELIST.toString().replace("<player>", args[0]).replace("<tokens>", tokens));
            }
        }
    }

    @Command(name = "whitelist.tokens", aliases = "wl.tokens")
    public void whitelistTokens(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        String label = command.getLabel().replace(".", " ");

        if (args.length != 1) {
            sender.sendMessage(CC.RED + "Usage: /" + label + " <user>");
            return;
        }

        File dir = new File(this.plugin.getDataFolder() + "/players/" + args[0] + ".yml");
        YamlConfiguration data = YamlConfiguration.loadConfiguration(dir);
        try {
            data.save(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String tokens = String.valueOf(data.getInt("tokens"));
        sender.sendMessage(Language.TOKENS_CHECK.toString().replace("<player>", args[0]).replace("<tokens>", tokens));
    }

    @Command(name = "whitelist.addtokens", aliases = "wl.addtokens", permission = "whitelist.addtokens")
    public void whitelistAddTokens(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        String label = command.getLabel().replace(".", " ");

        if (args.length != 2) {
            sender.sendMessage(CC.RED + "Usage: /" + label + " <user> <amount>");
            return;
        }

        String user = args[0];
        if (user == null) {
            sender.sendMessage(CC.RED + "Usage: /" + label + " <user> <amount>");
            return;
        }

        String stringAmount = args[1];
        if (!NumberUtils.isNumber(stringAmount)) {
            sender.sendMessage(CC.RED + "Usage: /" + label + " <user> <amount>");
            return;
        }

        File dir = new File(this.plugin.getDataFolder() + "/players/" + user + ".yml");
        YamlConfiguration data = YamlConfiguration.loadConfiguration(dir);
        data.set("tokens", data.getInt("tokens") + Integer.parseInt(stringAmount));
        try {
            data.save(dir);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Critical error, cant write the file");
        }
        sender.sendMessage(Language.ADMIN_ADDED_TOKENS.toString().replace("<player>", user).replace("<tokens>", stringAmount));
    }

    @Command(name = "whitelist.settokens", aliases = "wl.settokens", permission = "whitelist.settokens")
    public void whitelistSetTokens(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        String label = command.getLabel().replace(".", " ");

        if (args.length != 2) {
            sender.sendMessage(CC.RED + "Usage: /" + label + " <user> <amount>");
            return;
        }

        String user = args[0];
        if (user == null) {
            sender.sendMessage(CC.RED + "Usage: /" + label + " <user> <amount>");
            return;
        }

        String stringAmount = args[1];
        if (!NumberUtils.isNumber(stringAmount)) {
            sender.sendMessage(CC.RED + "Usage: /" + label + " <user> <amount>");
            return;
        }

        File dir = new File(this.plugin.getDataFolder() + "/players/" + user + ".yml");
        YamlConfiguration data = YamlConfiguration.loadConfiguration(dir);
        data.set("tokens", Integer.parseInt(stringAmount));
        try {
            data.save(dir);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Critical error, cant write the file");
        }
        sender.sendMessage(Language.ADMIN_SET_TOKENS.toString().replace("<player>", user).replace("<tokens>", stringAmount));
    }

    @Command(name = "whitelist.removetokens", aliases = "wl.removetokens", permission = "whitelist.removetokens")
    public void whitelistRemoveTokens(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        String label = command.getLabel().replace(".", " ");

        if (args.length != 2) {
            sender.sendMessage(CC.RED + "Usage: /" + label + " <user> <amount>");
            return;
        }

        String user = args[0];
        if (user == null) {
            sender.sendMessage(CC.RED + "Usage: /" + label + " <user> <amount>");
            return;
        }

        String stringAmount = args[1];
        if (!NumberUtils.isNumber(stringAmount)) {
            sender.sendMessage(CC.RED + "Usage: /" + label + " <user> <amount>");
            return;
        }

        File dir = new File(this.plugin.getDataFolder() + "/players/" + user + ".yml");
        YamlConfiguration data = YamlConfiguration.loadConfiguration(dir);
        data.set("tokens", data.getInt("tokens") - Integer.parseInt(stringAmount));
        if (data.getInt("tokens") < 0) {
            data.set("tokens", 0);
        }
        try {
            data.save(dir);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Critical error, cant write the file");
        }
        sender.sendMessage(Language.ADMIN_REMOVE_TOKENS.toString().replace("<player>", user).replace("<tokens>", stringAmount));
    }

    @Command(name = "whitelist.removeplayer", aliases = "wl.removeplayer", permission = "whitelist.removeplayer")
    public void whitelistRemovePlayer(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        String label = command.getLabel().replace(".", " ");

        if (args.length != 1) {
            sender.sendMessage(CC.RED + "Usage: /" + label + " <user>");
            return;
        }

        OfflinePlayer target = PlayerUtility.getOfflinePlayer(args[0]);
        if (target.isWhitelisted()) {
            target.setWhitelisted(false);
            sender.sendMessage(Language.ADMIN_USER_REMOVED_FROM_WHITELIST.toString().replace("<player>", args[0]));
        } else {
            sender.sendMessage(Language.ERROR_NOT_WHITELISTED.toString().replace("<player>", args[0]));
        }
    }

    @Command(name = "whitelist.listplayers", aliases = "wl.listplayers", permission = "whitelist.listplayers")
    public void whitelistListPlayers(CommandArgs command) {
        CommandSender sender = command.getSender();
        StringBuilder result = new StringBuilder();
        for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
            if (result.length() > 0) {
                result.append(CC.WHITE).append(", ");
            }
            result.append(player.isOnline() ? CC.GREEN + player.getName() : CC.RED + player.getName());
        }
        sender.sendMessage(CC.translate("&a&l■ &aOnline &7⎜ &c&l■ &cOffline"));
        sender.sendMessage(CC.GREEN + "Whitelisted Players" + CC.GRAY + " (" + CC.WHITE + Bukkit.getWhitelistedPlayers().size() + CC.GRAY + "): " + CC.WHITE + result.toString());
    }

    @Command(name = "whitelist.toggle", aliases = "wl.toggle", permission = "whitelist.toggle")
    public void whitelistToggle(CommandArgs command) {
        CommandSender sender = command.getSender();
        Bukkit.getServer().setWhitelist(!Bukkit.getServer().hasWhitelist());
        sender.sendMessage(Bukkit.getServer().hasWhitelist() ? CC.GREEN + "Whitelist has been turned on." : CC.RED + "Whitelist has been turned off.");
    }

    @Command(name = "whitelist.status", aliases = "wl.status", permission = "whitelist.status")
    public void whitelistStatus(CommandArgs command) {
        CommandSender sender = command.getSender();
        sender.sendMessage(CC.YELLOW + "Whitelist mode is currently turned " + (Bukkit.hasWhitelist() ? CC.GREEN + "ON" : CC.RED + "OFF"));
    }

    @Command(name = "whitelist.save", aliases = "wl.save", permission = "whitelist.save")
    public void whitelistSave(CommandArgs command) {
        CommandSender sender = command.getSender();
        long before = System.currentTimeMillis();
        Bukkit.reloadWhitelist();
        Bukkit.savePlayers();
        long after = System.currentTimeMillis();
        sender.sendMessage(Language.WHITELIST_SAVE.toString().replace("<time>", String.valueOf(after - before)));
    }
}