package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Bait;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class FishingListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final Bait bait;

    public FishingListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        bait = trinketManager.getBait();
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.FISHING) {
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            ItemStack mainHandItem = inventory.getItemInMainHand();
            ItemStack possibleTrinket;
            if (mainHandItem.getType() == Material.FISHING_ROD)
                possibleTrinket = inventory.getItemInOffHand();
            else
                possibleTrinket = mainHandItem;
            if (bait.isEnabledTrinket(possibleTrinket) && trinketManager.isOwnedBy(possibleTrinket, player.getName())) {
                FishHook hook = event.getHook();
                hook.setMinWaitTime((int) (hook.getMinWaitTime() * bait.getEfficiency()));
                hook.setMaxWaitTime((int) (hook.getMaxWaitTime() * bait.getEfficiency()));
                possibleTrinket.subtract();
            }
        }
    }
}
