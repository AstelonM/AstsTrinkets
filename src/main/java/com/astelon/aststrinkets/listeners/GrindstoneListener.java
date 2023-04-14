package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.managers.TrinketManager;
import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;

public class GrindstoneListener implements Listener {

    private final TrinketManager trinketManager;

    public GrindstoneListener(TrinketManager trinketManager) {
        this.trinketManager = trinketManager;
    }

    @EventHandler
    public void onPrepareResult(PrepareResultEvent event) {
        if (event.getInventory() instanceof GrindstoneInventory inventory) {
            ItemStack itemStack = inventory.getResult();
            if (itemStack != null && trinketManager.isTrinket(itemStack)) {
                event.setResult(new ItemStack(itemStack.getType()));
            }
        }
    }
}
