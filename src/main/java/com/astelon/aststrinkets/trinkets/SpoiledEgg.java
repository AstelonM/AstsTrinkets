package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class SpoiledEgg extends Trinket {

    private final Random random;
    private double infectivity;
    private int minSpread;
    private int maxSpread;
    private double lethality;

    public SpoiledEgg(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "spoiledEgg", Power.VIRUS, true, Usages.DONT);
        random = new Random();
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.EGG);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Spoiled Egg", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("You probably want to throw this away"),
                Component.text("before it spoils your other eggs.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public Set<Integer> spread(ItemStack source, ItemStack[] contents) {
        int amount = random.nextInt(getMinSpread(source), getMaxSpread(source) + 1);
        Set<Integer> slots = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            int index = random.nextInt(contents.length);
            slots.add(index);
            infect(source, contents[index]);
        }
        return slots;
    }

    public void infect(ItemStack source, ItemStack target) {
        if (Utils.isNothing(target) || isTrinket(target))
            return;
        keys.transferKeys(source, target);
    }

    public double getInfectivity(ItemStack spoiledEgg) {
        PersistentDataContainer container = spoiledEgg.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.virusInfectivityKey, PersistentDataType.DOUBLE, infectivity);
    }

    public void setInfectivity(double infectivity) {
        this.infectivity = infectivity;
    }

    public int getMinSpread(ItemStack spoiledEgg) {
        PersistentDataContainer container = spoiledEgg.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.virusMinSpreadKey, PersistentDataType.INTEGER, minSpread);
    }

    public void setMinSpread(int minSpread) {
        this.minSpread = minSpread;
    }

    public int getMaxSpread(ItemStack spoiledEgg) {
        PersistentDataContainer container = spoiledEgg.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.virusMaxSpreadKey, PersistentDataType.INTEGER, maxSpread);
    }

    public void setMaxSpread(int maxSpread) {
        this.maxSpread = maxSpread;
    }

    public double getLethality(ItemStack spoiledEgg) {
        PersistentDataContainer container = spoiledEgg.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.virusLethalityKey, PersistentDataType.DOUBLE, lethality);
    }

    public void setLethality(double lethality) {
        this.lethality = lethality;
    }
}
