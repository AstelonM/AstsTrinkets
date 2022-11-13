package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

public class TrueInvisibilityTunic extends Trinket {

    public TrueInvisibilityTunic(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey) {
        super(plugin, nameKey, powerKey, "trueInvisibilityTunic", NamedTextColor.GOLD, Power.TRUE_INVISIBILITY,
                true);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setUnbreakable(true);
        meta.setColor(Color.fromRGB(38, 50, 101));
        meta.displayName(Component.text("The True Tunic of Invisibility", NamedTextColor.DARK_BLUE));
        meta.lore(List.of(Component.text("Much stronger and more durable than the"),
                Component.text().append(Component.text("Tunic of Invisibility", NamedTextColor.GRAY))
                        .append(Component.text(", this one makes")).build(),
                Component.text("the wearer invisible to everyone,"),
                Component.text("no matter how good their eyesight is.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
