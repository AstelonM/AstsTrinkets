package com.astelon.aststrinkets.commands;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.commands.subcommands.*;
import com.astelon.aststrinkets.managers.TrinketManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TrinketCommand implements TabExecutor {

    private final HashMap<String, Subcommand> subcommands;

    public TrinketCommand(AstsTrinkets plugin, TrinketManager trinketManager) {
        subcommands = new HashMap<>();
        addSubcommand(new GiveCommand(plugin, trinketManager));
        addSubcommand(new ClearCommand(plugin, trinketManager));
        addSubcommand(new ReloadCommand(plugin, trinketManager));
        addSubcommand(new HelpCommand(plugin, trinketManager));
        addSubcommand(new DebugCommand(plugin, trinketManager));
        addSubcommand(new SetKeyCommand(plugin, trinketManager));
    }

    private void addSubcommand(Subcommand subcommand) {
        subcommands.put(subcommand.getName(), subcommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            //TODO Only usable commands or all commands?
            sender.sendMessage(Component.text("Available subcommands for you: " + getAvailableCommands(sender) + "."));
            return true;
        }
        Subcommand subcommand = subcommands.get(args[0]);
        if (subcommand == null) {
            sender.sendMessage(Component.text("Invalid command. Available subcommands for you: " +
                    getAvailableCommands(sender) + ".", NamedTextColor.RED));
            return true;
        }
        subcommand.runCommand(sender, args);
        return true;
    }

    private String getAvailableCommands(CommandSender sender) {
        return subcommands.values().stream().filter(subcommand -> subcommand.hasPermission(sender))
                .map(Subcommand::getName).collect(Collectors.joining(", "));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
                                                @NotNull String[] args) {
        if (args.length == 1) {
            String text = args[0].toLowerCase();
            return subcommands.values().stream().filter(subcommand -> subcommand.startsWith(text))
                    .filter(subcommand -> subcommand.hasPermission(sender))
                    .map(Subcommand::getName).collect(Collectors.toList());
        } else if (args.length >= 2) {
            Subcommand subcommand = subcommands.get(args[0]);
            if (subcommand != null) {
                return subcommand.getTabComplete(sender, args);
            }
        }
        return null;
    }
}
