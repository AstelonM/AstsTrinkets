package com.astelon.aststrinkets.trinkets.projectile.arrow;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.managers.RandomEffectManager;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.List;

public class MysteryArrow extends ArrowTrinket {

    private final RandomEffectManager randomEffectManager;

    public MysteryArrow(AstsTrinkets plugin, NamespacedKeys keys, RandomEffectManager randomEffectManager) {
        super(plugin, keys, "mysteryArrow", Power.RANDOM_POTION_EFFECT, false, Usages.ARROW);
        this.randomEffectManager = randomEffectManager;
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(Color.fromRGB(255, 255, 255));
        PotionData potionData = new PotionData(PotionType.UNCRAFTABLE);
        meta.setBasePotionData(potionData);
        meta.displayName(Component.text("Mystery Arrow", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("You might want to avoid the sharp"),
                Component.text("bits of it.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public PotionEffect applyRandomEffect(LivingEntity entity) {
        PotionEffect effect = randomEffectManager.getRandomEffect();
        entity.addPotionEffect(effect);
        return effect;
    }
}
