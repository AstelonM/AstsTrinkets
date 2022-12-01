package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.Usages;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ReignitableRocketPrototype extends Trinket {

    private final NamespacedKey failureChanceKey;
    private final NamespacedKey criticalFailureChanceKey;

    public ReignitableRocketPrototype(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey,
                                      NamespacedKey failureChanceKey, NamespacedKey criticalFailureChanceKey) {
        super(plugin, nameKey, powerKey, "reignitableRocketPrototype", TextColor.fromHexString("#485D33"),
                Power.REIGNITION_PROTOTYPE, false, Usages.FIREWORK_ROCKET);
        this.failureChanceKey = failureChanceKey;
        this.criticalFailureChanceKey = criticalFailureChanceKey;
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Re-ignitable Rocket Prototype", TextColor.fromHexString("#5D4833")));
        meta.lore(List.of(Component.text("An experimental rocket that can be"),
                Component.text("reused several times before"),
                Component.text("exhaustion. It has a chance to"),
                Component.text("explode on critical failures.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public double getFailureChance(ItemStack itemStack) {
        if (!isTrinket(itemStack))
            throw new IllegalArgumentException("Not a trinket.");
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if (!container.has(failureChanceKey, PersistentDataType.DOUBLE))
            return 0D;
        return container.getOrDefault(failureChanceKey, PersistentDataType.DOUBLE, 0D);
    }

    public double getCriticalFailureChance(ItemStack itemStack) {
        if (!isTrinket(itemStack))
            throw new IllegalArgumentException("Not a trinket.");
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if (!container.has(criticalFailureChanceKey, PersistentDataType.DOUBLE))
            return 0D;
        return container.getOrDefault(criticalFailureChanceKey, PersistentDataType.DOUBLE, 0D);
    }

    public void setFailures(double failureChance, double criticalFailureChance) {
        double failureChance1 = Utils.normalizeRate(failureChance);
        double criticalFailureChance1 = Utils.normalizeRate(criticalFailureChance);
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(failureChanceKey, PersistentDataType.DOUBLE, failureChance1);
        container.set(criticalFailureChanceKey, PersistentDataType.DOUBLE, criticalFailureChance1);
        ArrayList<Component> newLore = new ArrayList<>();
        // Use the percentage for display, instead of the normalized values
        newLore.add(Component.text("Failure chance: " + failureChance + "%", NamedTextColor.RED)
                .decoration(TextDecoration.ITALIC, false));
        newLore.add(Component.text("Critical failure chance: " + criticalFailureChance + "%", NamedTextColor.DARK_RED)
                .decoration(TextDecoration.ITALIC, false));
        newLore.addAll(List.of(Component.text("An experimental rocket that can be"),
                Component.text("reused several times before"),
                Component.text("exhaustion. It has a chance to"),
                Component.text("explode on critical failures.")));
        meta.lore(newLore);
        itemStack.setItemMeta(meta);
    }
}
