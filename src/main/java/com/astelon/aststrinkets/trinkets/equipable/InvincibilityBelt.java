package com.astelon.aststrinkets.trinkets.equipable;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

public class InvincibilityBelt extends Trinket {

    public InvincibilityBelt(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "invincibilityBelt", NamedTextColor.GOLD, Power.INVINCIBILITY, true, Usages.WEAR);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setUnbreakable(true);
        meta.setColor(Color.fromRGB(44, 48, 101));
        meta.displayName(Component.text("Belt of Invincibility", TextColor.fromHexString("#2C3065")));
        meta.lore(List.of(Component.text("It is said that those who wear the"),
                Component.text("belt cannot be harmed in any way."),
                Component.text("It also comes with a nice pair of"),
                Component.text("pants.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
