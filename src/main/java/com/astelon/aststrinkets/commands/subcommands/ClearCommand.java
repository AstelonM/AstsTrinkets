package com.astelon.aststrinkets.commands.subcommands;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ClearCommand extends Subcommand {

    public ClearCommand(AstsTrinkets plugin, TrinketManager trinketManager) {
        super(plugin, trinketManager, "clear", "aststrinkets.command.clear");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        Player player;
        if (args.length == 1 && !(sender instanceof Player)) {
            sender.sendMessage(Component.text("You have to specify a player if using the console.",
                    NamedTextColor.RED));
            return;
        } else {
            if (args.length == 1)
                player = (Player) sender;
            else
                player = Bukkit.getPlayer(args[1]);
        }
        if (player == null) {
            sender.sendMessage(Component.text("There is no online player with that name.", NamedTextColor.RED));
            return;
        }
        trinketManager.removePlayerTrinkets(player);
        if (sender.equals(player)) {
            sender.sendMessage("You have removed all your trinkets.");
        } else {
            sender.sendMessage("You removed " + player.getName() + "'s trinkets.");
        }
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        String text = args[1].toLowerCase();
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName)
                .filter(playerName -> playerName.toLowerCase().startsWith(text)).collect(Collectors.toList());
    }
}
