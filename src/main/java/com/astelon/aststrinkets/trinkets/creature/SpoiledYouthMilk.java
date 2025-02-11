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

public class SpoiledYouthMilk extends CreatureAffectingTrinket {

    public SpoiledYouthMilk(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "spoiledYouthMilk", Power.MAKE_ADULT, false, Usages.INTERACT_WITH_BABY);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.MILK_BUCKET);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Spoiled Milk of Youth", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("This Milk of Youth has fermented,"),
                Component.text("and now ages those who drink it."),
                Component.text("Could probably make some good"),
                Component.text("cheese, though.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
