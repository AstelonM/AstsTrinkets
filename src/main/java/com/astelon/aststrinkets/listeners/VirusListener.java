package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.SpoiledEgg;
import com.astelon.aststrinkets.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VirusListener implements Listener {

    private final Random random;

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final SpoiledEgg spoiledEgg;

    public VirusListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        random = new Random();
        spoiledEgg = trinketManager.getSpoiledEgg();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!spoiledEgg.isEnabled())
            return;
        InventoryView view = event.getView();
        searchAndAct(view.getTopInventory());
        searchAndAct(view.getBottomInventory());
    }

    private void searchAndAct(Inventory inventory) {
        List<HumanEntity> viewers = inventory.getViewers();
        if (viewers.size() != 1)
            return;
        Set<Integer> infectedSlots = new HashSet<>();
        ItemStack[] contents = inventory.getContents();
        List<ItemStack> originals = new ArrayList<>();
        int decayed = 0;
        for (ItemStack itemStack : inventory) {
            if (spoiledEgg.isTrinket(itemStack)) {
                if (trinketManager.isOwnedBy(itemStack, viewers.get(0).getName()))
                    continue;
                originals.add(itemStack);
            }
        }
        for (ItemStack itemStack : originals) {
            double roll = random.nextDouble();
            if (roll < spoiledEgg.getLethality(itemStack)) {
                itemStack.subtract();
                decayed++;
            } else {
                roll = random.nextDouble();
                if (roll < spoiledEgg.getInfectivity(itemStack)) {
                    infectedSlots.addAll(spoiledEgg.spread(itemStack, contents));
                }
            }
        }
        String inventoryType = inventory.getType().name();
        Location location = inventory.getLocation();
        HumanEntity player = viewers.get(0);
        if (location == null)
            location = viewers.get(0).getLocation();
        if (decayed != 0) {
            plugin.getLogger().info(decayed + " spoiled eggs decayed in a " + inventoryType + " inventory at " +
                    Utils.serializeCoordsLogging(location) + " because of " + player.getName() + ".");
        }
        if (!infectedSlots.isEmpty()) {
            plugin.getLogger().info(infectedSlots.size() + " items were infected by spoiled eggs in a " +
                    inventoryType + " inventory at " + Utils.serializeCoordsLogging(location) + " because of " +
                    player.getName() + ".");
        }
    }
}
