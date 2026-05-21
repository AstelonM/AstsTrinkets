package com.astelon.aststrinkets.trinkets.projectile;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class MysteryEgg extends ProjectileTrinket {

    //TODO make it external?
    public static final int MAX_TRIES = 10;

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

    @Override
    public void setProjectileTrinket(Projectile projectile, ItemStack itemStack) {
        super.setProjectileTrinket(projectile, itemStack);
        PersistentDataContainer sourceContainer = itemStack.getItemMeta().getPersistentDataContainer();
        PersistentDataContainer targetContainer = projectile.getPersistentDataContainer();
        String whitelist = sourceContainer.get(keys.mobWhitelistKey, PersistentDataType.STRING);
        if (whitelist != null) {
            targetContainer.set(keys.mobWhitelistKey, PersistentDataType.STRING, whitelist);
        }
        String blacklist = sourceContainer.get(keys.mobBlacklistKey, PersistentDataType.STRING);
        if (blacklist != null) {
            targetContainer.set(keys.mobBlacklistKey, PersistentDataType.STRING, blacklist);
        }
    }

    public EntityType getRandomEntityType(ItemStack mysteryEgg) {
        PersistentDataContainer container =  mysteryEgg.getItemMeta().getPersistentDataContainer();
        return getRandomEntityType(container);
    }

    public EntityType getRandomEntityType(Egg mysteryEgg) {
        PersistentDataContainer container =  mysteryEgg.getPersistentDataContainer();
        return getRandomEntityType(container);
    }

    private EntityType getRandomEntityType(PersistentDataContainer container) {
        String mobWhitelist = container.get(keys.mobWhitelistKey, PersistentDataType.STRING);
        if (mobWhitelist != null) {
            List<EntityType> whitelist = Arrays.stream(mobWhitelist.split(",")).map(EntityType::valueOf).toList();
            return whitelist.get(random.nextInt(whitelist.size()));
        }
        String mobBlacklist = container.get(keys.mobBlacklistKey, PersistentDataType.STRING);
        EntityType result = entityTypes.get(random.nextInt(entityTypes.size()));
        if (mobBlacklist != null) {
            Set<String> nameBlacklist = Set.of(mobBlacklist.split(","));
            int tries = 0;
            while (nameBlacklist.contains(result.toString()) && tries < MAX_TRIES) {
                result = entityTypes.get(random.nextInt(entityTypes.size()));
                tries++;
            }
        }
        return result;
    }

    public void setAllowedEntities(HashSet<EntityType> blacklist) {
        entityTypes = new ArrayList<>();
        for (EntityType type: EntityType.values()) {
            if (type.getEntityClass() != null && Mob.class.isAssignableFrom(type.getEntityClass()) && !blacklist.contains(type))
                entityTypes.add(type);
        }
    }
}
