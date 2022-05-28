package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MendingPowder extends Trinket {

    public MendingPowder(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey) {
        super(plugin, nameKey, powerKey, "mendingPowder", Power.MENDING, true);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Mending Powder", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Enough to repair one item of"),
                Component.text("any kind.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
