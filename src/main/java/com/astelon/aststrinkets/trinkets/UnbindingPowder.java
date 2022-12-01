package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class UnbindingPowder extends Trinket {

    private final NamespacedKey ownerKey;

    public UnbindingPowder(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, NamespacedKey ownerKey) {
        super(plugin, nameKey, powerKey, "unbindingPowder", NamedTextColor.YELLOW, Power.UNBINDING, true,
                Usages.INVENTORY);
        this.ownerKey = ownerKey;
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.PURPLE_DYE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Unbinding Powder", NamedTextColor.DARK_PURPLE));
        meta.lore(List.of(Component.text("Unbinds a trinket from its owner."),
                Component.text("Anyone will be able to use it.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack unbindTrinket(ItemStack itemStack, Trinket trinket) {
        ItemStack result = itemStack.asOne();
        ItemMeta meta = result.getItemMeta();
        if (meta == null)
            return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(nameKey, PersistentDataType.STRING))
            return null;
        if (!container.has(ownerKey, PersistentDataType.STRING))
            return null;
        container.remove(ownerKey);
        List<Component> lore = meta.lore();
        if (lore == null)
            meta.lore(trinket.itemStack.lore());
        else {
            ArrayList<Component> newLore = new ArrayList<>(lore.subList(1, lore.size()));
            meta.lore(newLore);
        }
        result.setItemMeta(meta);
        return result;
    }
}
