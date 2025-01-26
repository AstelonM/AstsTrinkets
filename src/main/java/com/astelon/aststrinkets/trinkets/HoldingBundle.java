package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.inventory.BindingPowder;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HoldingBundle extends Trinket {

    private Component magnetUsage;

    public HoldingBundle(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "holdingBundle", Power.INFINITE_ITEMS_OF_A_KIND_STORED, false, Usages.BUNDLE);
        magnetUsage = MiniMessage.miniMessage().deserialize("<gold>How to use: <trinketname></gold><br><trinketusage>",
                Placeholder.component("trinketname", this.itemStack.displayName().hoverEvent(this.itemStack.asHoverEvent())),
                Placeholder.parsed("trinketusage", "<green>" + Usages.BUNDLE_WITH_MAGNET));
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

    @Nullable
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
        meta.lore(buildLore(container));
        result.setItemMeta(meta);
        return result;
    }

    public ItemStack removeItems(ItemStack holdingBundle, Player player) {
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
        restoreContents(result, meta, container, amount, containedItem, player, false);
        return result;
    }

    public void refillBundle(ItemStack holdingBundle, ItemStack contained, Player player) {
        BundleMeta meta = (BundleMeta) holdingBundle.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int amount = container.getOrDefault(keys.amountKey, PersistentDataType.INTEGER, 0);
        if (amount == 0)
            return;
        restoreContents(holdingBundle, meta, container, amount, contained, player, true);
    }

    private void restoreContents(ItemStack result, BundleMeta meta, PersistentDataContainer container, int amount,
                                 ItemStack containedItem, Player player, boolean scheduled) {
        if (scheduled && meta.hasItems()) {
            List<ItemStack> contents = meta.getItems();
            if (contents.size() == 1) {
                ItemStack contentItem = contents.get(0);
                if (containedItem.isSimilar(contentItem))
                    return;
            }
            plugin.getLogger().warning("Holding bundle of player " + player.getName() + " has contents " + contents +
                    " which differ from the intended " + containedItem.toString() + ". Overwriting them.");
        }
        int maxStack = containedItem.getType().getMaxStackSize();
        int toRestore = Math.min(amount, maxStack);
        amount -= toRestore;
        container.set(keys.amountKey, PersistentDataType.INTEGER, amount);
        meta.setItems(List.of(containedItem.asQuantity(toRestore)));
        meta.lore(buildLore(container));
        result.setItemMeta(meta);
    }

    public boolean hasMagnet(ItemStack holdingBundle) {
        PersistentDataContainer container = holdingBundle.getItemMeta().getPersistentDataContainer();
        return container.has(keys.rangeKey, PersistentDataType.INTEGER);
    }

    public int getRange(ItemStack holdingBundle) {
        if (!isTrinket(holdingBundle))
            throw new IllegalArgumentException("Not a trinket.");
        PersistentDataContainer container = holdingBundle.getItemMeta().getPersistentDataContainer();
        if (!container.has(keys.rangeKey, PersistentDataType.INTEGER))
            return -1; //TODO replace with variable
        return container.getOrDefault(keys.rangeKey, PersistentDataType.INTEGER, -1);
    }

    public ItemStack addMagnet(ItemStack holdingBundle, int range) {
        ItemStack result = holdingBundle.asOne();
        ItemMeta meta = result.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.rangeKey, PersistentDataType.INTEGER, range);
        meta.displayName(Component.text("Bundle of Holding with Magnet", NamedTextColor.GOLD));
        meta.lore(buildLore(container));
        result.setItemMeta(meta);
        return result;
    }

    private ArrayList<Component> buildLore(PersistentDataContainer container) {
        ArrayList<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            String ownerName = container.get(keys.ownerKey, PersistentDataType.STRING);
            newLore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
        }
        if (container.has(keys.amountKey, PersistentDataType.INTEGER)) {
            int amount = container.getOrDefault(keys.amountKey, PersistentDataType.INTEGER, 0);
            newLore.add(Component.text("Extra amount: " + amount, infoColour).decoration(TextDecoration.ITALIC, false));
        }
        newLore.addAll(List.of(Component.text("This bundle can hold a virtually"),
                Component.text("limitless amount of a single type"),
                Component.text("of item inside, with no weight"),
                Component.text("added. Not like weight was"),
                Component.text("ever an issue.")));
        return newLore;
    }

    public ItemStack addItemsInBundle(ItemStack holdingBundle, ItemStack toAdd, Player player) {
        ItemStack itemStack = getItem(holdingBundle);
        if (itemStack == null) {
            player.sendMessage(Component.text("This Bundle of Holding is corrupted.", NamedTextColor.RED));
            return null;
        }
        if (itemStack.isSimilar(toAdd))
            return addItems(holdingBundle, toAdd);
        else {
            player.sendMessage(Component.text("This bundle has items of a different kind.", NamedTextColor.RED));
        }
        return null;
    }

    public ItemStack addExtraAmountInBundle(ItemStack holdingBundle, int amount) {
        ItemStack result = holdingBundle.asOne();
        ItemMeta meta = result.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int currentAmount = container.getOrDefault(keys.amountKey, PersistentDataType.INTEGER, 0);
        currentAmount += amount;
        container.set(keys.amountKey, PersistentDataType.INTEGER, currentAmount);
        meta.lore(buildLore(container));
        result.setItemMeta(meta);
        return result;
    }

    public Component getUsage(ItemStack holdingBundle) {
        if (hasMagnet(holdingBundle))
            return magnetUsage;
        return super.getUsage();
    }
}
