package com.astelon.aststrinkets.trinkets.equipable;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class AdvancedFlippers extends EffectGivingTrinket {

    public AdvancedFlippers(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "advancedFlippers", Power.DOLPHIN_GRACE, false, Usages.WEAR, List.of(
                new PotionEffect(PotionEffectType.DOLPHINS_GRACE, -1, 2, false, false)));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(Color.YELLOW);
        meta.displayName(Component.text("Advanced Flippers", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("The fins of these flippers are"),
                Component.text("retractable, allowing you to walk on"),
                Component.text("land without issues.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
