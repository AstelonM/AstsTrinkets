package com.astelon.aststrinkets.trinkets.block;

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

public class Spinneret extends Trinket {

    public Spinneret(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "spinneret", Power.PLACE_WEBS, false, Usages.SPINNERET);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.SPIDER_EYE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Spinneret", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("It may look like a spider eye,"),
                Component.text("but it's actually the organ they"),
                Component.text("use to spin webs.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
