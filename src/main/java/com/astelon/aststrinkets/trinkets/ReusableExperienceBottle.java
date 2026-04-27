package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ReusableExperienceBottle extends Trinket {

    public ReusableExperienceBottle(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "reusableExperienceBottle", Power.ABSORB_EXPERIENCE_INTO_BOTTLE, false,
                Usages.SHIFT_RIGHT_CLICK_THEN_DRINK);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.GLASS_BOTTLE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Empty Reusable Experience Bottle", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Ready to be filled with experience."),
                Component.text("Now using shatterproof glass!")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean hasExperience(ItemStack bottle) {
        PersistentDataContainer container = bottle.getItemMeta().getPersistentDataContainer();
        return container.has(keys.storedExperienceKey, PersistentDataType.INTEGER);
    }

    public int getExperience(ItemStack bottle) {
        PersistentDataContainer container = bottle.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.storedExperienceKey, PersistentDataType.INTEGER, -1);
    }

    public int getExperience(Projectile bottle) {
        PersistentDataContainer container = bottle.getPersistentDataContainer();
        return container.getOrDefault(keys.storedExperienceKey, PersistentDataType.INTEGER, -1);
    }

    public long getLastUse(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(keys.lastUseKey, PersistentDataType.LONG, 0L);
    }

    public ItemStack fillExperienceBottle(ItemStack bottle, int experience) {
        ItemStack result = bottle.asOne();
        result.setType(Material.POTION);
        PotionMeta meta = (PotionMeta) result.getItemMeta();
        meta.setColor(Color.YELLOW);//TODO colour based on amount?
        meta.displayName(Component.text("Filled Reusable Experience Bottle", NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false));
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.storedExperienceKey, PersistentDataType.INTEGER, experience);
        ArrayList<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            List<Component> oldLore = meta.lore();
            if (oldLore != null && !oldLore.isEmpty())
                newLore.add(oldLore.get(0));
        }
        newLore.add(Component.text("Experience: " + experience, infoColour).decoration(TextDecoration.ITALIC, false));
        newLore.add(Component.text("Bottled experience. Drink it"));
        newLore.add(Component.text("to recover."));
        meta.lore(newLore);
        result.setItemMeta(meta);
        return result;
    }
}
