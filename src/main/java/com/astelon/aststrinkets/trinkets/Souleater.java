package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Souleater extends Trinket {

    private final Random random;
    private long cooldown;
    private double useChance;

    public Souleater(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "souleater", Power.CONSUME_ENCHANTMENTS, false, Usages.HIT_AND_DAMAGE);
        random = new Random();
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("The Souleater", TextColor.fromHexString("#9F1D35")));
        meta.lore(List.of(Component.text("An entity possessed this sword."),
                Component.text("It occasionally feeds on the souls"),
                Component.text("used to enchant the equipment of"),
                Component.text("those it strikes.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean canEat(ItemStack souleater) {
        PersistentDataContainer container = souleater.getItemMeta().getPersistentDataContainer();
        if (container.has(keys.lastUseKey, PersistentDataType.LONG)) {
            long lastUsed = container.getOrDefault(keys.lastUseKey, PersistentDataType.LONG, 0L);
            return System.currentTimeMillis() - lastUsed > cooldown;
        }
        return true;
    }

    public boolean eatEnchantment(ItemStack souleater, EntityEquipment equipment) {
        ArrayList<EquipmentSlot> enchantedSlots = new ArrayList<>(6);
        if (hasEnchantments(equipment.getItemInMainHand()))
            enchantedSlots.add(EquipmentSlot.HAND);
        if (hasEnchantments(equipment.getItemInOffHand()))
            enchantedSlots.add(EquipmentSlot.OFF_HAND);
        if (hasEnchantments(equipment.getHelmet()))
            enchantedSlots.add(EquipmentSlot.HEAD);
        if (hasEnchantments(equipment.getChestplate()))
            enchantedSlots.add(EquipmentSlot.CHEST);
        if (hasEnchantments(equipment.getLeggings()))
            enchantedSlots.add(EquipmentSlot.LEGS);
        if (hasEnchantments(equipment.getBoots()))
            enchantedSlots.add(EquipmentSlot.FEET);
        if (enchantedSlots.size() != 0) {
            double roll = random.nextDouble();
            if (roll < useChance) {
                EquipmentSlot randomSlot = enchantedSlots.get(random.nextInt(enchantedSlots.size()));
                ItemStack randomItem = equipment.getItem(randomSlot);
                removeEnchantment(randomItem);
                setLastUse(souleater);
                equipment.setItem(randomSlot, randomItem);
                return true;
            }
        }
        return false;
    }

    private boolean hasEnchantments(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        return !itemStack.getEnchantments().isEmpty();
    }

    private void removeEnchantment(ItemStack itemStack) {
        ArrayList<Enchantment> enchantments = new ArrayList<>(itemStack.getEnchantments().keySet());
        Enchantment randomEnchantment = enchantments.get(random.nextInt(enchantments.size()));
        int level = itemStack.getEnchantments().get(randomEnchantment);
        if (level <= 1)
            itemStack.removeEnchantment(randomEnchantment);
        else
            itemStack.addEnchantment(randomEnchantment, level - 1);
    }

    private void setLastUse(ItemStack souleater) {
        ItemMeta meta = souleater.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.lastUseKey, PersistentDataType.LONG, System.currentTimeMillis());
        souleater.setItemMeta(meta);
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setUseChance(double useChance) {
        this.useChance = useChance;
    }
}
