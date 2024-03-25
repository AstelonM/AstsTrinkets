package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemMagnet extends Trinket {

    public ItemMagnet(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "itemMagnet", TextColor.fromHexString("#B0A7AC"), Power.ATTRACT_ITEMS, false,
                Usages.ITEM_MAGNET);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.IRON_INGOT);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Item Magnet", TextColor.fromHexString("#7D7D7D")));
        meta.lore(List.of(Component.text("This small magnet has been altered"),
                Component.text("to attract items.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean canUse(ItemStack itemMagnet) {
        if (!isTrinket(itemMagnet))
            throw new IllegalArgumentException("Not a trinket.");
        PersistentDataContainer container = itemMagnet.getItemMeta().getPersistentDataContainer();
        long lastUse = container.getOrDefault(keys.lastUseKey, PersistentDataType.LONG, 0L);
        return System.currentTimeMillis() - lastUse >= 1000;
    }

    public void use(ItemStack itemMagnet) {
        ItemMeta meta = itemMagnet.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.lastUseKey, PersistentDataType.LONG, System.currentTimeMillis());
        itemMagnet.setItemMeta(meta);
    }

    public int getRange(ItemStack itemMagnet) {
        if (!isTrinket(itemMagnet))
            throw new IllegalArgumentException("Not a trinket.");
        PersistentDataContainer container = itemMagnet.getItemMeta().getPersistentDataContainer();
        if (!container.has(keys.rangeKey, PersistentDataType.INTEGER))
            return 4; //TODO replace with variable
        return container.getOrDefault(keys.rangeKey, PersistentDataType.INTEGER, 4);
    }

    public void setRange(int range) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.rangeKey, PersistentDataType.INTEGER, range);
        ArrayList<Component> newLore = new ArrayList<>();
        newLore.add(Component.text("Range: " + range, this.infoColour)
                .decoration(TextDecoration.ITALIC, false));
        newLore.addAll(List.of(Component.text("This small magnet has been altered"),
                Component.text("to attract items.")));
        meta.lore(newLore);
        itemStack.setItemMeta(meta);
    }
}
