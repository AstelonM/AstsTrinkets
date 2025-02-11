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

public class EternalYouthCookie extends CreatureAffectingTrinket {

    public EternalYouthCookie(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "eternalYouthCookie", Power.YOUTH, false, Usages.INTERACT_ENTITY_WITH_BABY_FORM);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.COOKIE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Cookie of Eternal Youth", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Baked with Milk of Youth and other"),
                Component.text("special ingredients, this cookie can"),
                Component.text("turn creatures into their younger"),
                Component.text("selves forever. A blessing for"),
                Component.text("some, a curse for others.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
