package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TrinketImmunitySponge extends Trinket {

    public TrinketImmunitySponge(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "trinketImmunitySponge", Power.TRINKET_IMMUNITY, true, Usages.INVENTORY_OR_INTERACT);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.WET_SPONGE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Sponge of Trinket Immunity", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Using this sponge on any amount of"),
                Component.text("items or entities will make them"),
                Component.text("immune to trinkets that directly"),
                Component.text("affect them.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
