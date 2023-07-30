package com.astelon.aststrinkets.trinkets.creature;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class LifeWater extends CreatureAffectingTrinket {

    public LifeWater(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "lifeWater", Power.INVULNERABILITY, true, Usages.INTERACT_ENTITY_WITH_BABY_FORM);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.WATER));
        meta.displayName(Component.text("Water of Life", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Give this water to a creature to"),
                Component.text("drink and it will become immortal."),
                Component.text("Don't ask how it works on undead but"),
                Component.text("not on players, though.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void makeInvulnerable(LivingEntity entity, Player player) {
        if (entity.isInvulnerable())
            return;
        entity.setInvulnerable(true);
        PersistentDataContainer container = entity.getPersistentDataContainer();
        container.set(keys.invulnerabilitySourceKey, PersistentDataType.STRING, player.getName());
        entity.setRemoveWhenFarAway(false);
    }
}
