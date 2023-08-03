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
import org.bukkit.persistence.PersistentDataType;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class SetKeyCommand extends Subcommand {

    public SetKeyCommand(AstsTrinkets plugin, TrinketManager trinketManager) {
        super(plugin, trinketManager, "setkey", "aststrinkets.command.setkey");
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
        if (args.length <= 2) {
            sender.sendMessage(Component.text("Correct usage: /trinkets setkey <key> (values)",
                    NamedTextColor.RED));
            return;
        }
        String keyName = args[1];
        NamespacedKeys.KeyTypePair keyTypePair = trinketManager.getKeyTypePair(keyName);
        if (keyTypePair == null) {
            player.sendMessage(Component.text("There's no key with that name.", NamedTextColor.RED));
            return;
        }
        StringBuilder rawValue = new StringBuilder(args[2]);
        for (int i = 3; i < args.length; i++)
            rawValue.append(" ").append(args[i]);
        String value = rawValue.toString();
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = keyTypePair.key();
        PersistentDataType<?, ?> type = keyTypePair.type();
        try {
            if (type.equals(PersistentDataType.STRING))
                container.set(key, PersistentDataType.STRING, value);
            else if (type.equals(PersistentDataType.BYTE_ARRAY))
                container.set(key, PersistentDataType.BYTE_ARRAY, Base64.getDecoder().decode(value));
            else if (type.equals(PersistentDataType.DOUBLE))
                container.set(key, PersistentDataType.DOUBLE, Double.parseDouble(value));
            else if (type.equals(PersistentDataType.LONG))
                container.set(key, PersistentDataType.LONG, Long.parseLong(value));
            else if (type.equals(PersistentDataType.INTEGER))
                container.set(key, PersistentDataType.INTEGER, Integer.parseInt(value));
            else if (type.equals(PersistentDataType.BYTE))
                container.set(key, PersistentDataType.BYTE, Byte.parseByte(value));
            else {
                player.sendMessage(Component.text("Cannot assign the given value to this key.", NamedTextColor.RED));
                return;
            }
        } catch (Exception e) {
            player.sendMessage(Component.text("Cannot assign the given value to this key.", NamedTextColor.RED));
            return;
        }
        itemStack.setItemMeta(meta);
        player.sendMessage(Component.text("Key " + key.getKey() + " assigned.", NamedTextColor.GOLD));
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
