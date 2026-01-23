package com.astelon.aststrinkets.trinkets.projectile.rocket;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ReignitableRocketPrototype extends FireworkTrinket {

    private boolean pluginExplosion;
    private float explosionPower;
    private boolean setFire;
    private boolean breakBlocks;

    public ReignitableRocketPrototype(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "reignitableRocketPrototype", TextColor.fromHexString("#485D33"),
                Power.REIGNITION_PROTOTYPE, false, Usages.FIREWORK_ROCKET);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Reignitable Rocket Prototype", TextColor.fromHexString("#5D4833")));
        meta.lore(List.of(Component.text("An experimental rocket that can be"),
                Component.text("reused several times before"),
                Component.text("exhaustion. It has a chance to"),
                Component.text("explode on critical failures.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public double getFailureChance(ItemStack itemStack) {
        if (!isTrinket(itemStack))
            throw new IllegalArgumentException("Not a trinket.");
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if (!container.has(keys.failureChanceKey, PersistentDataType.DOUBLE))
            return 0D;
        return container.getOrDefault(keys.failureChanceKey, PersistentDataType.DOUBLE, 0D);
    }

    public double getCriticalFailureChance(ItemStack itemStack) {
        if (!isTrinket(itemStack))
            throw new IllegalArgumentException("Not a trinket.");
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if (!container.has(keys.criticalFailureChanceKey, PersistentDataType.DOUBLE))
            return 0D;
        return container.getOrDefault(keys.criticalFailureChanceKey, PersistentDataType.DOUBLE, 0D);
    }

    public void setFailures(double failureChance, double criticalFailureChance) {
        double normalizedFailureChance = Utils.normalizeRate(failureChance);
        double normalizedCriticalFailureChance = Utils.normalizeRate(criticalFailureChance);
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.failureChanceKey, PersistentDataType.DOUBLE, normalizedFailureChance);
        container.set(keys.criticalFailureChanceKey, PersistentDataType.DOUBLE, normalizedCriticalFailureChance);
        ArrayList<Component> newLore = new ArrayList<>();
        // Use the percentage for display, instead of the normalized values
        newLore.add(Component.text("Failure chance: " + failureChance + "%", NamedTextColor.RED)
                .decoration(TextDecoration.ITALIC, false));
        newLore.add(Component.text("Critical failure chance: " + criticalFailureChance + "%", NamedTextColor.DARK_RED)
                .decoration(TextDecoration.ITALIC, false));
        newLore.addAll(List.of(Component.text("An experimental rocket that can be"),
                Component.text("reused several times before"),
                Component.text("exhaustion. It has a chance to"),
                Component.text("explode on critical failures.")));
        meta.lore(newLore);
        itemStack.setItemMeta(meta);
    }

    public boolean isPluginExplosion() {
        return pluginExplosion;
    }

    public void setPluginExplosion(boolean pluginExplosion) {
        this.pluginExplosion = pluginExplosion;
    }

    //TODO figure a way to deal with multitype trinkets
    public float getExplosionPower(ItemStack rocket) {
        PersistentDataContainer container = rocket.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.explosionPowerKey, PersistentDataType.FLOAT, explosionPower);
    }

    public boolean isSetFire(ItemStack rocket) {
        PersistentDataContainer container = rocket.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.setFireKey, PersistentDataType.BYTE, (byte) (setFire ? 1 : 0)) == (byte) 1;
    }

    public boolean isBreakBlocks(ItemStack rocket) {
        PersistentDataContainer container = rocket.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.breakBlocksKey, PersistentDataType.BYTE, (byte) (breakBlocks ? 1 : 0)) == (byte) 1;
    }

    public void setExplosionPower(float explosionPower) {
        this.explosionPower = explosionPower;
    }

    public void setSetFire(boolean setFire) {
        this.setFire = setFire;
    }

    public void setBreakBlocks(boolean breakBlocks) {
        this.breakBlocks = breakBlocks;
    }
}
