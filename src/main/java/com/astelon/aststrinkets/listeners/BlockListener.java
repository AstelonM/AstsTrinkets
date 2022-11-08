package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.InfinityItem;
import com.astelon.aststrinkets.trinkets.Spinneret;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BlockListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final Spinneret spinneret;
    private final InfinityItem infinityItem;

    public BlockListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        spinneret = trinketManager.getSpinneret();
        infinityItem = trinketManager.getInfinityItem();
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        EquipmentSlot hand = event.getHand();
        ItemStack placedItem;
        ItemStack otherItem;
        PlayerInventory inventory = player.getInventory();
        boolean mainHand = hand == EquipmentSlot.HAND;
        if (mainHand) {
            placedItem = inventory.getItemInMainHand();
            otherItem = inventory.getItemInOffHand();
        } else {
            placedItem = inventory.getItemInOffHand();
            otherItem = inventory.getItemInMainHand();
        }
        if (trinketManager.isOwnedBy(placedItem, player.getName())) {
            if (infinityItem.isEnabled() && infinityItem.isTrinket(placedItem)) {
                //TODO Find a way to place the block without consuming the item and without cancelling the event
                inventory.setItemInMainHand(placedItem);
            }
        }
        if (trinketManager.isOwnedBy(otherItem, player.getName())) {
            if (spinneret.isEnabled() && spinneret.isTrinket(otherItem)) {
                if (event.getItemInHand().getType() == Material.STRING)
                    event.getBlock().setType(Material.COBWEB);
            }
        }
    }
}
