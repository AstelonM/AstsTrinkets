package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.inventory.BindingPowder;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class HoldingBundle extends Trinket {

    public HoldingBundle(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "holdingBundle", Power.INFINITE_ITEMS_OF_A_KIND_STORED, false, Usages.BUNDLE);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.BUNDLE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Bundle of Holding", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("This bundle can hold a virtually"),
                Component.text("limitless amount of a single type"),
                Component.text("of item inside, with no weight"),
                Component.text("added. Not like weight was"),
                Component.text("ever an issue.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean hasItems(ItemStack holdingBundle) {
        BundleMeta bundleMeta = (BundleMeta) holdingBundle.getItemMeta();
        return bundleMeta.hasItems();
    }

    public ItemStack getItem(ItemStack holdingBundle) {
        BundleMeta bundleMeta = (BundleMeta) holdingBundle.getItemMeta();
        if (!bundleMeta.hasItems())
            return null;
        List<ItemStack> items = bundleMeta.getItems();
        return items.get(0);
    }

    public ItemStack addItems(ItemStack holdingBundle, ItemStack toAdd) {
        ItemStack result = holdingBundle.asOne();
        ItemMeta meta = result.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int amount = container.getOrDefault(keys.amountKey, PersistentDataType.INTEGER, 0);
        amount += toAdd.getAmount();
        container.set(keys.amountKey, PersistentDataType.INTEGER, amount);
        meta.lore(buildLore(container, amount));
        result.setItemMeta(meta);
        return result;
    }

    public ItemStack removeItems(ItemStack holdingBundle) {
        ItemStack result = holdingBundle.asOne();
        BundleMeta meta = (BundleMeta) result.getItemMeta();
        if (!meta.hasItems())
            return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int amount = container.getOrDefault(keys.amountKey, PersistentDataType.INTEGER, 0);
        if (amount == 0) {
            meta.setItems(null);
            result.setItemMeta(meta);
            return result;
        }
        ItemStack containedItem = meta.getItems().get(0);
        restoreContents(result, meta, container, amount, containedItem);
        return result;
    }

    public void refillBundle(ItemStack holdingBundle, ItemStack contained) {
        BundleMeta meta = (BundleMeta) holdingBundle.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int amount = container.getOrDefault(keys.amountKey, PersistentDataType.INTEGER, 0);
        if (amount == 0)
            return;
        restoreContents(holdingBundle, meta, container, amount, contained);
    }

    private void restoreContents(ItemStack result, BundleMeta meta, PersistentDataContainer container, int amount, ItemStack containedItem) {
        int maxStack = containedItem.getType().getMaxStackSize();
        int toRestore = Math.min(amount, maxStack);
        amount -= toRestore;
        container.set(keys.amountKey, PersistentDataType.INTEGER, amount);
        meta.setItems(List.of(containedItem.asQuantity(toRestore)));
        meta.lore(buildLore(container, amount));
        result.setItemMeta(meta);
    }

    private ArrayList<Component> buildLore(PersistentDataContainer container, int amount) {
        ArrayList<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            String ownerName = container.get(keys.ownerKey, PersistentDataType.STRING);
            newLore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
        }
        newLore.add(Component.text("Extra amount: " + amount, infoColour).decoration(TextDecoration.ITALIC, false));
        newLore.addAll(List.of(Component.text("This bundle can hold a virtually"),
                Component.text("limitless amount of a single type"),
                Component.text("of item inside, with no weight"),
                Component.text("added. Not like weight was"),
                Component.text("ever an issue.")));
        return newLore;
    }
}
