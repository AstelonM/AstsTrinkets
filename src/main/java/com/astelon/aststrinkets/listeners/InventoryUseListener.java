package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.BindingPowder;
import com.astelon.aststrinkets.trinkets.Homendingdirt;
import com.astelon.aststrinkets.trinkets.Homendirt;
import com.astelon.aststrinkets.trinkets.MendingPowder;
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

import java.util.HashMap;

public class InventoryUseListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final MendingPowder mendingPowder;
    private final BindingPowder bindingPowder;
    private final Homendirt homendirt;
    private final Homendingdirt homendingdirt;

    public InventoryUseListener(AstsTrinkets plugin, TrinketManager manager) {
        this.plugin = plugin;
        this.trinketManager = manager;
        this.mendingPowder = manager.getMendingPowder();
        this.bindingPowder = manager.getBindingPowder();
        this.homendirt = manager.getHomendirt();
        this.homendingdirt = manager.getHomendingdirt();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack heldItem = event.getCursor();
        Player player = (Player) event.getView().getPlayer();
        if (heldItem != null && event.getClick() == ClickType.SHIFT_RIGHT && trinketManager.isOwnedBy(heldItem, player.getName())) {
            if (mendingPowder.isEnabled() && mendingPowder.isTrinket(heldItem)) {
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
            } else if (bindingPowder.isEnabled() && bindingPowder.isTrinket(heldItem)) {
                ItemStack item = event.getCurrentItem();
                if (item == null)
                    return;
                if (trinketManager.isTrinket(item)) {
                    event.setCancelled(true);
                    ItemStack result = bindingPowder.bindTrinket(item, player);
                    if (result != null) {
                        heldItem.subtract();
                        transformItem(item, result, event.getSlot(), event.getClickedInventory(), player);
                        player.sendMessage(Component.text("This trinket is bound to you now.", NamedTextColor.GOLD));
                        player.updateInventory();
                    } else {
                        player.sendMessage(Component.text("Could not bind this trinket to you.", NamedTextColor.RED));
                    }
                }
            } else if (homendirt.isEnabled() && homendirt.isTrinket(heldItem)) {
                ItemStack item = event.getCurrentItem();
                if (item == null)
                    return;
                int mending;
                if (item.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                    mending = meta.getStoredEnchantLevel(Enchantment.MENDING);
                } else {
                    mending = item.getEnchantmentLevel(Enchantment.MENDING);
                }
                if (mending == 0)
                    return;
                int slot = event.getSlot();
                Inventory inventory = event.getClickedInventory();
                ItemStack result = homendirt.removeMending(item);
                transformItem(item, result, slot, inventory, player);
                ItemStack homendingdirtItem = homendingdirt.createHomendingdirt(heldItem, mending);
                transformCursorItem(heldItem, homendingdirtItem, inventory, player);
                player.updateInventory();
            } else if (homendingdirt.isEnabled() && homendingdirt.isTrinket(heldItem)) {
                ItemStack item = event.getCurrentItem();
                if (item == null)
                    return;
                //TODO Mending 2+?
                int mending = item.getEnchantmentLevel(Enchantment.MENDING);
                if (mending != 0)
                    return;
                int slot = event.getSlot();
                Inventory inventory = event.getClickedInventory();
                ItemStack result = homendingdirt.addMending(item, heldItem);
                transformItem(item, result, slot, inventory, player);
                heldItem.subtract();
                player.updateInventory();
            }
        }
    }

    /**
     * Transforms an item into another. If the source stack has only 1 item, it is removed and replaced with the target.
     * Otherwise, the stack size is decremented, and the target item is added to the inventory of the source if there
     * is space, or dropped at the player's location if there isn't.
     * @param source the source ItemStack that is transformed
     * @param target the target ItemStack that the source is transformed to
     * @param slot the inventory slot of the source item
     * @param inventory the inventory where the transformation happens
     * @param player the player who triggered the transformation
     */
    private void transformItem(ItemStack source, ItemStack target, int slot, Inventory inventory, Player player) {
        int amount = source.getAmount();
        if (amount == 1) {
            inventory.setItem(slot, target);
        } else {
            source.subtract();
            HashMap<Integer, ItemStack> failed = inventory.addItem(target);
            if (!failed.isEmpty()) {
                player.getWorld().dropItemNaturally(player.getLocation(), target);
                player.sendMessage(Component.text("Your inventory was full. The resulting item was dropped near you.",
                        NamedTextColor.YELLOW));
            }
        }
    }

    /**
     * Transforms the item held by the player on the cursor.
     * See {@link InventoryUseListener#transformItem(ItemStack, ItemStack, int, Inventory, Player)}.
     * @param source the source ItemStack that is held by the player
     * @param target the target ItemStack that the source is transformed to
     * @param inventory the inventory where the transformation happens
     * @param player the player who triggered the transformation
     */
    private void transformCursorItem(ItemStack source, ItemStack target, Inventory inventory, Player player) {
        int amount = source.getAmount();
        if (amount == 1) {
            player.setItemOnCursor(target);
        } else {
            source.subtract();
            HashMap<Integer, ItemStack> failed = inventory.addItem(target);
            if (!failed.isEmpty()) {
                player.getWorld().dropItemNaturally(player.getLocation(), target);
                player.sendMessage(Component.text("Your inventory was full. The resulting item was dropped near you.",
                        NamedTextColor.YELLOW));
            }
        }
    }
}
