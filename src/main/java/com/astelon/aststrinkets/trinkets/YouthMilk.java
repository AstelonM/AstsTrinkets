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

public class YouthMilk extends PetCheckingTrinket {

    public YouthMilk(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey) {
        super(plugin, nameKey, powerKey, "youthMilk", Power.YOUTH, false);
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
