package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class PlayerMagnet extends Trinket {

    public PlayerMagnet(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "playerMagnet", Power.ATTRACT_PLAYERS, true, Usages.SHIFT_RIGHT_CLICK);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.NETHERITE_INGOT);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Player Magnet", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Players find this irresistible."),
                Component.text("Not like they have a choice.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean canUse(ItemStack playerMagnet) {
        if (!isTrinket(playerMagnet))
            throw new IllegalArgumentException("Not a trinket.");
        PersistentDataContainer container = playerMagnet.getItemMeta().getPersistentDataContainer();
        long lastUse = container.getOrDefault(keys.lastUseKey, PersistentDataType.LONG, 0L);
        return System.currentTimeMillis() - lastUse >= 1000;
    }

    public void use(ItemStack playerMagnet) {
        ItemMeta meta = playerMagnet.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.lastUseKey, PersistentDataType.LONG, System.currentTimeMillis());
        playerMagnet.setItemMeta(meta);
    }

    public int getRange(ItemStack playerMagnet) {
        if (!isTrinket(playerMagnet))
            throw new IllegalArgumentException("Not a trinket.");
        PersistentDataContainer container = playerMagnet.getItemMeta().getPersistentDataContainer();
        if (!container.has(keys.rangeKey, PersistentDataType.INTEGER))
            return 4;
        return container.getOrDefault(keys.rangeKey, PersistentDataType.INTEGER, 4);
    }

    public void setRange(int range) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.rangeKey, PersistentDataType.INTEGER, range);
        ArrayList<Component> newLore = new ArrayList<>();
        newLore.add(Component.text("Range: " + range, this.infoColour)
                .decoration(TextDecoration.ITALIC, false));
        newLore.addAll(List.of(Component.text("Players find this irresistible."),
                Component.text("Not like they have a choice.")));
        meta.lore(newLore);
        itemStack.setItemMeta(meta);
    }
}
