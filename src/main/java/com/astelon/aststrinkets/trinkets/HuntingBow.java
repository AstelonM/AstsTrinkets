package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HuntingBow extends Trinket {

    private final Random random;

    private int lootingLevel;
    private int luckLevel;

    public HuntingBow(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "huntingBow", Power.MULTIPLY_MOB_LOOT, false, Usages.SHOOT);
        random = new Random();
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.BOW);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Hunting Bow", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("A bow made for hunting animals. All"),
                Component.text("craftsmanship is of the highest"),
                Component.text("quality. Killing creatures with it"),
                Component.text("will let you collect more from its"),
                Component.text("carcass.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void setHuntingArrow(Arrow arrow) {
        PersistentDataContainer container = arrow.getPersistentDataContainer();
        container.set(keys.huntingArrowKey, PersistentDataType.BYTE, (byte) 1);
    }

    public boolean isHuntingArrow(Arrow arrow) {
        return arrow.getPersistentDataContainer().has(keys.huntingArrowKey, PersistentDataType.BYTE);
    }

    public Collection<ItemStack> getExtraLoot(Mob mob) {
        EntityType type = mob.getType();
        NamespacedKey key = type.getKey();
        LootTable lootTable = Bukkit.getLootTable(NamespacedKey.minecraft("entities/" + key.getKey()));
        if (lootTable != null) {
            LootContext lootContext = new LootContext.Builder(mob.getLocation())
                    .lootedEntity(mob)
                    .lootingModifier(lootingLevel)
                    .luck(luckLevel)
                    .build();
            return lootTable.populateLoot(random, lootContext);
        }
        return Collections.emptyList();
    }

    public int getLootingLevel() {
        return lootingLevel;
    }

    public void setLootingLevel(int lootingLevel) {
        this.lootingLevel = lootingLevel;
    }

    public int getLuckLevel() {
        return luckLevel;
    }

    public void setLuckLevel(int luckLevel) {
        this.luckLevel = luckLevel;
    }
}
