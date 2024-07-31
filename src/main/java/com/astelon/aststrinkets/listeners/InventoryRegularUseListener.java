package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Die;
import com.astelon.aststrinkets.trinkets.HoldingBundle;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import static com.astelon.aststrinkets.utils.Utils.isNothing;

public class InventoryRegularUseListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final HoldingBundle holdingBundle;
    private final Die die;

    public InventoryRegularUseListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        holdingBundle = trinketManager.getHoldingBundle();
        die = trinketManager.getDie();
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getView().getPlayer();
        ItemStack heldItem = event.getCursor();
        ItemStack clickedItem = event.getCurrentItem();
        switch (event.getClick()) {
            case RIGHT -> {
                if (holdingBundle.isEnabled()) {
                    if (isNothing(heldItem)) {
                        if (clickedItem != null && holdingBundle.isTrinket(clickedItem)) {
                            if (holdingBundle.hasItems(clickedItem)) {
                                event.setCancelled(true);
                                if (trinketManager.isOwnedBy(clickedItem, player)) {
                                    ItemStack containedItem = holdingBundle.getItem(clickedItem);
                                    player.setItemOnCursor(containedItem);
                                    ItemStack result = holdingBundle.removeItems(clickedItem, player);
                                    if (result == null) {
                                        player.sendMessage(Component.text("This Bundle of Holding is corrupted.",
                                                NamedTextColor.RED));
                                        return;
                                    }
                                    Utils.transformItem(clickedItem, result, event.getSlot(), event.getClickedInventory(),
                                            player);
                                    player.updateInventory();
                                }
                            }
                        }
                    } else if (holdingBundle.isTrinket(heldItem)) {
                        if (isNothing(clickedItem)) {
                            if (holdingBundle.hasItems(heldItem)) {
                                event.setCancelled(true);
                                if (trinketManager.isOwnedBy(heldItem, player)) {
                                    ItemStack containedItem = holdingBundle.getItem(heldItem);
                                    Inventory inventory = event.getClickedInventory();
                                    if (inventory == null) {
                                        plugin.getLogger().warning("Somehow the inventory was null when trying " +
                                                "to remove an item from a bundle");
                                        return;
                                    }
                                    inventory.setItem(event.getSlot(), containedItem);
                                    ItemStack result = holdingBundle.removeItems(heldItem, player);
                                    if (result == null) {
                                        player.sendMessage(Component.text("This Bundle of Holding is corrupted.", NamedTextColor.RED));
                                        return;
                                    }
                                    Utils.transformCursorItem(heldItem, result, player.getInventory(), player);
                                    player.updateInventory();
                                }
                            }
                        } else {
                            if (holdingBundle.hasItems(heldItem)) {
                                event.setCancelled(true);
                                if (trinketManager.isOwnedBy(heldItem, player)) {
                                    ItemStack result = holdingBundle.addItemsInBundle(heldItem, clickedItem, player);
                                    if (result != null) {
                                        Utils.transformCursorItem(heldItem, result, player.getInventory(), player);
                                        clickedItem.setAmount(0);
                                        player.updateInventory();
                                    }
                                }
                            } else if (!trinketManager.isOwnedBy(heldItem, player)) {
                                event.setCancelled(true);
                            }
                        }
                    } else {
                        if (!isNothing(clickedItem) && holdingBundle.isTrinket(clickedItem)) {
                            if (holdingBundle.hasItems(clickedItem)) {
                                event.setCancelled(true);
                                if (trinketManager.isOwnedBy(clickedItem, player)) {
                                    ItemStack result = holdingBundle.addItemsInBundle(clickedItem, heldItem, player);
                                    if (result != null) {
                                        Utils.transformItem(clickedItem, result, event.getSlot(),
                                                event.getClickedInventory(), player);
                                        heldItem.setAmount(0);
                                        player.updateInventory();
                                    }
                                }
                            } else {
                                if (!trinketManager.isOwnedBy(clickedItem, player)) {
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
            case LEFT -> {
                if (isNothing(clickedItem) || isNothing(heldItem))
                    return;
                if (die.isEnabled() && die.isTrinket(heldItem) && die.isTrinket(clickedItem) &&
                        trinketManager.isOwnedBy(heldItem, player) && trinketManager.isOwnedBy(clickedItem, player)) {
                    die.removeRoll(heldItem);
                    die.removeRoll(clickedItem);
                    player.updateInventory();
                }
            }
            case DOUBLE_CLICK -> {
                if (!isNothing(clickedItem) || isNothing(heldItem))
                    return;
                if (die.isEnabled() && die.isTrinket(heldItem)) {
                    if (trinketManager.isOwnedBy(heldItem, player))
                        die.removeRoll(heldItem);
                    InventoryView inventoryView = event.getView();
                    for (ItemStack itemStack: inventoryView.getTopInventory()) {
                        if (die.isTrinket(itemStack) && die.isSimilar(heldItem, itemStack) &&
                                trinketManager.isOwnedBy(itemStack, player)) {
                            die.removeRoll(itemStack);
                        }
                    }
                    for (ItemStack itemStack: inventoryView.getBottomInventory()) {
                        if (die.isTrinket(itemStack) && die.isSimilar(heldItem, itemStack) &&
                                trinketManager.isOwnedBy(itemStack, player)) {
                            die.removeRoll(itemStack);
                        }
                    }
                    player.updateInventory();
                }
            }
        }
    }
}
