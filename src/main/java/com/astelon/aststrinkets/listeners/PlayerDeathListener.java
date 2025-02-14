package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.SunTotem;
import com.astelon.aststrinkets.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerDeathListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final SunTotem sunTotem;

    public PlayerDeathListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        sunTotem = trinketManager.getSunTotem();
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = getSunTotem(inventory);
            if (itemStack == null)
                return;
            //TODO separate listener to ensure trinkets are used as supposed rather than their natural use?
            event.setCancelled(true);
            if (sunTotem.isEnabled() && trinketManager.isOwnedBy(itemStack, player)) {
                useSunTotem(itemStack, player);
                plugin.getLogger().info("Player " + player.getName() + " used a Sun Totem by dying at " +
                        Utils.serializeCoordsLogging(player.getLocation()));
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        EntityDamageEvent cause = player.getLastDamageCause();
        if (cause instanceof EntityDamageByEntityEvent damageEvent) {
            Entity damager = damageEvent.getDamager();
            if (damager instanceof Player killer) {
                ItemStack itemStack = getSunTotem(killer.getInventory());
                if (itemStack == null)
                    return;
                if (sunTotem.isEnabled() && trinketManager.isOwnedBy(itemStack, killer)) {
                    useSunTotem(itemStack, killer);
                    plugin.getLogger().info("Player " + killer.getName() + " used a Sun Totem by killing player " +
                            player.getName() + " at " + Utils.serializeCoordsLogging(player.getLocation()));
                }
            }
        }
    }

    private ItemStack getSunTotem(PlayerInventory inventory) {
        ItemStack itemStack = inventory.getItemInMainHand();
        if (!sunTotem.isTrinket(itemStack)) {
            itemStack = inventory.getItemInOffHand();
            if (!sunTotem.isTrinket(itemStack))
                return null;
        }
        return itemStack;
    }

    private void useSunTotem(ItemStack itemStack, Player player) {
        player.getWorld().setTime(Utils.DAY_TIME);
        itemStack.subtract();
    }
}
