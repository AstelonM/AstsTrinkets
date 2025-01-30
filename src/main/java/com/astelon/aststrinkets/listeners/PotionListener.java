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
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
        addNightVisionOnJoin(helmet, player);
        addNightVisionOnJoin(chestplate, player);
        addNightVisionOnJoin(leggings, player);
        addNightVisionOnJoin(boots, player);
    }

    private void addNightVisionOnJoin(ItemStack itemStack, Player player) {
        if (nightVisionGoggles.isEnabledTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, player)) {
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
            ItemStack chestplate = player.getInventory().getChestplate();
            ItemStack leggings = player.getInventory().getLeggings();
            ItemStack boots = player.getInventory().getBoots();
            checkNightVisionOnAdd(helmet, player, event);
            checkNightVisionOnAdd(chestplate, player, event);
            checkNightVisionOnAdd(leggings, player, event);
            checkNightVisionOnAdd(boots, player, event);
        }
    }

    private void checkNightVisionOnAdd(ItemStack itemStack, Player player, EntityPotionEffectEvent event) {
        if (nightVisionGoggles.isEnabledTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
        removeNightVisionOnLeave(helmet, player);
        removeNightVisionOnLeave(chestplate, player);
        removeNightVisionOnLeave(leggings, player);
        removeNightVisionOnLeave(boots, player);
    }

    private void removeNightVisionOnLeave(ItemStack itemStack, Player player) {
        if (nightVisionGoggles.isEnabledTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, player) &&
                nightVisionGoggles.hasNightVision(player)) {
            nightVisionGoggles.removeNightVision(player);
        }
    }
}
