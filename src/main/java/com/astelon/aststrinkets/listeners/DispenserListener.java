package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.trinkets.UniversalFertilizer;
import com.astelon.aststrinkets.trinkets.projectile.ProjectileTrinket;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;

public class DispenserListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final UniversalFertilizer universalFertilizer;

    public DispenserListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        universalFertilizer = trinketManager.getUniversalFertilizer();
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDispense(BlockDispenseEvent event) {
        ItemStack itemStack = event.getItem();
        if (universalFertilizer.isEnabledTrinket(itemStack) && trinketManager.getOwner(itemStack) == null &&
                universalFertilizer.isDispenserAllowed()) {
            Block block = event.getBlock();
            Dispenser dispenser = (Dispenser) block.getBlockData();
            Block target = block.getRelative(dispenser.getFacing());
            ItemStack result = universalFertilizer.getPlant(target.getType());
            if (result != null) {
                event.setCancelled(true);
                World world = block.getWorld();
                Location location = target.getLocation();
                world.dropItemNaturally(location, result);
                itemStack.subtract();
                event.setItem(itemStack);
            }
        }
    }
}
