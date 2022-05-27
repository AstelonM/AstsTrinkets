package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.ShapeShifter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

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
                if (shapeShifter.isTrinket(itemStack))
                    shapeShifter.shapeShift(itemStack);
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
}
