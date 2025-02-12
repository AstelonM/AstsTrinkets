package com.astelon.aststrinkets.trinkets.equipable;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public abstract class EffectGivingTrinket extends Trinket {

    protected final List<PotionEffect> effects;

    protected EffectGivingTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour, Power power,
                                  boolean op, String usage, List<PotionEffect> effects) {
        super(plugin, keys, name, infoColour, power, op, usage);
        this.effects = effects;
    }

    protected EffectGivingTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, Power power, boolean op, String usage,
                                  List<PotionEffect> effects) {
        super(plugin, keys, name, power, op, usage);
        this.effects = effects;
    }

    public void addEffects(Player player) {
        for (PotionEffect effect : effects) {
            player.addPotionEffect(effect);
        }
    }

    public boolean hasEffects(Player player) {
        return player.getActivePotionEffects().containsAll(effects);
    }

    public boolean isEffect(PotionEffect otherEffect) {
        return effects.contains(otherEffect);
    }

    public void removeEffects(Player player) {
        for (PotionEffect effect : effects) {
            player.removePotionEffect(effect.getType());
        }
    }
}
