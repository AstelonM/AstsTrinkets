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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class UnbreakableTurtleShell extends Trinket {

    public UnbreakableTurtleShell(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "unbreakableTurtleShell", TextColor.fromHexString("#BF8D22"), Power.INVINCIBILITY,
                true, Usages.WEAR);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.TURTLE_HELMET);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setUnbreakable(true);
        meta.displayName(Component.text("Unbreakable Turtle Shell", TextColor.fromHexString("#6A1B9A")));
        meta.lore(List.of(Component.text("The shell of a long extinct turtle,"),
                Component.text("the years did not make it any"),
                Component.text("weaker.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
