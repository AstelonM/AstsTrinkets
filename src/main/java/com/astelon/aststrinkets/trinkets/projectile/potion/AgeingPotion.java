package com.astelon.aststrinkets.trinkets.projectile.potion;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.projectile.ProjectileTrinket;
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

public class AgeingPotion extends ProjectileTrinket {

    public AgeingPotion(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "ageingPotion", TextColor.fromHexString("#00FFFF"), Power.MAKE_ADULT, false, Usages.THROW);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.AWKWARD));
        meta.setColor(Color.fromRGB(0x00FF00));
        meta.displayName(Component.text("Ageing Potion", TextColor.fromHexString("#00FF00")));
        meta.lore(List.of(Component.text("A magical elixir that causes"),
                Component.text("creatures to age."),
                Component.text("Warning! Overuse is known to cause"),
                Component.text("loss of childhood.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
