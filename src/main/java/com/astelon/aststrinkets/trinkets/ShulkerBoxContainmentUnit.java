package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ShulkerBoxContainmentUnit extends Trinket {

    private final NamespacedKey shulkerBoxKey;
    private final NamespacedKey ownerKey;

    public ShulkerBoxContainmentUnit(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, NamespacedKey shulkerBoxKey,
                                     NamespacedKey ownerKey) {
        super(plugin, nameKey, powerKey, "shulkerBoxContainmentUnit", NamedTextColor.YELLOW, Power.STORE_SHULKER_BOXES,
                false);
        this.shulkerBoxKey = shulkerBoxKey;
        this.ownerKey = ownerKey;
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Shulker Box Containment Unit", NamedTextColor.BLUE));
        meta.lore(List.of(Component.text("This chest contains a pocket"),
                Component.text("dimension in which you can store a"),
                Component.text("shulker box. It can then be placed"),
                Component.text("in another shulker box.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean hasShulkerBox(ItemStack itemStack) {
        if (!isTrinket(itemStack))
            throw new IllegalArgumentException("Not a trinket");
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        return container.has(shulkerBoxKey, PersistentDataType.BYTE_ARRAY);
    }

    public boolean canContainShulkerBox(ItemStack shulkerBox) {
        if (!Utils.isShulkerBox(shulkerBox))
            throw new IllegalArgumentException("Not a shulker box");
        BlockStateMeta meta = (BlockStateMeta) shulkerBox.getItemMeta();
        if (meta == null || !meta.hasBlockState())
            return false;
        ShulkerBox box = (ShulkerBox) meta.getBlockState();
        for (ItemStack item: box.getInventory().getContents()) {
            if (isTrinket(item))
                return false;
        }
        return true;
    }

    public ItemStack setContainedShulkerBox(ItemStack trinket, ItemStack shulkerBox) {
        if (!isTrinket(trinket))
            throw new IllegalArgumentException("Not a trinket");
        if (!Utils.isShulkerBox(shulkerBox))
            throw new IllegalArgumentException("Not a shulker box");
        if (hasShulkerBox(trinket))
            return null;
        ItemStack result = trinket.asOne();
        byte[] serializedShulkerBox = shulkerBox.serializeAsBytes();
        ItemMeta meta = result.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(shulkerBoxKey, PersistentDataType.BYTE_ARRAY, serializedShulkerBox);
        meta.displayName(Component.text("Filled Shulker Box Containment Unit", NamedTextColor.BLUE)
                .decoration(TextDecoration.ITALIC, false));
        ArrayList<Component> newLore = new ArrayList<>();
        if (container.has(ownerKey, PersistentDataType.STRING)) {
            String ownerName = container.get(ownerKey, PersistentDataType.STRING);
            newLore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
        }
        String blockName = Utils.getBlockName(shulkerBox.getType());
        ItemMeta shulkerMeta = shulkerBox.getItemMeta();
        Component shulkerDisplayName = null;
        if (shulkerMeta != null) {
            shulkerDisplayName = shulkerMeta.displayName();
        }
        TextComponent shulkerName = Component.text("Contains: " + blockName, infoColour);
        if (shulkerDisplayName != null)
            shulkerName = shulkerName.append(Component.text(" named ", infoColour)).append(shulkerDisplayName);
        newLore.add(shulkerName.decoration(TextDecoration.ITALIC, false));
        newLore.addAll(this.itemStack.lore());
        meta.lore(newLore);
        result.setItemMeta(meta);
        return result;
    }

    public ItemStack getContainedShulkerBox(ItemStack trinket) {
        if (!isTrinket(trinket))
            throw new IllegalArgumentException("Not a trinket");
        PersistentDataContainer container = trinket.getItemMeta().getPersistentDataContainer();
        if (!container.has(shulkerBoxKey, PersistentDataType.BYTE_ARRAY))
            return null;
        byte[] result = container.get(shulkerBoxKey, PersistentDataType.BYTE_ARRAY);
        return ItemStack.deserializeBytes(result);
    }
}
