package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

public abstract class RandomTeleportationItemTrinket extends RandomTeleportationTrinket {

    public RandomTeleportationItemTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour,
                                          Power power, boolean op, String usage) {
        super(plugin, keys, name, infoColour, power, op, usage);
    }

    public RandomTeleportationItemTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, Power power, boolean op,
                                          String usage) {
        super(plugin, keys, name, power, op, usage);
    }

    public Location findLocation(ItemStack trinket, World world) {
        PersistentDataContainer container = trinket.getItemMeta().getPersistentDataContainer();
        return findLocation(container, world);
    }
}
