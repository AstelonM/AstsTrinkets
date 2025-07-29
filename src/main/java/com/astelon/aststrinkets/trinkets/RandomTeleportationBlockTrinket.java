package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

public abstract class RandomTeleportationBlockTrinket extends RandomTeleportationTrinket {

    public RandomTeleportationBlockTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour,
                                           Power power, boolean op, String usage) {
        super(plugin, keys, name, infoColour, power, op, usage);
    }

    public RandomTeleportationBlockTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, Power power, boolean op,
                                           String usage) {
        super(plugin, keys, name, power, op, usage);
    }

    public void transferKeys(ItemStack trinket, TileState block) {
        PersistentDataContainer sourceContainer = trinket.getItemMeta().getPersistentDataContainer();
        PersistentDataContainer destinationContainer = block.getPersistentDataContainer();
        keys.transferKeys(sourceContainer, destinationContainer);
    }

    public Location findLocation(TileState trinket, World world) {
        PersistentDataContainer container = trinket.getPersistentDataContainer();
        return findLocation(container, world);
    }
}
