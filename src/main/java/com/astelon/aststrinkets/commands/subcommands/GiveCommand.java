package com.astelon.aststrinkets.commands.subcommands;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Trinket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class GiveCommand extends Subcommand {

    public GiveCommand(AstsTrinkets plugin, TrinketManager trinketManager) {
        super(plugin, trinketManager, "give", "aststrinkets.command.give");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(Component.text("Correct usage: /trinkets give <trinket name> (optional player name) " +
                            "(optional amount)",
                    NamedTextColor.RED));
            return;
        }
        String trinketName = args[1];
        Trinket trinket = trinketManager.getTrinket(trinketName);
        if (trinket == null) {
            sender.sendMessage(Component.text("There is no trinket with the name " + trinketName + ".",
                    NamedTextColor.RED));
            return;
        }
        if (trinket.isOp() && !sender.hasPermission("aststrinkets.op")) {
            sender.sendMessage(Component.text("You don't have permission to give this trinket.", NamedTextColor.RED));
            return;
        }
        Player player;
        if (args.length == 2 && !(sender instanceof Player)) {
            sender.sendMessage(Component.text("You have to specify a player to give the trinket to if using " +
                    "the console.", NamedTextColor.RED));
            return;
        } else {
            if (args.length == 2)
                player = (Player) sender;
            else
                player = Bukkit.getPlayer(args[2]);
        }
        int amount = 1;
        if (player == null) {
            try {
                amount = Integer.parseInt(args[2]);
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Component.text("There is no online player with that name.", NamedTextColor.RED));
                    return;
                }
                player = (Player) sender;
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("There is no online player with that name.", NamedTextColor.RED));
                return;
            }
        } else if (args.length == 4) {
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("Invalid amount.", NamedTextColor.RED));
                return;
            }
        }
        if (amount <= 0) {
            sender.sendMessage(Component.text("Invalid amount.", NamedTextColor.RED));
            return;
        }
        // Maybe I should allow this
        if (amount > trinket.getItemStack().getType().getMaxStackSize()) {
            sender.sendMessage(Component.text("You can't give more than a full stack.", NamedTextColor.RED));
            return;
        }
        giveTrinket(sender, player, trinket, amount);
        if (sender.equals(player)) {
            sender.sendMessage(Component.text("You have received a trinket.", NamedTextColor.GOLD));
        } else {
            sender.sendMessage(Component.text("You gave " + player.getName() + " a trinket."));
        }
    }

    private void giveTrinket(CommandSender sender, Player target, Trinket trinket, int amount) {
        ItemStack itemStack = trinket.getItemStack();
        itemStack.setAmount(amount);
        if (target.getInventory().firstEmpty() != -1) {
            target.getInventory().addItem(itemStack);
            target.updateInventory();
        } else {
            target.getWorld().dropItem(target.getLocation(), itemStack);
            if (!sender.equals(target))
                sender.sendMessage(Component.text(target.getName() + " has full inventory, the trinket was dropped " +
                        "at their location.", NamedTextColor.RED));
            target.sendMessage(Component.text("Your inventory is full, the trinket was dropped at your location.",
                    NamedTextColor.RED));
        }
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        String trinketText = args[1].toLowerCase();
        if (args.length == 2) {
            return trinketManager.getTrinkets().stream()
                    .filter(trinket -> sender.hasPermission("aststrinkets.op") || !trinket.isOp())
                    .map(Trinket::getName)
                    .filter(trinketName -> trinketName.toLowerCase().startsWith(trinketText))
                    .collect(Collectors.toList());
        } else if (args.length == 3) {
            String playerText = args[2].toLowerCase();
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName)
                    .filter(playerName -> playerName.toLowerCase().startsWith(playerText)).collect(Collectors.toList());
        }
        return null;
    }
}
