package com.astelon.aststrinkets.trinkets.projectile;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class ProjectileTrinket extends Trinket {

    public ProjectileTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour, Power power,
                             boolean isOp, String usage) {
        super(plugin, keys, name, infoColour, power, isOp, usage);
    }

    public void setProjectileTrinket(Projectile projectile, ItemStack itemStack) {
        PersistentDataContainer sourceContainer = itemStack.getItemMeta().getPersistentDataContainer();
        PersistentDataContainer container = projectile.getPersistentDataContainer();
        container.set(keys.nameKey, PersistentDataType.STRING,
                sourceContainer.getOrDefault(keys.nameKey, PersistentDataType.STRING, name));
        container.set(keys.powerKey, PersistentDataType.STRING,
                sourceContainer.getOrDefault(keys.powerKey, PersistentDataType.STRING, power.powerName()));
        if (sourceContainer.has(keys.ownerKey, PersistentDataType.STRING))
            container.set(keys.ownerKey, PersistentDataType.STRING,
                    sourceContainer.getOrDefault(keys.ownerKey, PersistentDataType.STRING, ""));
    }
}
