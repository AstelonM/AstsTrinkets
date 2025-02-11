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

public class SnowGolemKit extends Trinket {

    public SnowGolemKit(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "snowGolemKit", Power.BUILD_SNOW_GOLEM, false, Usages.PLACE_WITH_SPACE);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.BARREL);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Snow Golem Kit", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Do you want to build a snowman?"),
                Component.text("Then, this kit is for you! Inside"),
                Component.text("you'll find everything you need to"),
                Component.text("build your very own snowman.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
