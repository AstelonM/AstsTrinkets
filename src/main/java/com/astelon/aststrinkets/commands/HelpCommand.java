package com.astelon.aststrinkets.commands;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Trinket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class HelpCommand extends Subcommand {

    public HelpCommand(AstsTrinkets plugin, TrinketManager trinketManager) {
        super(plugin, trinketManager, "help", "aststrinkets.command.help");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command cannot be run by console.", NamedTextColor.RED));
            return;
        }
        PlayerInventory inventory = player.getInventory();
        ItemStack mainHandItem = inventory.getItemInMainHand();
        Trinket trinket = trinketManager.getTrinket(mainHandItem);
        if (trinket != null) {
            player.sendMessage(trinket.getUsage());
            return;
        }
        ItemStack offHandItem = inventory.getItemInOffHand();
        trinket = trinketManager.getTrinket(offHandItem);
        if (trinket != null) {
            player.sendMessage(trinket.getUsage());
            return;
        }
        player.sendMessage(Component.text("You are not holding any trinket.", NamedTextColor.RED));
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
