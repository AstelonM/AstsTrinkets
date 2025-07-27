package com.astelon.aststrinkets.trinkets.creature;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Lasso extends Trinket {

    public Lasso(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "lasso", Power.LEASH_EVERYTHING, false, Usages.INTERACT_ENTITY);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.LEAD);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Lasso", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("A trusty lasso that lets you rope"),
                Component.text("unwilling creatures.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean lassoEntity(ItemStack lasso, Mob mob, Player player) {
        EntityType type = mob.getType();
        if (type == EntityType.BAT || type == EntityType.ENDER_DRAGON || type == EntityType.WITHER)
            return false;
        if (mob.isLeashed())
            return false;
        boolean result = mob.setLeashHolder(player);
        if (result) {
            setLasso(lasso, mob);
        }
        return result;
    }

    public void setLasso(ItemStack lasso, Mob mob) {
        PersistentDataContainer container = mob.getPersistentDataContainer();
        container.set(keys.lassoKey, PersistentDataType.BYTE_ARRAY, lasso.asOne().serializeAsBytes());
    }

    public boolean hasLasso(Mob mob) {
        PersistentDataContainer container = mob.getPersistentDataContainer();
        return container.has(keys.lassoKey, PersistentDataType.BYTE_ARRAY);
    }

    public void removeLasso(Mob mob) {
        PersistentDataContainer container = mob.getPersistentDataContainer();
        container.remove(keys.lassoKey);
    }

    public ItemStack getLasso(Mob mob) {
        PersistentDataContainer container = mob.getPersistentDataContainer();
        byte[] bytes =  container.get(keys.lassoKey, PersistentDataType.BYTE_ARRAY);
        if (bytes != null) {
            return ItemStack.deserializeBytes(bytes);
        }
        return null;
    }
}
