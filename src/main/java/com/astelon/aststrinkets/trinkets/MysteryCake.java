package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MysteryCake extends Trinket {

    private final ArrayList<PotionEffect> effects;
    private final Random random;
    private boolean checkHealth;

    public MysteryCake(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey) {
        super(plugin, nameKey, powerKey, "mysteryCake", Power.RANDOM_POTION_EFFECT, true, Usages.PLACE_AND_EAT);
        effects = new ArrayList<>();
        random = new Random();
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

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.CAKE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Mystery Cake", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Every bite brings a pleasant"),
                Component.text("(or unpleasant) surprise.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void applyRandomEffect(Player player) {
        PotionEffect effect = effects.get(random.nextInt(effects.size()));
        player.addPotionEffect(effect);
    }

    public boolean isCheckHealth() {
        return checkHealth;
    }

    public void setCheckHealth(boolean checkHealth) {
        this.checkHealth = checkHealth;
    }
}
