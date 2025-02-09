package com.astelon.aststrinkets.trinkets.creature;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CuringApple extends CreatureAffectingTrinket {

    private int minDuration;
    private int maxDuration;
    private double bruteChance;

    public CuringApple(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "curingApple", Power.NETHER_ZOMBIFICATION_REVERSAL, false, Usages.INTERACT_NETHER_ZOMBIE);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Curing Apple", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Nether zombies need something more"),
                Component.text("potent than Overworld ones.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public int getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(int minDuration) {
        this.minDuration = minDuration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public double getBruteChance() {
        return bruteChance;
    }

    public void setBruteChance(double bruteChance) {
        this.bruteChance = bruteChance;
    }
}
