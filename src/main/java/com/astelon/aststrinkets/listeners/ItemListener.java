package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Die;
import com.astelon.aststrinkets.trinkets.HoldingBundle;
import com.astelon.aststrinkets.utils.Utils;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class ItemListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final Die die;
    private final HoldingBundle holdingBundle;

    public ItemListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        die = trinketManager.getDie();
        holdingBundle = trinketManager.getHoldingBundle();
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();
        Player player = event.getPlayer();
        if (player.isSneaking())
            return;
        if (die.isEnabledTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, player)) {
            List<Integer> numbers = die.rollDie(item);
            plugin.getLogger().info("Player " + player.getName() + " threw a die and got " + numbers + ".");
        }
    }

    @EventHandler
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event) {
        Item item = event.getItem();
        Player player = event.getPlayer();
        if (holdingBundle.isEnabled()) {
            PlayerInventory inventory = player.getInventory();
            ItemStack handItem = inventory.getItemInMainHand();
            if (tryAddItemsToBundle(handItem, inventory.getHeldItemSlot(), item, event.getRemaining(), player)) {
                event.setCancelled(true);
                item.remove();
                return;
            }
            handItem = inventory.getItemInOffHand();
            if (tryAddItemsToBundle(handItem, Utils.OFF_HAND_SLOT, item, event.getRemaining(), player)) {
                event.setCancelled(true);
                item.remove();
            }
        }
    }

    private boolean tryAddItemsToBundle(ItemStack heldItem, int slot, Item item, int remaining, Player player) {
        ItemStack itemStack = item.getItemStack();
        if (holdingBundle.isTrinket(heldItem) && trinketManager.isOwnedBy(heldItem, player) &&
                holdingBundle.hasMagnet(heldItem)) {
            ItemStack containedItem = holdingBundle.getItem(heldItem);
            if (containedItem == null)
                return false;
            if (containedItem.isSimilar(itemStack)) {
                ItemStack result = holdingBundle.addItemsInBundle(heldItem, itemStack, player);
                if (result != null) {
                    Utils.transformItem(heldItem, result, slot, player.getInventory(), player);
                    player.updateInventory();
                }
                return true;
            }
        }
        return false;
    }
}
