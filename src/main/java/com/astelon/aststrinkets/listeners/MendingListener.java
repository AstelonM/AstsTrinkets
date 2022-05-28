package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.MendingPowder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class MendingListener implements Listener {

    private final AstsTrinkets plugin;
    private final MendingPowder mendingPowder;

    public MendingListener(AstsTrinkets plugin, TrinketManager manager) {
        this.plugin = plugin;
        this.mendingPowder = manager.getMendingPowder();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (mendingPowder.isEnabled() && mendingPowder.isTrinket(event.getCursor()) &&
                event.getClick() == ClickType.SHIFT_RIGHT) {
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
                ItemStack trinketStack = Objects.requireNonNull(event.getCursor());
                int amount  = trinketStack.getAmount();
                if (amount > 1) {
                    trinketStack.setAmount(amount - 1);
                    event.getView().getPlayer().setItemOnCursor(trinketStack);
                } else {
                    event.getView().getPlayer().setItemOnCursor(null);
                }
            }
        }
    }
}
