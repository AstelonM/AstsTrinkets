package com.astelon.aststrinkets.trinkets.creature;

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

public class YouthMilk extends CreatureAffectingTrinket {

    public YouthMilk(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "youthMilk", Power.YOUTH, false, Usages.INTERACT_ENTITY_WITH_BABY_FORM);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.MILK_BUCKET);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Milk of Youth", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Feed the milk to a creature and it"),
                Component.text("may become a baby once more."),
                Component.text("Assuming it was once a baby.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
