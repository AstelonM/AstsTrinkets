package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.equipable.NightVisionGoggles;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

//TODO merge player join/quit in one listener, rest in their own, maybe
public class PotionListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final NightVisionGoggles nightVisionGoggles;

    public PotionListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        nightVisionGoggles = trinketManager.getNightVisionGoggles();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ItemStack helmet = player.getInventory().getHelmet();
        if (nightVisionGoggles.isEnabledTrinket(helmet) && trinketManager.isOwnedBy(helmet, player)) {
            nightVisionGoggles.addNightVision(player);
        }
    }

    @EventHandler
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;
        if (!nightVisionGoggles.isEnabled())
            return;
        if (nightVisionGoggles.isNightVision(event.getOldEffect()) && event.getAction() != EntityPotionEffectEvent.Action.ADDED) {
            ItemStack helmet = player.getInventory().getHelmet();
            if (nightVisionGoggles.isEnabledTrinket(helmet) && trinketManager.isOwnedBy(helmet, player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        if (event.getSlotType() == PlayerArmorChangeEvent.SlotType.HEAD) {
            if (!nightVisionGoggles.isEnabled())
                return;
            ItemStack oldItem = event.getOldItem();
            ItemStack newItem = event.getNewItem();
            Player player = event.getPlayer();
            if (nightVisionGoggles.isEnabledTrinket(oldItem) && trinketManager.isOwnedBy(oldItem, player)) {
                if ((!nightVisionGoggles.isEnabledTrinket(newItem) || !trinketManager.isOwnedBy(newItem, player)) &&
                        nightVisionGoggles.hasNightVision(player)) {
                    nightVisionGoggles.removeNightVision(player);
                }
            } else if (nightVisionGoggles.isEnabledTrinket(newItem) && trinketManager.isOwnedBy(newItem, player)) {
                nightVisionGoggles.addNightVision(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ItemStack helmet = player.getInventory().getHelmet();
        if (nightVisionGoggles.isEnabledTrinket(helmet) && trinketManager.isOwnedBy(helmet, player) &&
                nightVisionGoggles.hasNightVision(player)) {
            nightVisionGoggles.removeNightVision(player);
        }
    }
}
