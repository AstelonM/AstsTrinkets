package com.astelon.aststrinkets.trinkets.block;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.managers.RandomEffectManager;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MysteryCake extends Trinket {

    private final RandomEffectManager randomEffectManager;
    private boolean checkHealth;
    private boolean ignoreBlockRestrictions;
    private boolean useLowestPriorityListener;
    private boolean consumeCakeEnabled;

    public MysteryCake(AstsTrinkets plugin, NamespacedKeys keys, RandomEffectManager randomEffectManager) {
        super(plugin, keys, "mysteryCake", Power.RANDOM_POTION_EFFECT, true, Usages.PLACE_AND_EAT);
        this.randomEffectManager = randomEffectManager;
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

    public PotionEffect applyRandomEffect(Player player) {
        PotionEffect effect = randomEffectManager.getRandomEffect();
        player.addPotionEffect(effect);
        return effect;
    }

    public boolean isCheckHealth() {
        return checkHealth;
    }

    public void setCheckHealth(boolean checkHealth) {
        this.checkHealth = checkHealth;
    }

    public boolean isIgnoreBlockRestrictions() {
        return ignoreBlockRestrictions;
    }

    public void setIgnoreBlockRestrictions(boolean ignoreBlockRestrictions) {
        this.ignoreBlockRestrictions = ignoreBlockRestrictions;
    }

    public boolean isUseLowestPriorityListener() {
        return useLowestPriorityListener;
    }

    public void setUseLowestPriorityListener(boolean useLowestPriorityListener) {
        this.useLowestPriorityListener = useLowestPriorityListener;
    }

    public boolean isConsumeCakeEnabled() {
        return consumeCakeEnabled;
    }

    public void setConsumeCakeEnabled(boolean consumeCakeEnabled) {
        this.consumeCakeEnabled = consumeCakeEnabled;
    }
}
