package com.astelon.aststrinkets.trinkets.projectile;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class ExplosiveArrow extends ProjectileTrinket {

    public ExplosiveArrow(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "explosiveArrow", TextColor.fromHexString("#00FF00"), Power.CREATE_EXPLOSION, false,
                Usages.ARROW);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(Color.fromRGB(255, 255, 0));
        PotionData potionData = new PotionData(PotionType.INSTANT_DAMAGE);
        meta.setBasePotionData(potionData);
        meta.displayName(Component.text("Explosive Arrow", TextColor.fromHexString("#FFFF00")));
        meta.lore(List.of(Component.text("The tip is made out of an"),
                Component.text("explosive material.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
