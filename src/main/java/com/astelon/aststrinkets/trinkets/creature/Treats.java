package com.astelon.aststrinkets.trinkets.creature;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Treats extends Trinket {

    public Treats(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "treats", Power.TAME_ANIMALS, false, Usages.INTERACT_WITH_TAMEABLE);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.COCOA_BEANS);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Treats", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Animals go crazy after these."),
                Component.text("They'll love you instantly if you"),
                Component.text(("give them treats."))));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
