package com.astelon.aststrinkets.trinkets.rocket;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ReignitableRocket extends Trinket {

    public ReignitableRocket(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "reignitableRocket", TextColor.fromHexString("#5FB036"), Power.REIGNITION,
                false, Usages.FIREWORK_ROCKET);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Reignitable Rocket", TextColor.fromHexString("#B08836")));
        meta.lore(List.of(Component.text("Technological advances allowed for"),
                Component.text("an improved re-ignitable rocket"),
                Component.text("design. This one no longer has a"),
                Component.text("chance to explode.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public double getFailureChance(ItemStack itemStack) {
        if (!isTrinket(itemStack))
            throw new IllegalArgumentException("Not a trinket.");
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if (!container.has(keys.failureChanceKey, PersistentDataType.DOUBLE))
            return 0D;
        return container.getOrDefault(keys.failureChanceKey, PersistentDataType.DOUBLE, 0D);
    }

    public void setFailureChance(double failureChance) {
        double normalizedFailureChance = Utils.normalizeRate(failureChance);
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.failureChanceKey, PersistentDataType.DOUBLE, normalizedFailureChance);
        ArrayList<Component> newLore = new ArrayList<>();
        // Use the percentage for display, instead of the normalized values
        newLore.add(Component.text("Failure chance: " + failureChance + "%", NamedTextColor.RED)
                .decoration(TextDecoration.ITALIC, false));
        newLore.addAll(List.of(Component.text("Technological advances allowed for"),
                Component.text("an improved re-ignitable rocket"),
                Component.text("design. This one no longer has a"),
                Component.text("chance to explode.")));
        meta.lore(newLore);
        itemStack.setItemMeta(meta);
    }
}
