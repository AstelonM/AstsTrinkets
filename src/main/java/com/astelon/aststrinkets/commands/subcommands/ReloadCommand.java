package com.astelon.aststrinkets.commands.subcommands;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends Subcommand {

    public ReloadCommand(AstsTrinkets plugin, TrinketManager trinketManager) {
        super(plugin, trinketManager, "reload", "aststrinkets.command.reload");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        plugin.reload();
        sender.sendMessage(Component.text("Ast's Trinkets reloaded!", NamedTextColor.GOLD));
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
