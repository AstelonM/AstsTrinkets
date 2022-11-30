package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class DeathArrow extends ProjectileTrinket {

    public DeathArrow(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, NamespacedKey ownerKey) {
        super(plugin, nameKey, powerKey, ownerKey, "deathArrow", TextColor.fromHexString("#AA0055"), Power.DEATH,
                false);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(Color.fromRGB(194, 0, 0));
        PotionData potionData = new PotionData(PotionType.INSTANT_DAMAGE);
        meta.setBasePotionData(potionData);
        meta.displayName(Component.text("Arrow of Death", TextColor.fromHexString("#C20000")));
        meta.lore(List.of(Component.text("An arrow capable of killing any"),
                Component.text("creature it manages to hurt"),
                Component.text("instantly.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
