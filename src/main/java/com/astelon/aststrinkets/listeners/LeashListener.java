package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.creature.Lasso;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class LeashListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final Lasso lasso;

    private final Map<Player, Long> cooldowns;

    public LeashListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        lasso = trinketManager.getLasso();
        cooldowns = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Mob entity))
            return;
        if (entity.isLeashed())
            return;
        Player player = event.getPlayer();
        EquipmentSlot hand = event.getHand();
        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = hand == EquipmentSlot.HAND ? inventory.getItemInMainHand() : inventory.getItemInOffHand();
        if (lasso.isEnabledTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, player)) {
            if (trinketManager.isTrinketImmune(entity)) {
                player.sendMessage(Component.text("Trinkets cannot be used on this entity.", NamedTextColor.RED));
                return;
            }
            if (!lasso.lassoEntity(itemStack, entity, player)) {
                player.sendMessage(Component.text("Could not lasso that creature.", NamedTextColor.RED));
                return;
            }
            itemStack.subtract();
            cooldowns.put(player, System.currentTimeMillis());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        // If the lasso is disabled, players could still use it on leashable mobs and this even would be called, so
        // the mob needs to have the lasso set so it wouldn't be lost when the creature is unleashed
        if (lasso.isEnabled())
            return;
        if (!(event.getEntity() instanceof Mob entity) || entity.isLeashed())
            return;
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = inventory.getItemInMainHand();
        if (!lasso.isTrinket(itemStack)) {
            if (itemStack.getType() != Material.LEAD) {
                itemStack = inventory.getItemInOffHand();
                if (!lasso.isTrinket(itemStack))
                    return;
            }
        }
        lasso.setLasso(itemStack, entity);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityUnleash(EntityUnleashEvent event) {
        if (!(event.getEntity() instanceof Mob entity))
            return;
        if (event.getReason() != EntityUnleashEvent.UnleashReason.PLAYER_UNLEASH) {
            if (!event.isDropLeash())
                lasso.removeLasso(entity);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event) {
        if (!(event.getEntity() instanceof Mob entity))
            return;
        if (event.getReason() == EntityUnleashEvent.UnleashReason.PLAYER_UNLEASH) {
            long now = System.currentTimeMillis();
            if (now - cooldowns.getOrDefault(event.getPlayer(), 0L) <= 1000)
                event.setCancelled(true);
        }
        if (!event.isDropLeash())
            lasso.removeLasso(entity);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDropItem(EntityDropItemEvent event) {
        if (!(event.getEntity() instanceof  Mob entity))
            return;
        Item item = event.getItemDrop();
        if (item.getItemStack().getType() == Material.LEAD && lasso.hasLasso(entity)) {
            ItemStack result = lasso.getLasso(entity);
            lasso.removeLasso(entity);
            if (result != null) {
                item.setItemStack(result);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cooldowns.remove(event.getPlayer());
    }
}
