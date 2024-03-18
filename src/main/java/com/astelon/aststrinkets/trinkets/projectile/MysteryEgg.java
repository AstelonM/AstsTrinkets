package com.astelon.aststrinkets.trinkets.projectile;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MysteryEgg extends ProjectileTrinket {

    private final Random random;
    private ArrayList<EntityType> entityTypes;

    public MysteryEgg(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "mysteryEgg", Power.SPAWN_RANDOM_CREATURES, false, Usages.THROW_RIGHT_CLICK);
        random = new Random();
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.EGG);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Mystery Egg", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("What could hatch from it?")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public EntityType getRandomEntityType() {
        return entityTypes.get(random.nextInt(entityTypes.size()));
    }

    public void setAllowedEntities(HashSet<EntityType> blacklist) {
        entityTypes = new ArrayList<>();
        for (EntityType type: EntityType.values()) {
            if (type.getEntityClass() != null && Mob.class.isAssignableFrom(type.getEntityClass()) && !blacklist.contains(type))
                entityTypes.add(type);
        }
    }
}
