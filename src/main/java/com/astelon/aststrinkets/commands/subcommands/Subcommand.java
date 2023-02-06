package com.astelon.aststrinkets.commands.subcommands;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class Subcommand {

    protected final AstsTrinkets plugin;
    protected final TrinketManager trinketManager;
    protected final String name;
    protected final String[] aliases;
    protected final String permission;

    protected Subcommand(AstsTrinkets plugin, TrinketManager trinketManager, String name, String[] aliases, String permission) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
    }

    protected Subcommand(AstsTrinkets plugin, TrinketManager trinketManager, String name, String permission) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.name = name;
        this.aliases = new String[0];
        this.permission = permission;
    }

    public void runCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission(permission))
            execute(sender, args);
        else
            sender.sendMessage(Component.text("You don't have permission to use this command.", NamedTextColor.RED));
    }

    protected abstract void execute(CommandSender sender, String[] args);

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(permission);
    }

    public boolean isCommand(String command) {
        if (name.equalsIgnoreCase(command))
            return true;
        for (String alias: aliases) {
            if (alias.equalsIgnoreCase(command))
                return true;
        }
        return false;
    }

    public boolean startsWith(String text) {
        if (name.startsWith(text))
            return true;
        for (String alias: aliases) {
            if (alias.startsWith(text))
                return true;
        }
        return false;
    }

    public abstract List<String> getTabComplete(CommandSender sender, String[] args);

    public String getName() {
        return name;
    }
}
