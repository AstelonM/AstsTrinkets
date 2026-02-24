package com.astelon.aststrinkets.managers;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class RandomEffectManager {

    private final Random random;
    private final List<PotionEffect> effects;

    public RandomEffectManager() {
        random = new Random();
        effects = new ArrayList<>();
        initEffects();
    }

    private void initEffects() {
        effects.add(new PotionEffect(PotionEffectType.ABSORPTION, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.BAD_OMEN, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.BLINDNESS, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.CONDUIT_POWER, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.CONFUSION, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.GLOWING, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.HARM, 1, 0));
        effects.add(new PotionEffect(PotionEffectType.HEAL, 1, 0));
        effects.add(new PotionEffect(PotionEffectType.HEALTH_BOOST, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.HUNGER, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.JUMP, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.LEVITATION, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.LUCK, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.POISON, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.REGENERATION, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.SATURATION, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.SLOW, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.SLOW_FALLING, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.SPEED, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.UNLUCK, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.WATER_BREATHING, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.WEAKNESS, 600, 0));
        effects.add(new PotionEffect(PotionEffectType.WITHER, 600, 0));
    }

    public PotionEffect getRandomEffect() {
        return effects.get(random.nextInt(effects.size()));
    }
}
