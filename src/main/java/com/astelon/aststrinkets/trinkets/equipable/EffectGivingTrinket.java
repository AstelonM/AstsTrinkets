package com.astelon.aststrinkets.trinkets.equipable;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public abstract class EffectGivingTrinket extends Trinket {

    protected final PotionEffect effect;

    protected EffectGivingTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour, Power power,
                                  boolean op, String usage, PotionEffect effect) {
        super(plugin, keys, name, infoColour, power, op, usage);
        this.effect = effect;
    }

    protected EffectGivingTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, Power power, boolean op, String usage,
                                  PotionEffect effect) {
        super(plugin, keys, name, power, op, usage);
        this.effect = effect;
    }

    public void addEffect(Player player) {
        player.addPotionEffect(effect);
    }

    public boolean hasEffect(Player player) {
        return player.getActivePotionEffects().contains(effect);
    }

    public boolean isEffect(PotionEffect otherEffect) {
        return effect.equals(otherEffect);
    }

    public void removeEffect(Player player) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }
}
