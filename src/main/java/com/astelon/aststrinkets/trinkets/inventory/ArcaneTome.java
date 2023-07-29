package com.astelon.aststrinkets.trinkets.inventory;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArcaneTome extends Trinket {

    private final Random random;

    public ArcaneTome(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "arcaneTome", Power.IMPROVE_RANDOM_ENCHANTMENT, false, Usages.INVENTORY);
        random = new Random();
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

    public ItemStack improveEnchantment(ItemStack toImprove) {
        ItemStack result = toImprove.asOne();
        if (result.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) result.getItemMeta();
            ArrayList<Enchantment> enchantments = new ArrayList<>(meta.getStoredEnchants().keySet());
            if (enchantments.isEmpty()) {
                improveActualEnchantments(result);
                return result;
            }
            Enchantment randomEnchantment = enchantments.get(random.nextInt(enchantments.size()));
            int level = meta.getStoredEnchantLevel(randomEnchantment);
            meta.addStoredEnchant(randomEnchantment, level + 1, true);
            result.setItemMeta(meta);
        } else {
            improveActualEnchantments(result);
        }
        return result;
    }

    private void improveActualEnchantments(ItemStack itemStack) {
        ArrayList<Enchantment> enchantments = new ArrayList<>(itemStack.getEnchantments().keySet());
        Enchantment randomEnchantment = enchantments.get(random.nextInt(enchantments.size()));
        int level = itemStack.getEnchantments().get(randomEnchantment);
        itemStack.addUnsafeEnchantment(randomEnchantment, level + 1);
    }
}
