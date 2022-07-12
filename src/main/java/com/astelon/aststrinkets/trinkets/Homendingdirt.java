package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Homendingdirt extends Trinket {

    private final NamespacedKey ownerKey;

    public Homendingdirt(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, NamespacedKey ownerKey) {
        super(plugin, nameKey, powerKey, "homendingdirt", Power.APPLY_MENDING, true);
        this.ownerKey = ownerKey;
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.DIRT);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Homendingdirt", NamedTextColor.GOLD));
        meta.lore(List.of(
                Component.text("The result of Homendirt absorbing"),
                Component.text("mending from an item. Legend has"),
                Component.text("it that by sacrificing the"),
                Component.text("Homendingdirt, the mending it"),
                Component.text("has absorbed can be applied to"),
                Component.text("another item.")
        ));
        itemStack.setItemMeta(meta);
        itemStack.addUnsafeEnchantment(Enchantment.MENDING, 1);
        return itemStack;
    }

    public ItemStack createHomendingdirt(ItemStack homendirt, int mendingLevel) {
        ItemStack result = homendirt.asOne();
        ItemMeta meta = result.getItemMeta();
        if (meta == null)
            throw new IllegalArgumentException("Not a Homendirt.");
        meta.displayName(Component.text("Homendingdirt", NamedTextColor.GOLD));
        PersistentDataContainer container = meta.getPersistentDataContainer();
        ArrayList<Component> lore = new ArrayList<>();
        if (container.has(ownerKey, PersistentDataType.STRING)) {
            List<Component> oldLore = meta.lore();
            if (oldLore != null && oldLore.size() >= 1)
                lore.add(oldLore.get(0));
        }
        lore.addAll(List.of(
                Component.text("The result of Homendirt absorbing"),
                Component.text("mending from an item. Legend has"),
                Component.text("it that by sacrificing the"),
                Component.text("Homendingdirt, the mending it"),
                Component.text("has absorbed can be applied to"),
                Component.text("another item.")
        ));
        meta.lore(lore);

        container.set(nameKey, PersistentDataType.STRING, name);
        container.set(powerKey, PersistentDataType.STRING, power.powerName());
        result.setItemMeta(meta);
        result.addUnsafeEnchantment(Enchantment.MENDING, mendingLevel);
        return result;
    }

    public ItemStack addMending(ItemStack item, ItemStack source) {
        int mendingLevel = source.getEnchantmentLevel(Enchantment.MENDING);
        ItemStack result = item.asOne();
        result.addUnsafeEnchantment(Enchantment.MENDING, mendingLevel);
        return result;
    }
}
