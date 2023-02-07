package com.astelon.aststrinkets.trinkets.equipable;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.List;

public class DivingHelmet extends Trinket {

    public DivingHelmet(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey) {
        super(plugin, nameKey, powerKey, "divingHelmet", TextColor.fromHexString("#73B833"), Power.PREVENT_SUFFOCATION,
                false, Usages.WEAR);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.IRON_HELMET);
        Repairable meta = (Repairable) itemStack.getItemMeta();
        meta.displayName(Component.text("Diving Helmet", TextColor.fromHexString("#B87333")));
        meta.lore(List.of(Component.text("This helmet is able to stop you from"),
                Component.text("drowning. Just don't ask where the"),
                Component.text("air comes from.")));
        meta.setRepairCost(100);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
