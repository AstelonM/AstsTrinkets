package com.astelon.aststrinkets.commands.subcommands;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class CopyKeysCommand extends Subcommand {

    public CopyKeysCommand(AstsTrinkets plugin, TrinketManager trinketManager) {
        super(plugin, trinketManager, "copykeys", "aststrinkets.command.copykeys");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command cannot be run by console.", NamedTextColor.RED));
            return;
        }
        PlayerInventory inventory = player.getInventory();
        ItemStack mainHandItem = inventory.getItemInMainHand();
        ItemStack offHandItem = inventory.getItemInOffHand();
        if (Utils.isNothing(mainHandItem) || Utils.isNothing(offHandItem)) {
            player.sendMessage(Component.text("You need to hold something in both hands.", NamedTextColor.RED));
            return;
        }
        trinketManager.getNamespacedKeys().transferKeys(mainHandItem, offHandItem);
        player.sendMessage(Component.text("Keys copied.", NamedTextColor.GOLD));
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
