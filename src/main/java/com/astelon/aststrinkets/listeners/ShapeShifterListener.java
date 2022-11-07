package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.ShapeShifter;
import com.astelon.aststrinkets.utils.Utils;
import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.*;

public class ShapeShifterListener implements Listener {

    private final AstsTrinkets plugin;
    private final ShapeShifter shapeShifter;

    public ShapeShifterListener(AstsTrinkets plugin, TrinketManager manager) {
        this.plugin = plugin;
        shapeShifter = manager.getShapeShifter();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!shapeShifter.isEnabled())
            return;
        InventoryView view = event.getView();
        searchAndShift(view.getTopInventory());
        searchAndShift(view.getBottomInventory());
    }

    private void searchAndShift(Inventory inventory) {
        if (inventory.getViewers().size() == 1) {
            for (ItemStack itemStack: inventory) {
                if (shapeShifter.isTrinket(itemStack)) {
                    shapeShifter.shapeShift(itemStack);
                    Location location = inventory.getLocation();
                    if (inventory.getHolder() instanceof BlockInventoryHolder && location != null) {
                        plugin.getLogger().info("Shape shifter shifted shape in a block inventory at " +
                                Utils.locationToString(location) + ".");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (shapeShifter.isEnabled() && shapeShifter.isTrinket(event.getItemInHand())) {
            event.setCancelled(true);
            shapeShifter.shapeShift(event.getItemInHand());
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (shapeShifter.isEnabled()) {
            ItemStack[] items = event.getInventory().getMatrix();
            for (ItemStack item: items) {
                if (shapeShifter.isTrinket(item)) {
                    event.setCancelled(true);
                    shapeShifter.shapeShift(item);
                }
            }
        }
    }

    @EventHandler
    public void onPrepareResult(PrepareResultEvent event) {
        if (shapeShifter.isEnabled()) {
            Inventory inventory = event.getInventory();
            for (ItemStack item: inventory.getContents()) {
                if (item != null && shapeShifter.isTrinket(item)) {
                    shapeShifter.shapeShift(item);
                    event.setResult(null);
                }
            }
        }
    }
}
