package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.BindingPowder;
import com.astelon.aststrinkets.trinkets.MendingPowder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryUseListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final MendingPowder mendingPowder;
    private final BindingPowder bindingPowder;

    public InventoryUseListener(AstsTrinkets plugin, TrinketManager manager) {
        this.plugin = plugin;
        this.trinketManager = manager;
        this.mendingPowder = manager.getMendingPowder();
        this.bindingPowder = manager.getBindingPowder();
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
                    consumeItem(heldItem, player);
                }
            } else if (bindingPowder.isEnabled() && bindingPowder.isTrinket(heldItem)) {
                ItemStack item = event.getCurrentItem();
                if (item == null)
                    return;
                if (trinketManager.isTrinket(item)) {
                    event.setCancelled(true);
                    if (bindingPowder.bindTrinket(item, player)) {
                        consumeItem(heldItem, player);
                        player.sendMessage(Component.text("This trinket is bound to you now.", NamedTextColor.GOLD));
                    } else {
                        player.sendMessage(Component.text("Could not bind this trinket to you.", NamedTextColor.RED));
                    }
                }
            }
        }
    }

    private void consumeItem(ItemStack item, Player player) {
        int amount = item.getAmount();
        if (amount > 1) {
            item.setAmount(amount - 1);
            player.setItemOnCursor(item);
        } else {
            player.setItemOnCursor(null);
        }
    }
}
