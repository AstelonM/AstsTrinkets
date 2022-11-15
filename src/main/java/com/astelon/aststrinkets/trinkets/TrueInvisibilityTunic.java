package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
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
        meta.setColor(Color.fromRGB(44, 48, 101));
        meta.displayName(Component.text("The True Tunic of Invisibility", TextColor.fromHexString("#2C3065")));
        meta.lore(List.of(Component.text("Far more powerful than the Tunic"),
                Component.text("of Invisibility, this unbreakable"),
                Component.text("artifact will make the wearer"),
                Component.text("invisible to everyone, no matter"),
                Component.text("how good their eyesight is.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
