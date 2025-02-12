package com.astelon.aststrinkets.trinkets.equipable;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Flippers extends EffectGivingTrinket {

    public Flippers(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "flippers", Power.DOLPHIN_GRACE_AND_SLOWNESS, false, Usages.WEAR, List.of(
                new PotionEffect(PotionEffectType.DOLPHINS_GRACE, -1, 1, false, false),
                new PotionEffect(PotionEffectType.SLOW, -1, 0, false, false)
                )
        );
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(Color.BLUE);
        meta.displayName(Component.text("Flippers", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("A pair of flippers you can wear"),
                Component.text("on your feet to help you swim"),
                Component.text("faster. Also known as swimfins.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
