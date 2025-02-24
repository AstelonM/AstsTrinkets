package com.astelon.aststrinkets.trinkets.inventory;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ArcaneTome extends EnchantmentChangingTrinket {

    public ArcaneTome(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "arcaneTome", Power.IMPROVE_RANDOM_ENCHANTMENT, false, Usages.INVENTORY);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Arcane Tome", TextColor.fromHexString("#246BCE")));
        meta.lore(List.of(Component.text("If you'd have put some more points"),
                Component.text("in Intelligence, maybe you would"),
                Component.text("have understood how this book works."),
                Component.text("At least you know it improves"),
                Component.text("item enchantments.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
