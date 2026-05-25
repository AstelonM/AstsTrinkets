package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.UniversalFertilizer;
import io.papermc.paper.event.block.BlockPreDispenseEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
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
    public void onBlockPreDispense(BlockPreDispenseEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (universalFertilizer.isEnabledTrinket(itemStack) && trinketManager.getOwner(itemStack) == null &&
                universalFertilizer.isDispenserAllowed()) {
            Block block = event.getBlock();
            int slot = event.getSlot();
            Dispenser dispenserData = (Dispenser) block.getBlockData();
            Block target = block.getRelative(dispenserData.getFacing());
            ItemStack result = universalFertilizer.getPlant(target.getType());
            if (result != null) {
                // Can't remove in the pre event because the server's gonna crash when it tries to decrement it to dispense
                Bukkit.getScheduler().runTask(plugin, () -> {
                    var dispenser = (org.bukkit.block.Dispenser) block.getState(false);
                    Inventory inventory = dispenser.getInventory();
                    ItemStack toRemove = inventory.getItem(slot);
                    if (toRemove != null) {
                        toRemove.subtract();
                    }
                    dispenser.update();
                });
                Location targetLocation = target.getLocation();
                targetLocation.getWorld().dropItemNaturally(targetLocation, result);
            }
        }
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
            if (result != null)
                event.setCancelled(true);
        }
    }
}
