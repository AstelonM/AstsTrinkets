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

public class SunTotem extends Trinket {

    public SunTotem(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "sunTotem", TextColor.fromHexString("#78AA68"), Power.ADVANCE_TIME_TO_SUNRISE, true,
                Usages.SACRIFICE);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Sun Totem", TextColor.fromHexString("#3EAAD8")));
        meta.lore(List.of(Component.text("The Sun God requires sustenance in"),
                Component.text("the form of human sacrifice, so he"),
                Component.text("may keep his sister and brothers"),
                Component.text("at bay and prevent infinite night.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
