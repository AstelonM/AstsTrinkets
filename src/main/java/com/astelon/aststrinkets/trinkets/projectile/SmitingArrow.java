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

public class SmitingArrow extends ProjectileTrinket {

    public SmitingArrow(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "smitingArrow", TextColor.fromHexString("#FF7A7A"), Power.SUMMON_LIGHTNING, false,
                Usages.ARROW);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(Color.fromRGB(122, 122, 255));
        PotionData potionData = new PotionData(PotionType.INSTANT_DAMAGE);
        meta.setBasePotionData(potionData);
        meta.displayName(Component.text("Arrow of Smiting", TextColor.fromHexString("#7A7AFF")));
        meta.lore(List.of(Component.text("Your foes will feel the wrath"),
                Component.text("of the sky.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
