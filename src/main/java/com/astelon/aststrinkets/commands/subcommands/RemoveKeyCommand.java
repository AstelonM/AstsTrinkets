package com.astelon.aststrinkets.commands.subcommands;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveKeyCommand extends Subcommand {

    public RemoveKeyCommand(AstsTrinkets plugin, TrinketManager trinketManager) {
        super(plugin, trinketManager, "removekey", "aststrinkets.command.removekey");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command cannot be run by console.", NamedTextColor.RED));
            return;
        }
        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = inventory.getItemInMainHand();
        if (itemStack.getType() == Material.AIR)
            itemStack = inventory.getItemInOffHand();
        if (itemStack.getType() == Material.AIR) {
            player.sendMessage(Component.text("You're not holding any item.", NamedTextColor.RED));
            return;
        }
        if (args.length <= 1) {
            sender.sendMessage(Component.text("Correct usage: /trinkets removekey <key>", NamedTextColor.RED));
            return;
        }
        String keyName = args[1];
        NamespacedKeys.KeyTypePair keyTypePair = trinketManager.getKeyTypePair(keyName);
        if (keyTypePair == null) {
            player.sendMessage(Component.text("There's no key with that name.", NamedTextColor.RED));
            return;
        }
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = keyTypePair.key();
        container.remove(key);
        itemStack.setItemMeta(meta);
        player.sendMessage(Component.text("Key " + key.getKey() + " removed.", NamedTextColor.GOLD));
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, String[] args) {
        String keyText = args[1].toLowerCase();
        if (args.length == 2) {
            return trinketManager.getKeys().stream()
                    .map(NamespacedKey::getKey)
                    .filter(keyName -> keyName.startsWith(keyText))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
