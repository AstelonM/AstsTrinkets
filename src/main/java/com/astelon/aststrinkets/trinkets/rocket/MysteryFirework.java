package com.astelon.aststrinkets.trinkets.rocket;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MysteryFirework extends FireworkTrinket {

    private final Random random;
    private int minFlightDuration;
    private int maxFlightDuration;
    private int minEffectAmount;
    private int maxEffectAmount;
    private int minPrimaryColours;
    private int maxPrimaryColours;
    private int minFadeColours;
    private int maxFadeColours;
    private boolean allowCustomColours;

    public MysteryFirework(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "mysteryFirework", Power.RANDOM_FIREWORK, false, Usages.FIREWORK);
        setAllowUseAsFirework(true);
        random = new Random();
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Mystery Firework", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("A must have for parties.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void setRandomEffects(Firework firework) {
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(random.nextInt(minFlightDuration, maxFlightDuration + 1));
        int effects = random.nextInt(minEffectAmount, maxEffectAmount + 1);
        for (int i = 0; i < effects; i++) {
            FireworkEffect.Builder builder = FireworkEffect.builder();
            builder.with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)]);
            if (random.nextBoolean())
                builder.withFlicker();
            if (random.nextBoolean())
                builder.withTrail();
            int primaryColours = random.nextInt(minPrimaryColours, maxPrimaryColours + 1);
            builder.withColor(generateColours(primaryColours));
            int fadeColours = random.nextInt(minFadeColours, maxFadeColours + 1);
            if (fadeColours != 0)
                builder.withFade(generateColours(fadeColours));
            meta.addEffect(builder.build());
        }
        firework.setFireworkMeta(meta);
    }

    private List<Color> generateColours(int amount) {
        List<Color> result = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            if (allowCustomColours && random.nextBoolean())
                result.add(Utils.generateRandomColour(random));
            else
                result.add(Utils.getRandomBaseColour(random));
        }
        return result;
    }

    public int getMinFlightDuration() {
        return minFlightDuration;
    }

    public void setMinFlightDuration(int minFlightDuration) {
        this.minFlightDuration = minFlightDuration;
    }

    public int getMaxFlightDuration() {
        return maxFlightDuration;
    }

    public void setMaxFlightDuration(int maxFlightDuration) {
        this.maxFlightDuration = maxFlightDuration;
    }

    public int getMinEffectAmount() {
        return minEffectAmount;
    }

    public void setMinEffectAmount(int minEffectAmount) {
        this.minEffectAmount = minEffectAmount;
    }

    public int getMaxEffectAmount() {
        return maxEffectAmount;
    }

    public void setMaxEffectAmount(int maxEffectAmount) {
        this.maxEffectAmount = maxEffectAmount;
    }

    public int getMinPrimaryColours() {
        return minPrimaryColours;
    }

    public void setMinPrimaryColours(int minPrimaryColours) {
        this.minPrimaryColours = minPrimaryColours;
    }

    public int getMaxPrimaryColours() {
        return maxPrimaryColours;
    }

    public void setMaxPrimaryColours(int maxPrimaryColours) {
        this.maxPrimaryColours = maxPrimaryColours;
    }

    public int getMinFadeColours() {
        return minFadeColours;
    }

    public void setMinFadeColours(int minFadeColours) {
        this.minFadeColours = minFadeColours;
    }

    public int getMaxFadeColours() {
        return maxFadeColours;
    }

    public void setMaxFadeColours(int maxFadeColours) {
        this.maxFadeColours = maxFadeColours;
    }

    public boolean isAllowCustomColours() {
        return allowCustomColours;
    }

    public void setAllowCustomColours(boolean allowCustomColours) {
        this.allowCustomColours = allowCustomColours;
    }
}
