package com.astelon.aststrinkets.commands.subcommands;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.HoldingBundle;
import com.astelon.aststrinkets.trinkets.Spellbook;
import com.astelon.aststrinkets.trinkets.Trinket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class HelpCommand extends Subcommand {

    private final Spellbook spellbook;
    private final HoldingBundle holdingBundle;

    public HelpCommand(AstsTrinkets plugin, TrinketManager trinketManager) {
        super(plugin, trinketManager, "help", "aststrinkets.command.help");
        spellbook = trinketManager.getSpellbook();
        holdingBundle = trinketManager.getHoldingBundle();
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command cannot be run by console.", NamedTextColor.RED));
            return;
        }
        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = inventory.getItemInMainHand();
        Trinket trinket = trinketManager.getTrinket(itemStack);
        if (noHelp(trinket, itemStack)) {
            itemStack = inventory.getItemInOffHand();
            trinket = trinketManager.getTrinket(itemStack);
        }
        if (noHelp(trinket, itemStack)) {
            player.sendMessage(Component.text("You are not holding any trinket.", NamedTextColor.RED));
            return;
        }
        if (trinket instanceof Spellbook)
            player.sendMessage(spellbook.getUsage(itemStack));
        else if (trinket instanceof HoldingBundle)
            player.sendMessage(holdingBundle.getUsage(itemStack));
        else
            player.sendMessage(trinket.getUsage());
    }

    private boolean noHelp(Trinket trinket, ItemStack itemStack) {
        return trinket == null || !trinketManager.shouldShowHelp(itemStack);
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
