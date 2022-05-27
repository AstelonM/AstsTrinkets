package com.astelon.aststrinkets.commands;

import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.managers.TrinketManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class TrinketCommand implements TabExecutor {

    private final TrinketManager trinketManager;
    private final List<String> subcommands;

    public TrinketCommand(TrinketManager trinketManager) {
        this.trinketManager = trinketManager;
        subcommands = List.of("give", "clear");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Available subcommands: give, clear."));
            return true;
        }
        String subcommand = args[0];
        if (subcommand.equalsIgnoreCase("give")) {
            if (args.length == 1) {
                sender.sendMessage(Component.text("Correct usage: /trinkets give <trinket name> (optional player name)",
                        NamedTextColor.RED));
                return true;
            }
            String trinketName = args[1];
            Trinket trinket = trinketManager.getTrinket(trinketName);
            if (trinket == null) {
                sender.sendMessage(Component.text("There is no enabled trinket with the name " + trinketName + ".",
                        NamedTextColor.RED));
                return true;
            }
            if (trinket.isOp() && !sender.hasPermission("aststrinkets.op")) {
                sender.sendMessage(Component.text("You don't have permission to give this trinket.", NamedTextColor.RED));
                return true;
            }
            Player player;
            if (args.length == 2 && !(sender instanceof Player)) {
                sender.sendMessage(Component.text("You have to specify a player to give the trinket to if using " +
                        "the console.", NamedTextColor.RED));
                return true;
            } else {
                if (args.length == 2)
                    player = (Player) sender;
                else
                    player = Bukkit.getPlayer(args[2]);
            }
            if (player == null) {
                sender.sendMessage(Component.text("There is no online player with that name.", NamedTextColor.RED));
                return true;
            }
            giveTrinket(sender, player, trinket);
            if (sender.equals(player)) {
                sender.sendMessage(Component.text("You have received a trinket.", NamedTextColor.GOLD));
            } else {
                sender.sendMessage(Component.text("You gave " + player.getName() + " a trinket."));
            }
        } else if (subcommand.equalsIgnoreCase("clear")) {
            Player player;
            if (args.length == 1 && !(sender instanceof Player)) {
                sender.sendMessage(Component.text("You have to specify a player if using the console.",
                        NamedTextColor.RED));
                return true;
            } else {
                if (args.length == 1)
                    player = (Player) sender;
                else
                    player = Bukkit.getPlayer(args[1]);
            }
            if (player == null) {
                sender.sendMessage(Component.text("There is no online player with that name.", NamedTextColor.RED));
                return true;
            }
            trinketManager.removePlayerTrinkets(player);
            if (sender.equals(player)) {
                sender.sendMessage("You have removed all your trinkets.");
            } else {
                sender.sendMessage("You removed " + player.getName() + "'s trinkets.");
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            String text = args[0].toLowerCase();
            return subcommands.stream().filter(subcommand -> subcommand.startsWith(text)).collect(Collectors.toList());
        } else if (args.length == 2) {
            String subcommand = args[0];
            if (subcommand.equalsIgnoreCase("clear")) {
                String text = args[1];
                return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName)
                        .filter(playerName -> playerName.startsWith(text)).collect(Collectors.toList());
            } else if (subcommand.equalsIgnoreCase("give")) {
                String text = args[1];
                return trinketManager.getTrinkets().stream()
                        .filter(Trinket::isEnabled)
                        .filter(trinket -> sender.hasPermission("aststrinkets.op") || !trinket.isOp())
                        .map(Trinket::getName)
                        .filter(trinketName -> trinketName.startsWith(text))
                        .collect(Collectors.toList());
            }
        } else if (args.length == 3) {
            String subcommand = args[0];
            if (subcommand.equalsIgnoreCase("give")) {
                String text = args[2];
                return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName)
                        .filter(playerName -> playerName.startsWith(text)).collect(Collectors.toList());
            }
        }
        return null;
    }

    private void giveTrinket(CommandSender sender, Player target, Trinket trinket) {
        ItemStack itemStack = trinket.getItemStack();
        if (target.getInventory().firstEmpty() != -1) {
            target.getInventory().addItem(itemStack);
            target.updateInventory();
        } else {
            target.getWorld().dropItem(target.getLocation(), itemStack);
            sender.sendMessage(Component.text(target.getName() + " has full inventory, the trinket was dropped " +
                    "at their location.", NamedTextColor.RED));
            target.sendMessage(Component.text(" your inventory is full, the trinket was dropped at your location.",
                    NamedTextColor.RED));
        }
    }
}
