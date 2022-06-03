package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class BindingPowder extends Trinket {

    private final NamespacedKey ownerKey;

    public BindingPowder(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, NamespacedKey ownerKey) {
        super(plugin, nameKey, powerKey, "bindingPowder", Power.BINDING, true);
        this.ownerKey = ownerKey;
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Binding Powder", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Binds a trinket to you. No one"),
                Component.text("else will be able to use it.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean bindTrinket(ItemStack itemStack, Player player) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(nameKey, PersistentDataType.STRING))
            return false;
        if (container.has(ownerKey, PersistentDataType.STRING))
            return false;
        String name = player.getName();
        container.set(ownerKey, PersistentDataType.STRING, name);
        List<Component> lore = meta.lore();
        if (lore == null)
            meta.lore(List.of(Component.text("Owner: " + name)));
        else {
            ArrayList<Component> newLore = new ArrayList<>(lore.size() + 1);
            newLore.add(Component.text("Owner: " + name));
            newLore.addAll(lore);
            meta.lore(newLore);
        }
        itemStack.setItemMeta(meta);
        return true;
    }
}
