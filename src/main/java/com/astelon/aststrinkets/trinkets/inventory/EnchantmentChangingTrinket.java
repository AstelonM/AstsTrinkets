package com.astelon.aststrinkets.trinkets.inventory;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class EnchantmentChangingTrinket extends Trinket {

    private final Random random;
    private boolean ignoreMaxLevels;

    public EnchantmentChangingTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour, Power power, boolean op, String usage) {
        super(plugin, keys, name, infoColour, power, op, usage);
        random = new Random();
    }

    public EnchantmentChangingTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, Power power, boolean op, String usage) {
        super(plugin, keys, name, power, op, usage);
         random = new Random();
    }

    public ItemStack improveEnchantment(ItemStack trinket, ItemStack toImprove) {
        ItemStack result = toImprove.asOne();
        if (result.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) result.getItemMeta();
            List<Enchantment> enchantments = new ArrayList<>(meta.getStoredEnchants().keySet());
            if (enchantments.isEmpty()) {
                if (improveActualEnchantments(trinket, result))
                    return result;
                return null;
            }
            enchantments = getAllowedEnchantments(trinket, meta.getStoredEnchants());
            if (enchantments.isEmpty())
                return null;
            Enchantment randomEnchantment = enchantments.get(random.nextInt(enchantments.size()));
            int level = meta.getStoredEnchantLevel(randomEnchantment);
            meta.addStoredEnchant(randomEnchantment, level + 1, true);
            result.setItemMeta(meta);
        } else {
            if (improveActualEnchantments(trinket, result))
                return result;
            return null;
        }
        return result;
    }

    private boolean improveActualEnchantments(ItemStack trinket, ItemStack itemStack) {
        List<Enchantment> enchantments = getAllowedEnchantments(trinket, itemStack.getEnchantments());
        if (enchantments.isEmpty())
            return false;
        Enchantment randomEnchantment = enchantments.get(random.nextInt(enchantments.size()));
        int level = itemStack.getEnchantments().get(randomEnchantment);
        itemStack.addUnsafeEnchantment(randomEnchantment, level + 1);
        return true;
    }

    private List<Enchantment> getAllowedEnchantments(ItemStack trinket, Map<Enchantment, Integer> enchantments) {
        if (isBypassMaxLevels(trinket))
            return new ArrayList<>(enchantments.keySet());
        return enchantments.entrySet().stream()
                .filter(enchantment -> enchantment.getValue() < enchantment.getKey().getMaxLevel())
                .map(Map.Entry::getKey)
                .toList();
    }

    public boolean isBypassMaxLevels(ItemStack trinket) {
        PersistentDataContainer container = trinket.getItemMeta().getPersistentDataContainer();
        byte contained = container.getOrDefault(keys.ignoreMaxLevelsKey, PersistentDataType.BYTE, (byte) -1);
        return contained == -1 ? ignoreMaxLevels : contained == 1;
    }

    public void setIgnoreMaxLevels(boolean ignoreMaxLevels) {
        this.ignoreMaxLevels = ignoreMaxLevels;
    }
}
