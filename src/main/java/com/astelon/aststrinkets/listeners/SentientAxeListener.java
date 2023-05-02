package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.SentientAxeMessageManager;
import com.astelon.aststrinkets.managers.SentientAxeTaskManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.SentientAxe;
import com.astelon.aststrinkets.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public class SentientAxeListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final SentientAxeMessageManager messageManager;
    private final SentientAxeTaskManager taskManager;
    private final SentientAxe sentientAxe;

    private final HashMap<String, Long> lastPickMessage;

    public SentientAxeListener(AstsTrinkets plugin, TrinketManager trinketManager, SentientAxeMessageManager messageManager, SentientAxeTaskManager taskManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.messageManager = messageManager;
        sentientAxe = trinketManager.getSentientAxe();
        this.taskManager = taskManager;
        lastPickMessage = new HashMap<>();
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.getInstaBreak())
            return;
        Material material = event.getBlock().getType();
        if (isTree(material)) {
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = inventory.getItemInMainHand();
            if (sentientAxe.isEnabledTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, player.getName())) {
                event.setInstaBreak(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Material material = event.getBlock().getType();
        if (isTree(material)) {
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = inventory.getItemInMainHand();
            if (sentientAxe.isEnabledTrinket(itemStack)) {
                if (trinketManager.isOwnedBy(itemStack, player.getName())) {
                    if (sentientAxe.hasName(itemStack)) {
                        if (sentientAxe.canSendChopMessage(itemStack)) {
                            String name = sentientAxe.getName(itemStack);
                            player.sendMessage(messageManager.getChoppingMessage(name));
                        }
                    } else {
                        String name = sentientAxe.setName(itemStack);
                        player.sendMessage(messageManager.getGreeting(name));
                    }
                    sentientAxe.setLastUse(itemStack);
                    taskManager.startTaskBeforeComplaining(player.getName(), sentientAxe.getCustomId(itemStack));
                } else {
                    if (player.hasPermission("aststrinkets.trinket.handleownedsentientaxe"))
                        return;
                    event.setCancelled(true);
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                    Utils.transformItem(itemStack, null, inventory.getHeldItemSlot(), inventory, player);
                    if (sentientAxe.hasName(itemStack)) {
                        player.sendMessage(messageManager.getPickByStrangerMessage(sentientAxe.getName(itemStack)));
                    }
                }
            }
        }
    }

    private boolean isTree(Material material) {
        String materialName = material.name();
        return materialName.endsWith("_LOG") || materialName.endsWith("_WOOD") ||
                materialName.endsWith("CRIMSON_STEM") || materialName.endsWith("WARPED_STEM") ||
                materialName.endsWith("HYPHAE");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemDrop().getItemStack();
        if (sentientAxe.isEnabledTrinket(itemStack) && sentientAxe.hasName(itemStack)) {
            taskManager.cancelTask(player.getName(), sentientAxe.getCustomId(itemStack));
            if (trinketManager.isStrictlyOwnedBy(itemStack, player.getName()))
                player.sendMessage(messageManager.getDropMessage(sentientAxe.getName(itemStack)));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        ItemStack itemStack = event.getItem().getItemStack();
        if (sentientAxe.isEnabledTrinket(itemStack)) {
            if (!(event.getEntity() instanceof Player player)) {
                event.setCancelled(true);
                return;
            }
            String playerName = player.getName();
            if (sentientAxe.hasName(itemStack)) {
                if (trinketManager.isStrictlyOwnedBy(itemStack, playerName)) {
                    player.sendMessage(messageManager.getPickUpMessage(sentientAxe.getName(itemStack)));
                    taskManager.startTaskBeforeComplaining(playerName, sentientAxe.getCustomId(itemStack));
                } else if (trinketManager.getOwner(itemStack) != null) {
                    if (player.hasPermission("aststrinkets.trinket.handleownedsentientaxe"))
                        return;
                    event.setCancelled(true);
                    if (System.currentTimeMillis() - lastPickMessage.getOrDefault(playerName, 0L) >= 10000) {
                        player.sendMessage(messageManager.getPickByStrangerMessage(sentientAxe.getName(itemStack)));
                        lastPickMessage.put(playerName, System.currentTimeMillis());
                    }
                } else
                    taskManager.startTaskBeforeComplaining(playerName, sentientAxe.getCustomId(itemStack));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!sentientAxe.isEnabled())
            return;
        InventoryView view = event.getView();
        Player player = (Player) event.getPlayer();
        Inventory topInventory = view.getTopInventory();
        searchForAxeOnOpen(topInventory, topInventory.getHolder() instanceof Player, player);
    }

    private void searchForAxeOnOpen(Inventory inventory, boolean playerInventory, Player player) {
        for (ItemStack itemStack: inventory) {
            if (sentientAxe.isTrinket(itemStack) && sentientAxe.hasName(itemStack)) {
                if (!playerInventory && trinketManager.isStrictlyOwnedBy(itemStack, player.getName())) {
                    player.sendMessage(messageManager.getOpenContainerMessage(sentientAxe.getName(itemStack)));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!sentientAxe.isEnabled())
            return;
        InventoryView view = event.getView();
        Player player = (Player) event.getPlayer();
        Inventory topInventory = view.getTopInventory();
        searchForAxeOnClose(topInventory, topInventory.getHolder() instanceof Player, player);
    }

    private void searchForAxeOnClose(Inventory inventory, boolean playerInventory, Player player) {
        for (ItemStack itemStack: inventory) {
            if (sentientAxe.isTrinket(itemStack) && sentientAxe.hasName(itemStack)) {
                if (!playerInventory && trinketManager.isStrictlyOwnedBy(itemStack, player.getName())) {
                    player.sendMessage(messageManager.getStoreMessage(sentientAxe.getName(itemStack)));
                    taskManager.cancelTask(player.getName(), sentientAxe.getCustomId(itemStack));
                }
                if (playerInventory && trinketManager.isOwnedBy(itemStack, player.getName())) {
                    taskManager.startTaskBeforeComplaining(player.getName(), sentientAxe.getCustomId(itemStack));
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!sentientAxe.isEnabled())
            return;
        ItemStack itemStack = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (itemStack != null && sentientAxe.isTrinket(itemStack) && sentientAxe.hasName(itemStack) &&
                !trinketManager.isOwnedBy(itemStack, player.getName()) &&
                !player.hasPermission("aststrinkets.trinket.handleownedsentientaxe")) {
            event.setCancelled(true);
            player.sendMessage(messageManager.getPickByStrangerMessage(sentientAxe.getName(itemStack)));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEnchantItem(EnchantItemEvent event) {
        ItemStack itemStack = event.getItem();
        if (sentientAxe.isEnabledTrinket(itemStack)) {
            event.setCancelled(true);
            if (!sentientAxe.hasName(itemStack))
                return;
            Player player = event.getEnchanter();
            if (trinketManager.isStrictlyOwnedBy(itemStack, player.getName())) {
                player.sendMessage(messageManager.getEnchantOwnedMessage(sentientAxe.getName(itemStack)));
            } else if (trinketManager.getOwner(itemStack) == null) {
                player.sendMessage(messageManager.getEnchantUnownedMessage(sentientAxe.getName(itemStack)));
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!sentientAxe.isEnabled())
            return;
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        for (ItemStack itemStack: inventory) {
            if (sentientAxe.isTrinket(itemStack) && sentientAxe.hasName(itemStack) &&
                    trinketManager.isOwnedBy(itemStack, player.getName())) {
                taskManager.startTaskBeforeComplaining(player.getName(), sentientAxe.getCustomId(itemStack));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastPickMessage.remove(event.getPlayer().getName());
    }
}
