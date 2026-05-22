package com.astelon.aststrinkets.trinkets.equipable;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class FrogLegs extends EffectGivingTrinket {

    public FrogLegs(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "frogLegs", TextColor.fromHexString("#12B04A"), Power.JUMP_BOOST, false, Usages.WEAR,
                List.of(new PotionEffect(PotionEffectType.JUMP, -1, 4, false, false)));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(Color.fromRGB(0x72DE09));
        meta.displayName(Component.text("Frog Legs", TextColor.fromHexString("#72DE09")));
        meta.lore(List.of(Component.text("Contrary to their name, they are"),
                Component.text("not actual frog legs. They just"),
                Component.text("allow you to jump really high.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
