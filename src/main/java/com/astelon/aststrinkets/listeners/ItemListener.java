package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Die;
import com.astelon.aststrinkets.trinkets.HoldingBundle;
import com.astelon.aststrinkets.trinkets.ItemMagnet;
import com.astelon.aststrinkets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ItemListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final Die die;
    private final HoldingBundle holdingBundle;
    private final ItemMagnet itemMagnet;

    public ItemListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        die = trinketManager.getDie();
        holdingBundle = trinketManager.getHoldingBundle();
        itemMagnet = trinketManager.getItemMagnet();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        ItemStack droppedItem = item.getItemStack();
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack heldItem = inventory.getItemInMainHand();
        //TODO maybe split into this listener and another that ensures security and prevents exploits when trinkets are disabled
        if (holdingBundle.isTrinket(heldItem)) {
            checkBundleItemDrop(event, heldItem, droppedItem, player);
        }
        heldItem = inventory.getItemInOffHand();
        if (holdingBundle.isTrinket(heldItem)) {
            checkBundleItemDrop(event, heldItem, droppedItem, player);
        }
        if (player.isSneaking())
            return;
        if (die.isEnabledTrinket(droppedItem) && trinketManager.isOwnedBy(droppedItem, player)) {
            List<Integer> numbers = die.rollDie(item);
            plugin.getLogger().info("Player " + player.getName() + " threw a die and got " + numbers + ".");
        }
    }

    private void checkBundleItemDrop(PlayerDropItemEvent event, ItemStack bundle, ItemStack droppedItem, Player player) {
        if (!droppedItem.equals(holdingBundle.getItem(bundle)))
            return;
        if (!holdingBundle.isEnabled() || !trinketManager.isOwnedBy(bundle, player)) {
            event.setCancelled(true);
            return;
        }
        ItemStack containedItem = holdingBundle.getItem(bundle);
        Bukkit.getScheduler().runTask(plugin, () -> {
            holdingBundle.refillBundle(bundle, containedItem, player);
            player.updateInventory();
        });
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

    @EventHandler(ignoreCancelled = true)
    public void onBlockDropItem(BlockDropItemEvent event) {
        if (itemMagnet.isEnabled()) {
            List<Item> items = event.getItems();
            if (items.isEmpty())
                return;
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            if (hasNoItemMagnet(player, inventory))
                return;
            Location location = player.getLocation();
            Iterator<Item> iterator = items.iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                ItemStack toAdd = item.getItemStack();
                iterator.remove();
                HashMap<Integer, ItemStack> notAdded = inventory.addItem(toAdd);
                for (ItemStack remaining: notAdded.values())
                    player.getWorld().dropItem(location, remaining);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (itemMagnet.isEnabled()) {
            List<ItemStack> items = event.getDrops();
            if (items.isEmpty())
                return;
            Player player = event.getEntity().getKiller();
            if (player == null)
                return;
            PlayerInventory inventory = player.getInventory();
            if (hasNoItemMagnet(player, inventory))
                return;
            Location location = player.getLocation();
            Iterator<ItemStack> iterator = items.iterator();
            while (iterator.hasNext()) {
                ItemStack toAdd = iterator.next();
                iterator.remove();
                HashMap<Integer, ItemStack> notAdded = inventory.addItem(toAdd);
                for (ItemStack remaining: notAdded.values())
                    player.getWorld().dropItem(location, remaining);
            }
        }
    }

    private boolean hasNoItemMagnet(Player player, PlayerInventory inventory) {
        ItemStack itemStack = inventory.getItemInMainHand();
        if (!itemMagnet.isTrinket(itemStack) || !trinketManager.isOwnedBy(itemStack, player)) {
            itemStack = inventory.getItemInOffHand();
            return !itemMagnet.isTrinket(itemStack) || !trinketManager.isOwnedBy(itemStack, player);
        }
        return false;
    }
}
