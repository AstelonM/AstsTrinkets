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

public class IronGolemKit extends Trinket {

    public IronGolemKit(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "ironGolemKit", Power.BUILD_IRON_GOLEM, false, Usages.PLACE_WITH_SPACE);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.BARREL);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Iron Golem Kit", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Ever wanted your very own iron man?"),
                Component.text("Then, this kit is what you need! It"),
                Component.text("contains all the materials needed,"),
                Component.text("and you don't even need to be stuck"),
                Component.text("in a cave.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
