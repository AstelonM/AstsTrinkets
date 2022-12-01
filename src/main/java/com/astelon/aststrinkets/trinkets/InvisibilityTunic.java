package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.Repairable;

import java.util.List;

public class InvisibilityTunic extends Trinket {

    public InvisibilityTunic(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey) {
        super(plugin, nameKey, powerKey, "invisibilityTunic", TextColor.fromHexString("#C8C8C8"), Power.INVISIBILITY,
                false, Usages.WEAR);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(Color.BLACK);
        meta.displayName(Component.text("Tunic of Invisibility", NamedTextColor.GRAY));
        meta.lore(List.of(Component.text("The wearer is invisible to everyone"),
                Component.text("but those with the sharpest eyes.")));
        Repairable repairableMeta = (Repairable) meta;
        repairableMeta.setRepairCost(100);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
