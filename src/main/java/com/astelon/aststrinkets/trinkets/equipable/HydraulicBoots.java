package com.astelon.aststrinkets.trinkets.equipable;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.List;

public class HydraulicBoots extends Trinket {

    public HydraulicBoots(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "hydraulicBoots", TextColor.fromHexString("#73B833"), Power.PREVENT_FALL_DAMAGE,
                false, Usages.WEAR);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.IRON_BOOTS);
        Repairable meta = (Repairable) itemStack.getItemMeta();
        meta.displayName(Component.text("Hydraulic Boots", TextColor.fromHexString("#B87333")));
        meta.lore(List.of(Component.text("These boots are equipped with a"),
                Component.text("hydraulic system that acts as a"),
                Component.text("shock absorber, so you don't take"),
                Component.text("fall damage while wearing them.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
