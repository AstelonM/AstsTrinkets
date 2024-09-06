package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InvisibilityPowder extends Trinket {

    public InvisibilityPowder(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "invisibilityPowder", TextColor.fromHexString("#C0C0C0"), Power.MAKE_ITEM_FRAMES_INVISIBLE,
                false, Usages.INTERACT_ITEM_FRAME_WITH_MAIN_HAND);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.LIGHT_GRAY_DYE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Invisibility Powder", TextColor.fromHexString("#C0C0C0")));
        meta.lore(List.of(Component.text("Not enough to use on yourself, plus"),
                Component.text("that it's also probably toxic, but"),
                Component.text("you could use it on item frames.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
