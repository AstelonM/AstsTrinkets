package com.astelon.aststrinkets.trinkets.inventory;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AdamantineStrand extends Trinket {

    public AdamantineStrand(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "adamantineStrand", TextColor.fromHexString("#8F2b79"), Power.MAKE_ARMOUR_UNBREAKABLE,
                false, Usages.INVENTORY);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.STRING);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Adamantine Strand", TextColor.fromHexString("#7FA8B8")));
        meta.lore(List.of(Component.text("You could make the sharpest weapons"),
                Component.text("and the strongest armours with this,"),
                Component.text("if you knew how to process it."),
                Component.text("At least you can sew it on armour"),
                Component.text("to make it unbreakable.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack makeUnbreakable(ItemStack armour) {
        ItemStack result = armour.asOne();
        ItemMeta meta = result.getItemMeta();
        meta.setUnbreakable(true);
        result.setItemMeta(meta);
        return result;
    }
}
