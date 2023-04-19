package com.astelon.aststrinkets.trinkets.projectile;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ExperienceBottle extends ProjectileTrinket {

    public ExperienceBottle(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "experienceBottle", Power.ABSORB_EXPERIENCE, false, Usages.SHIFT_RIGHT_CLICK_AIR);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.GLASS_BOTTLE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Empty Experience Bottle", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Ready to be filled with experience.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void setProjectileTrinket(Projectile projectile, ItemStack itemStack) {
        super.setProjectileTrinket(projectile, itemStack);
        PersistentDataContainer sourceContainer = itemStack.getItemMeta().getPersistentDataContainer();
        PersistentDataContainer container = projectile.getPersistentDataContainer();
        container.set(keys.storedExperienceKey, PersistentDataType.INTEGER,
                sourceContainer.getOrDefault(keys.storedExperienceKey, PersistentDataType.INTEGER, -1));
    }

    public boolean hasExperience(ItemStack bottle) {
        return bottle.getType() == Material.EXPERIENCE_BOTTLE;
    }

    public int getExperience(ItemStack bottle) {
        PersistentDataContainer container = bottle.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.storedExperienceKey, PersistentDataType.INTEGER, -1);
    }

    public int getExperience(Projectile bottle) {
        PersistentDataContainer container = bottle.getPersistentDataContainer();
        return container.getOrDefault(keys.storedExperienceKey, PersistentDataType.INTEGER, -1);
    }

    public ItemStack fillExperienceBottle(ItemStack bottle, int experience) {
        ItemStack result = bottle.asOne();
        result.setType(Material.EXPERIENCE_BOTTLE);
        ItemMeta meta = result.getItemMeta();
        meta.displayName(Component.text("Filled Experience Bottle", NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false));
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.storedExperienceKey, PersistentDataType.INTEGER, experience);
        ArrayList<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            List<Component> oldLore = meta.lore();
            if (oldLore != null && oldLore.size() >= 1)
                newLore.add(oldLore.get(0));
        }
        newLore.add(Component.text("Experience: " + experience, infoColour).decoration(TextDecoration.ITALIC, false));
        newLore.add(Component.text("Bottled experience. Break the glass"));
        newLore.add(Component.text("to recover."));
        meta.lore(newLore);
        result.setItemMeta(meta);
        return result;
    }
}
