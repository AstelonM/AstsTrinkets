package com.astelon.aststrinkets.trinkets.projectile.arrow;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class TrueDeathArrow extends ArrowTrinket {

    public TrueDeathArrow(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "trueDeathArrow", NamedTextColor.WHITE, Power.TRUE_DEATH, true, Usages.ARROW);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(Color.BLACK);
        PotionData potionData = new PotionData(PotionType.INSTANT_DAMAGE);
        meta.setBasePotionData(potionData);
        meta.displayName(Component.text("True Arrow of Death", TextColor.fromHexString("#360000")));
        meta.lore(List.of(Component.text("Through methods unknown, this"),
                Component.text("artifact is able to kill the"),
                Component.text("unkillable. Whatever gets hit by it"),
                Component.text("dies in an instant, even if the"),
                Component.text("arrow bounced off.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
