package com.astelon.aststrinkets.trinkets.creature;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class TaintedLifeWater extends CreatureAffectingTrinket {

    public TaintedLifeWater(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "taintedLifeWater", TextColor.fromHexString("#BFD448"), Power.REMOVE_INVULNERABILITY,
                true, Usages.INTERACT_ENTITY);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.WATER));
        meta.setColor(Color.fromRGB(0XD7B261));
        meta.displayName(Component.text("Tainted Water of Life", TextColor.fromHexString("#D7B261")));
        meta.lore(List.of(Component.text("This foul water was tainted, and"),
                Component.text("instead of granting eternal life,"),
                Component.text("it strips it away from those who"),
                Component.text("have it.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void removeInvulnerability(LivingEntity entity) {
        if (!entity.isInvulnerable())
            return;
        entity.setInvulnerable(false);
    }
}
