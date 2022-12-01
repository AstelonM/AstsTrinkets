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

public class ReignitableRocket extends Trinket {

    private final NamespacedKey failureChanceKey;

    public ReignitableRocket(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, NamespacedKey failureChanceKey) {
        super(plugin, nameKey, powerKey, "reignitableRocket", TextColor.fromHexString("#5FB036"), Power.REIGNITION,
                false, Usages.FIREWORK_ROCKET);
        this.failureChanceKey = failureChanceKey;
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Re-ignitable Rocket", TextColor.fromHexString("#B08836")));
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
        if (!container.has(failureChanceKey, PersistentDataType.DOUBLE))
            return 0D;
        return container.getOrDefault(failureChanceKey, PersistentDataType.DOUBLE, 0D);
    }

    public void setFailureChance(double failureChance) {
        double failureChance1 = Utils.normalizeRate(failureChance);
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(failureChanceKey, PersistentDataType.DOUBLE, failureChance1);
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
