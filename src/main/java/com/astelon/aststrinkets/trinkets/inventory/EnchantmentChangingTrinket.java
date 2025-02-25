package com.astelon.aststrinkets.trinkets.inventory;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.MaxLevelBehaviour;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public abstract class EnchantmentChangingTrinket extends Trinket {

    private final Random random;
    private MaxLevelBehaviour maxLevelBehaviour;
    private int basicMaxLevelIncrease;
    private Map<Enchantment, Integer> customMaxLevels;

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
        return switch (getMaxLevelBehaviour(trinket)) {
            case DEFAULT -> enchantments.entrySet().stream()
                    .filter(enchantment -> enchantment.getValue() < enchantment.getKey().getMaxLevel())
                    .map(Map.Entry::getKey)
                    .toList();
            case BOUND_WITH_EXCEPTIONS -> {
                Map<Enchantment, Integer> exceptions = getCustomMaxLevels(trinket);
                int basicIncrease = getBasicMaxLevelIncrease(trinket);
                yield enchantments.entrySet().stream()
                        .filter(entry -> canIncreaseBound(entry, exceptions, basicIncrease))
                        .map(Map.Entry::getKey)
                        .toList();
            }
            case UNBOUND_WITH_EXCEPTIONS -> {
                Map<Enchantment, Integer> exceptions = getCustomMaxLevels(trinket);
                yield enchantments.entrySet().stream()
                        .filter(entry -> canIncreaseUnbound(entry, exceptions))
                        .map(Map.Entry::getKey)
                        .toList();
            }
            case UNBOUND -> new ArrayList<>(enchantments.keySet());
        };
    }

    private boolean canIncreaseBound(Map.Entry<Enchantment, Integer> entry, Map<Enchantment, Integer> exceptions, int basicIncrease) {
        Enchantment enchantment = entry.getKey();
        int level = entry.getValue();
        if (exceptions.get(enchantment) != null) {
            return level < exceptions.get(enchantment);
        } else {
            return level < enchantment.getMaxLevel() + basicIncrease;
        }
    }

    private boolean canIncreaseUnbound(Map.Entry<Enchantment, Integer> entry, Map<Enchantment, Integer> exceptions) {
        Enchantment enchantment = entry.getKey();
        int level = entry.getValue();
        if (exceptions.get(enchantment) != null) {
            return level < exceptions.get(enchantment);
        }
        return true;
    }

    public MaxLevelBehaviour getMaxLevelBehaviour(ItemStack trinket) {
        PersistentDataContainer container = trinket.getItemMeta().getPersistentDataContainer();
        String behaviourName = container.get(keys.behaviourKey, PersistentDataType.STRING);
        if (behaviourName == null)
            return maxLevelBehaviour;
        return MaxLevelBehaviour.getBehaviourByName(behaviourName, maxLevelBehaviour);
    }

    public void setMaxLevelBehaviour(MaxLevelBehaviour maxLevelBehaviour) {
        this.maxLevelBehaviour = maxLevelBehaviour;
    }

    public int getBasicMaxLevelIncrease(ItemStack trinket) {
        PersistentDataContainer container = trinket.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.maxIncreaseKey, PersistentDataType.INTEGER, basicMaxLevelIncrease);
    }

    public void setBasicMaxLevelIncrease(int basicMaxLevelIncrease) {
        this.basicMaxLevelIncrease = basicMaxLevelIncrease;
    }

    public Map<Enchantment, Integer> getCustomMaxLevels(ItemStack trinket) throws IllegalArgumentException {
        PersistentDataContainer container = trinket.getItemMeta().getPersistentDataContainer();
        String customMaxLevelsContent = container.get(keys.customMaxLevelsKey, PersistentDataType.STRING);
        if (customMaxLevelsContent == null)
            return customMaxLevels;
        return deserializeCustomMaxLevels(customMaxLevelsContent);
    }

    private Map<Enchantment, Integer> deserializeCustomMaxLevels(String input) throws IllegalArgumentException {
        Map<Enchantment, Integer> result = new HashMap<>();
        String[] split = input.split(",");
        for (String entry: split) {
            String[] extraSplit = entry.split(":");
            String enchantmentName = extraSplit[0];
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName));
            if (enchantment == null)
                throw new IllegalArgumentException();
            int level = Integer.parseInt(extraSplit[1]);
            result.put(enchantment, level);
        }
        return result;
    }

    public void setCustomMaxLevels(Map<Enchantment, Integer> customMaxLevels) {
        this.customMaxLevels = customMaxLevels;
    }
}
