package com.astelon.aststrinkets.trinkets;

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

public class Bait extends Trinket {

    private double efficiency;
    private double consumeChance;

    public Bait(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "bait", Power.SPEED_UP_FISHING, false, Usages.BAIT);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.ROTTEN_FLESH);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Bait", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("It doesn't look great, but fish"),
                Component.text("love it.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = 1 - efficiency;
    }

    public double getConsumeChance() {
        return consumeChance;
    }

    public void setConsumeChance(double consumeChance) {
        this.consumeChance = consumeChance;
    }
}
