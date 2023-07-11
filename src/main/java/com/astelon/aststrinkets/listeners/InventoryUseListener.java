package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.*;
import com.astelon.aststrinkets.trinkets.block.InfinityItem;
import com.astelon.aststrinkets.trinkets.inventory.*;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import static com.astelon.aststrinkets.utils.Utils.transformCursorItem;
import static com.astelon.aststrinkets.utils.Utils.transformItem;

public class InventoryUseListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final MendingPowder mendingPowder;
    private final BindingPowder bindingPowder;
    private final UnbindingPowder unbindingPowder;
    private final Homendirt homendirt;
    private final Homendingdirt homendingdirt;
    private final InfinityItem infinityItem;
    private final ShulkerBoxContainmentUnit shulkerBoxContainmentUnit;
    private final BuddingSolution buddingSolution;
    private final HoldingBundle holdingBundle;

    public InventoryUseListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        mendingPowder = trinketManager.getMendingPowder();
        bindingPowder = trinketManager.getBindingPowder();
        unbindingPowder = trinketManager.getUnbindingPowder();
        homendirt = trinketManager.getHomendirt();
        homendingdirt = trinketManager.getHomendingdirt();
        infinityItem = trinketManager.getInfinityItem();
        shulkerBoxContainmentUnit = trinketManager.getShulkerBoxContainmentUnit();
        buddingSolution = trinketManager.getBuddingSolution();
        holdingBundle = trinketManager.getHoldingBundle();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack heldItem = event.getCursor();
        Player player = (Player) event.getView().getPlayer();
        if (heldItem == null)
            return;
        ItemStack clickedItem = event.getCurrentItem();
        if (event.getClick() == ClickType.RIGHT) {
            if (holdingBundle.isEnabled()) {
                if (holdingBundle.isTrinket(heldItem)) {
                    if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                        if (holdingBundle.hasItems(heldItem)) {
                            event.setCancelled(true);
                            ItemStack result = addItemsInBundle(heldItem, clickedItem, player);
                            if (result != null) {
                                Utils.transformCursorItem(heldItem, result, player.getInventory(), player);
                                clickedItem.setAmount(0);
                                player.updateInventory();
                            }
                        }
                    } else {
                        event.setCancelled(true);
                        ItemStack containedItem = holdingBundle.getItem(heldItem);
                        Inventory inventory = event.getClickedInventory();
                        if (inventory == null)
                            return;
                        inventory.setItem(event.getSlot(), containedItem);
                        ItemStack result = holdingBundle.removeItems(heldItem);
                        if (result == null) {
                            player.sendMessage(Component.text("This Bundle of Holding is corrupted.", NamedTextColor.RED));
                            return;
                        }
                        Utils.transformCursorItem(heldItem, result, player.getInventory(), player);
                        player.updateInventory();
                    }
                } else if (clickedItem != null && holdingBundle.isTrinket(clickedItem) && holdingBundle.hasItems(clickedItem)) {
                    event.setCancelled(true);
                    ItemStack result = addItemsInBundle(clickedItem, heldItem, player);
                    if (result != null) {
                        Utils.transformItem(clickedItem, result, event.getSlot(), event.getClickedInventory(), player);
                        heldItem.setAmount(0);
                        player.updateInventory();
                    }
                }
                return;
            }
        }
        if (event.getClick() == ClickType.SHIFT_RIGHT && trinketManager.isOwnedBy(heldItem, player.getName())) {
            if (mendingPowder.isEnabledTrinket(heldItem)) {
                if (clickedItem == null)
                    return;
                if (trinketManager.isTrinketImmune(clickedItem)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                    return;
                }
                ItemMeta meta = clickedItem.getItemMeta();
                if (!(meta instanceof Damageable damageable))
                    return;
                int maxDurability = clickedItem.getType().getMaxDurability();
                int damage = damageable.getDamage();
                // Just in case there's some weird values
                if (damage != 0 && maxDurability > 0) {
                    event.setCancelled(true);
                    damageable.setDamage(0);
                    clickedItem.setItemMeta(meta);
                    heldItem.subtract();
                    player.updateInventory();
                }
            } else if (bindingPowder.isEnabledTrinket(heldItem)) {
                if (clickedItem == null)
                    return;
                if (trinketManager.isTrinketImmune(clickedItem)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                    return;
                }
                Trinket trinket = trinketManager.getTrinket(clickedItem);
                if (trinket != null) {
                    event.setCancelled(true);
                    ItemStack result = bindingPowder.bindTrinket(clickedItem, trinket, player);
                    if (result != null) {
                        heldItem.subtract();
                        transformItem(clickedItem, result, event.getSlot(), event.getClickedInventory(), player);
                        player.sendMessage(Component.text("This trinket is bound to you now.", NamedTextColor.GOLD));
                        player.updateInventory();
                    } else {
                        player.sendMessage(Component.text("Could not bind this trinket to you.", NamedTextColor.RED));
                    }
                }
            } else if (unbindingPowder.isEnabledTrinket(heldItem)) {
                if (clickedItem == null)
                    return;
                if (trinketManager.isTrinketImmune(clickedItem)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                    return;
                }
                Trinket trinket = trinketManager.getTrinket(clickedItem);
                if (trinket != null) {
                    event.setCancelled(true);
                    ItemStack result = unbindingPowder.unbindTrinket(clickedItem, trinket);
                    if (result != null) {
                        heldItem.subtract();
                        transformItem(clickedItem, result, event.getSlot(), event.getClickedInventory(), player);
                        player.sendMessage(Component.text("This trinket is now unbound.", NamedTextColor.GOLD));
                        player.updateInventory();
                    } else {
                        player.sendMessage(Component.text("Could not unbind this trinket.", NamedTextColor.RED));
                    }
                }
            } else if (homendirt.isEnabledTrinket(heldItem)) {
                if (clickedItem == null)
                    return;
                if (trinketManager.isTrinketImmune(clickedItem)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                    return;
                }
                int slot = event.getSlot();
                Inventory inventory = event.getClickedInventory();
                if (homendingdirt.isTrinket(clickedItem)) {
                    event.setCancelled(true);
                    int mending = clickedItem.getEnchantmentLevel(Enchantment.MENDING);
                    clickedItem.subtract();
                    ItemStack newHomendingdirt = homendingdirt.createHomendingdirt(heldItem, mending);
                    transformCursorItem(heldItem, newHomendingdirt, inventory, player);
                    player.sendMessage(Component.text("The Homendingdirt is consumed.", NamedTextColor.GOLD));
                    player.updateInventory();
                    return;
                }
                int mending;
                if (clickedItem.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) clickedItem.getItemMeta();
                    mending = meta.getStoredEnchantLevel(Enchantment.MENDING);
                } else {
                    mending = clickedItem.getEnchantmentLevel(Enchantment.MENDING);
                }
                if (mending == 0)
                    return;
                event.setCancelled(true);
                ItemStack result = homendirt.removeMending(clickedItem);
                transformItem(clickedItem, result, slot, inventory, player);
                ItemStack homendingdirtItem = homendingdirt.createHomendingdirt(heldItem, mending);
                transformCursorItem(heldItem, homendingdirtItem, inventory, player);
                player.updateInventory();
            } else if (homendingdirt.isEnabledTrinket(heldItem)) {
                if (clickedItem == null)
                    return;
                if (trinketManager.isTrinketImmune(clickedItem)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                    return;
                }
                int slot = event.getSlot();
                Inventory inventory = event.getClickedInventory();
                if (homendirt.isTrinket(clickedItem)) {
                    event.setCancelled(true);
                    int mending = heldItem.getEnchantmentLevel(Enchantment.MENDING);
                    heldItem.subtract();
                    ItemStack newHomendingdirt = homendingdirt.createHomendingdirt(clickedItem, mending);
                    transformItem(clickedItem, newHomendingdirt, slot, inventory, player);
                    player.updateInventory();
                    player.sendMessage(Component.text("The Homendirt becomes a Homendingdirt.", NamedTextColor.GOLD));
                    return;
                }
                int mending = clickedItem.getEnchantmentLevel(Enchantment.MENDING);
                if (mending != 0)
                    return;
                event.setCancelled(true);
                ItemStack result = homendingdirt.addMending(clickedItem, heldItem);
                transformItem(clickedItem, result, slot, inventory, player);
                heldItem.subtract();
                player.updateInventory();
            } else if (infinityItem.isEnabledTrinket(heldItem)) {
                if (infinityItem.hasBlock(heldItem))
                    return;
                if (clickedItem == null)
                    return;
                Material block = clickedItem.getType();
                if (!infinityItem.isAllowedBlock(block, player)) {
                    player.sendMessage(Component.text("This block cannot be replicated.", NamedTextColor.RED));
                    return;
                }
                event.setCancelled(true);
                ItemStack result = infinityItem.replicateBlock(heldItem, block);
                transformCursorItem(heldItem, result, player.getInventory(), player);
                player.updateInventory();
            } else if (shulkerBoxContainmentUnit.isEnabledTrinket(heldItem)) {
                if (shulkerBoxContainmentUnit.hasShulkerBox(heldItem))
                    return;
                if (!Utils.isShulkerBox(clickedItem))
                    return;
                //TODO trinket immune check?
                if (!shulkerBoxContainmentUnit.canContainShulkerBox(clickedItem)) {
                    player.sendMessage(Component.text("You can't contain this shulker. Make sure it doesn't have " +
                            "another containment unit inside.", NamedTextColor.RED));
                    return;
                }
                ItemStack result = shulkerBoxContainmentUnit.setContainedShulkerBox(heldItem, clickedItem);
                if (result != null) {
                    event.setCancelled(true);
                    transformCursorItem(heldItem, result, player.getInventory(), player);
                    clickedItem.subtract();
                    player.updateInventory();
                }
            } else if (buddingSolution.isEnabledTrinket(heldItem)) {
                if (clickedItem == null || clickedItem.getType() != Material.AMETHYST_BLOCK)
                    return;
                if (trinketManager.isTrinketImmune(clickedItem)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                    return;
                }
                heldItem.subtract();
                transformItem(clickedItem, new ItemStack(Material.BUDDING_AMETHYST), event.getSlot(), player.getInventory(), player);
                player.updateInventory();
                event.setCancelled(true);
            }
        }
    }

    private ItemStack addItemsInBundle(ItemStack bundle, ItemStack toAdd, Player player) {
        ItemStack itemStack = holdingBundle.getItem(bundle);
        if (itemStack == null) {
            player.sendMessage(Component.text("This Bundle of Holding is corrupted.", NamedTextColor.RED));
            return null;
        }
        if (itemStack.isSimilar(toAdd))
            return holdingBundle.addItems(bundle, toAdd);
        else {
            player.sendMessage(Component.text("This bundle has items of a different kind.", NamedTextColor.RED));
        }
        return null;
    }
}
