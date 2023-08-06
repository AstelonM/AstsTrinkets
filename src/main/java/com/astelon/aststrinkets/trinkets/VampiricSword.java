package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class VampiricSword extends Trinket {

    private final Random random;
    private long cooldown;
    private double useChance;
    private double percentageAbsorbed;

    public VampiricSword(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "vampiricSword", Power.ABSORB_HEALTH_ON_HIT, false, Usages.HIT_AND_DAMAGE);
        random = new Random();
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Vampiric Sword", TextColor.fromHexString("#660000")));
        meta.lore(List.of(Component.text("Through dark magic, the wielder of"),
                Component.text("this sword can absorb the very life"),
                Component.text("force of his enemies.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void tryAbsorbHealth(ItemStack vampiricSword, double damage, LivingEntity entity) {
        if (damage == 0)
            return;
        double roll = random.nextDouble();
        if (roll < getUseChance(vampiricSword)) {
            double health = percentageAbsorbed * damage;
            double maxHealth = Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
            entity.setHealth(Math.min(entity.getHealth() + health, maxHealth));
            setLastUse(vampiricSword);
        }
    }

    public double getUseChance(ItemStack vampiricSword) {
        PersistentDataContainer container = vampiricSword.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.useChanceKey, PersistentDataType.DOUBLE, useChance);
    }

    public boolean canUse(ItemStack vampiricSword) {
        PersistentDataContainer container = vampiricSword.getItemMeta().getPersistentDataContainer();
        if (container.has(keys.lastUseKey, PersistentDataType.LONG)) {
            long lastUsed = container.getOrDefault(keys.lastUseKey, PersistentDataType.LONG, 0L);
            return System.currentTimeMillis() - lastUsed > cooldown;
        }
        return true;
    }

    private void setLastUse(ItemStack vampiricSword) {
        ItemMeta meta = vampiricSword.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.lastUseKey, PersistentDataType.LONG, System.currentTimeMillis());
        vampiricSword.setItemMeta(meta);
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setUseChance(double useChance) {
        this.useChance = useChance;
    }

    public void setPercentageAbsorbed(double percentageAbsorbed) {
        this.percentageAbsorbed = percentageAbsorbed;
    }
}
