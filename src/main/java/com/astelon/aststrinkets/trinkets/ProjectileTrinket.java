package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class ProjectileTrinket extends Trinket {

    private final NamespacedKey ownerKey;

    public ProjectileTrinket(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, NamespacedKey ownerKey,
                             String name, TextColor infoColour, Power power, boolean isOp, String usage) {
        super(plugin, nameKey, powerKey, name, infoColour, power, isOp, usage);
        this.ownerKey = ownerKey;
    }

    public void setProjectileTrinket(Projectile projectile, ItemStack itemStack) {
        PersistentDataContainer sourceContainer = itemStack.getItemMeta().getPersistentDataContainer();
        PersistentDataContainer container = projectile.getPersistentDataContainer();
        container.set(nameKey, PersistentDataType.STRING,
                sourceContainer.getOrDefault(nameKey, PersistentDataType.STRING, name));
        container.set(powerKey, PersistentDataType.STRING,
                sourceContainer.getOrDefault(powerKey, PersistentDataType.STRING, power.powerName()));
        if (sourceContainer.has(ownerKey, PersistentDataType.STRING))
            container.set(ownerKey, PersistentDataType.STRING,
                    sourceContainer.getOrDefault(ownerKey, PersistentDataType.STRING, ""));
    }
}
