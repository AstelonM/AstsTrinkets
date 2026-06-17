package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Bait;
import com.astelon.aststrinkets.trinkets.NotebookFishingRod;
import com.astelon.aststrinkets.trinkets.ShapeShifter;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Random;

public class FishingListener implements Listener {

    private final Random random;

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final Bait bait;
    private final NotebookFishingRod notebookFishingRod;
    private final ShapeShifter shapeShifter;

    public FishingListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        random = new Random();
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        bait = trinketManager.getBait();
        notebookFishingRod = trinketManager.getNotebookFishingRod();
        shapeShifter = trinketManager.getShapeShifter();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack mainHandItem = inventory.getItemInMainHand();
        ItemStack possibleTrinket;
        if (event.getState() == PlayerFishEvent.State.FISHING) {
            if (mainHandItem.getType() == Material.FISHING_ROD)
                possibleTrinket = inventory.getItemInOffHand();
            else
                possibleTrinket = mainHandItem;
            if (bait.isEnabledTrinket(possibleTrinket) && trinketManager.isOwnedBy(possibleTrinket, player.getName())) {
                FishHook hook = event.getHook();
                hook.setMinWaitTime((int) (hook.getMinWaitTime() * bait.getEfficiency(possibleTrinket)));
                hook.setMaxWaitTime((int) (hook.getMaxWaitTime() * bait.getEfficiency(possibleTrinket)));
                if (random.nextDouble() < bait.getConsumeChance(possibleTrinket))
                    possibleTrinket.subtract();
            }
        } else if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if (mainHandItem.getType() == Material.FISHING_ROD)
                possibleTrinket = mainHandItem;
            else
                possibleTrinket = inventory.getItemInOffHand();
            if (notebookFishingRod.isEnabledTrinket(possibleTrinket) && trinketManager.isOwnedBy(possibleTrinket, player.getName())) {
                if (event.getCaught() instanceof Item caught) {
                    ItemStack itemStack = caught.getItemStack();
                    if (shapeShifter.isTrinket(itemStack))
                        notebookFishingRod.tally(possibleTrinket, null);
                    else
                        notebookFishingRod.tally(possibleTrinket, itemStack);
                }
            }
        }
    }
}
