package com.astelon.aststrinkets.commands.subcommands;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.astelon.aststrinkets.utils.Utils.isNothing;

public class DebugCommand extends Subcommand {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public DebugCommand(AstsTrinkets plugin, TrinketManager trinketManager) {
        super(plugin, trinketManager, "debug", "aststrinkets.command.debug");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command cannot be run by console.", NamedTextColor.RED));
            return;
        }
        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = inventory.getItemInMainHand();
        HashMap<String, Object> keys = getKeys(itemStack);
        if (keys == null) {
            itemStack = inventory.getItemInOffHand();
            keys = getKeys(itemStack);
            if (keys == null) {
                player.sendMessage(Component.text("The items you're holding have no trinket keys set.", NamedTextColor.GREEN));
                return;
            }
        }
        StringBuilder result = new StringBuilder("<gold>Keys present on <itemstack>:");
        for (Map.Entry<String, Object> entry: keys.entrySet()) {
            String value = getValueAsString(entry.getValue());
            result.append("<br><green>").append(entry.getKey()).append(": <yellow>").append(value);
        }
        player.sendMessage(miniMessage.deserialize(result.toString(),
                Placeholder.component("itemstack", itemStack.displayName().hoverEvent(itemStack.asHoverEvent()))));
    }

    private HashMap<String, Object> getKeys(ItemStack itemStack) {
        if (!isNothing(itemStack)) {
            HashMap<String, Object> keys = trinketManager.getPresentKeys(itemStack);
            if (keys.isEmpty())
                return null;
            return keys;
        }
        return null;
    }

    private String getValueAsString(Object value) {
        if (value instanceof String string)
            return string;
        else if (value instanceof byte[] bytes)
            return Base64.getEncoder().encodeToString(bytes);
        else
            return value.toString();
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
