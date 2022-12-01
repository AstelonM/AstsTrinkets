package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Homendirt extends Trinket {

    public Homendirt(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey) {
        super(plugin, nameKey, powerKey, "homendirt", TextColor.fromHexString("#3AFC3A"), Power.ABSORB_MENDING,
                true, Usages.INVENTORY);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.DIRT);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Homendirt", NamedTextColor.YELLOW));
        meta.lore(List.of(
                Component.text("A mythical piece of dirt"),
                Component.text("created by Homen06 in a time"),
                Component.text("before time. The full extent"),
                Component.text("of its powers is unknown, though"),
                Component.text("it has an affinity for mending.")
        ));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack removeMending(ItemStack item) {
        ItemStack result = item.asOne();
        if (result.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) result.getItemMeta();
            meta.removeStoredEnchant(Enchantment.MENDING);
            result.setItemMeta(meta);
        } else {
            result.removeEnchantment(Enchantment.MENDING);
        }
        return result;
    }
}
