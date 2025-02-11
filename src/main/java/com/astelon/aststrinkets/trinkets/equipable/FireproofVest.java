package com.astelon.aststrinkets.trinkets.equipable;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.Repairable;

import java.util.List;

public class FireproofVest extends Trinket {

    public FireproofVest(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "fireproofVest", TextColor.fromHexString("#00E800"), Power.PREVENT_FIRE_DAMAGE, false,
                Usages.WEAR);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(Color.ORANGE);
        meta.displayName(Component.text("Fireproof Vest", TextColor.fromHexString("#FFA500")));
        meta.lore(List.of(Component.text("Standard issue for those who don't"),
                Component.text("want to get burnt.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
