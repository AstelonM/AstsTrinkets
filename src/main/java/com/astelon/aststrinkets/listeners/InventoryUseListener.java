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

    public InventoryUseListener(AstsTrinkets plugin, TrinketManager manager) {
        this.plugin = plugin;
        this.trinketManager = manager;
        this.mendingPowder = manager.getMendingPowder();
        this.bindingPowder = manager.getBindingPowder();
        this.unbindingPowder = manager.getUnbindingPowder();
        this.homendirt = manager.getHomendirt();
        this.homendingdirt = manager.getHomendingdirt();
        this.infinityItem = manager.getInfinityItem();
        this.shulkerBoxContainmentUnit = manager.getShulkerBoxContainmentUnit();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack heldItem = event.getCursor();
        Player player = (Player) event.getView().getPlayer();
        if (heldItem != null && event.getClick() == ClickType.SHIFT_RIGHT && trinketManager.isOwnedBy(heldItem, player.getName())) {
            if (mendingPowder.isEnabledTrinket(heldItem)) {
                ItemStack item = event.getCurrentItem();
                if (item == null)
                    return;
                ItemMeta meta = item.getItemMeta();
                if (!(meta instanceof Damageable damageable))
                    return;
                int maxDurability = item.getType().getMaxDurability();
                int damage = damageable.getDamage();
                // Just in case there's some weird values
                if (damage != 0 && maxDurability > 0) {
                    event.setCancelled(true);
                    damageable.setDamage(0);
                    item.setItemMeta(meta);
                    heldItem.subtract();
                    player.updateInventory();
                }
            } else if (bindingPowder.isEnabledTrinket(heldItem)) {
                ItemStack item = event.getCurrentItem();
                if (item == null)
                    return;
                Trinket trinket = trinketManager.getTrinket(item);
                if (trinket != null) {
                    event.setCancelled(true);
                    ItemStack result = bindingPowder.bindTrinket(item, trinket, player);
                    if (result != null) {
                        heldItem.subtract();
                        transformItem(item, result, event.getSlot(), event.getClickedInventory(), player);
                        player.sendMessage(Component.text("This trinket is bound to you now.", NamedTextColor.GOLD));
                        player.updateInventory();
                    } else {
                        player.sendMessage(Component.text("Could not bind this trinket to you.", NamedTextColor.RED));
                    }
                }
            } else if (unbindingPowder.isEnabledTrinket(heldItem)) {
                ItemStack item = event.getCurrentItem();
                if (item == null)
                    return;
                Trinket trinket = trinketManager.getTrinket(item);
                if (trinket != null) {
                    event.setCancelled(true);
                    ItemStack result = unbindingPowder.unbindTrinket(item, trinket);
                    if (result != null) {
                        heldItem.subtract();
                        transformItem(item, result, event.getSlot(), event.getClickedInventory(), player);
                        player.sendMessage(Component.text("This trinket is now unbound.", NamedTextColor.GOLD));
                        player.updateInventory();
                    } else {
                        player.sendMessage(Component.text("Could not unbind this trinket.", NamedTextColor.RED));
                    }
                }
            } else if (homendirt.isEnabledTrinket(heldItem)) {
                ItemStack item = event.getCurrentItem();
                if (item == null)
                    return;
                int slot = event.getSlot();
                Inventory inventory = event.getClickedInventory();
                if (homendingdirt.isTrinket(item)) {
                    event.setCancelled(true);
                    int mending = item.getEnchantmentLevel(Enchantment.MENDING);
                    item.subtract();
                    ItemStack newHomendingdirt = homendingdirt.createHomendingdirt(heldItem, mending);
                    transformCursorItem(heldItem, newHomendingdirt, inventory, player);
                    player.sendMessage(Component.text("The Homendingdirt is consumed.", NamedTextColor.GOLD));
                    player.updateInventory();
                    return;
                }
                int mending;
                if (item.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                    mending = meta.getStoredEnchantLevel(Enchantment.MENDING);
                } else {
                    mending = item.getEnchantmentLevel(Enchantment.MENDING);
                }
                if (mending == 0)
                    return;
                event.setCancelled(true);
                ItemStack result = homendirt.removeMending(item);
                transformItem(item, result, slot, inventory, player);
                ItemStack homendingdirtItem = homendingdirt.createHomendingdirt(heldItem, mending);
                transformCursorItem(heldItem, homendingdirtItem, inventory, player);
                player.updateInventory();
            } else if (homendingdirt.isEnabledTrinket(heldItem)) {
                ItemStack item = event.getCurrentItem();
                if (item == null)
                    return;
                int slot = event.getSlot();
                Inventory inventory = event.getClickedInventory();
                if (homendirt.isTrinket(item)) {
                    event.setCancelled(true);
                    int mending = heldItem.getEnchantmentLevel(Enchantment.MENDING);
                    heldItem.subtract();
                    ItemStack newHomendingdirt = homendingdirt.createHomendingdirt(item, mending);
                    transformItem(item, newHomendingdirt, slot, inventory, player);
                    player.updateInventory();
                    player.sendMessage(Component.text("The Homendirt becomes a Homendingdirt.", NamedTextColor.GOLD));
                    return;
                }
                int mending = item.getEnchantmentLevel(Enchantment.MENDING);
                if (mending != 0)
                    return;
                event.setCancelled(true);
                ItemStack result = homendingdirt.addMending(item, heldItem);
                transformItem(item, result, slot, inventory, player);
                heldItem.subtract();
                player.updateInventory();
            } else if (infinityItem.isEnabledTrinket(heldItem)) {
                if (infinityItem.hasBlock(heldItem))
                    return;
                ItemStack item = event.getCurrentItem();
                if (item == null)
                    return;
                Material block = item.getType();
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
                ItemStack shulker = event.getCurrentItem();
                if (!Utils.isShulkerBox(shulker))
                    return;
                if (!shulkerBoxContainmentUnit.canContainShulkerBox(shulker)) {
                    player.sendMessage(Component.text("You can't contain this shulker. Make sure it doesn't have " +
                            "another containment unit inside.", NamedTextColor.RED));
                    return;
                }
                ItemStack result = shulkerBoxContainmentUnit.setContainedShulkerBox(heldItem, shulker);
                if (result != null) {
                    event.setCancelled(true);
                    transformCursorItem(heldItem, result, player.getInventory(), player);
                    shulker.subtract();
                    player.updateInventory();
                }
            }
        }
    }
}
