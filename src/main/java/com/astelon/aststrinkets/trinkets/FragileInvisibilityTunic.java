package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.Repairable;

import java.util.List;

public class FragileInvisibilityTunic extends Trinket {

    public FragileInvisibilityTunic(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey) {
        super(plugin, nameKey, powerKey, "fragileInvisibilityTunic", Power.INVISIBILITY, false);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(Color.GRAY);
        meta.displayName(Component.text("Fragile Tunic of Invisibility", NamedTextColor.GRAY));
        meta.lore(List.of(Component.text("This one doesn't seem like it can take"),
                Component.text("many more hits. It still makes the"),
                Component.text("wearer invisible to most people, though.")));
        Repairable repairableMeta = (Repairable) meta;
        repairableMeta.setRepairCost(100);
        Damageable damageableMeta = (Damageable) repairableMeta;
        damageableMeta.setDamage(70);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
