package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.trinkets.equipable.EffectGivingTrinket;
import com.astelon.aststrinkets.trinkets.equipable.Flippers;
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
    private final Flippers flippers;

    public PotionListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        nightVisionGoggles = trinketManager.getNightVisionGoggles();
        flippers = trinketManager.getFlippers();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
        addEffectOnJoin(helmet, player);
        addEffectOnJoin(chestplate, player);
        addEffectOnJoin(leggings, player);
        addEffectOnJoin(boots, player);
    }

    private void addEffectOnJoin(ItemStack itemStack, Player player) {
        Trinket trinket = trinketManager.getTrinket(itemStack);
        if (trinket instanceof EffectGivingTrinket effectGivingTrinket) {
            if (trinket.isEnabled() && trinketManager.isOwnedBy(itemStack, player))
                effectGivingTrinket.addEffect(player);
        }
    }

    @EventHandler
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;
        checkTrinketOnAdd(event, player, nightVisionGoggles);
        checkTrinketOnAdd(event, player, flippers);
    }

    private void checkTrinketOnAdd(EntityPotionEffectEvent event, Player player, EffectGivingTrinket effectGivingTrinket) {
        if (effectGivingTrinket.isEnabled()) {
            if (effectGivingTrinket.isEffect(event.getOldEffect()) && event.getAction() != EntityPotionEffectEvent.Action.ADDED) {
                ItemStack helmet = player.getInventory().getHelmet();
                ItemStack chestplate = player.getInventory().getChestplate();
                ItemStack leggings = player.getInventory().getLeggings();
                ItemStack boots = player.getInventory().getBoots();
                checkEffectOnAdd(helmet, player, event, effectGivingTrinket);
                checkEffectOnAdd(chestplate, player, event, effectGivingTrinket);
                checkEffectOnAdd(leggings, player, event, effectGivingTrinket);
                checkEffectOnAdd(boots, player, event, effectGivingTrinket);
            }
        }
    }

    private void checkEffectOnAdd(ItemStack itemStack, Player player, EntityPotionEffectEvent event,
                                  EffectGivingTrinket effectGivingTrinket) {
        if (effectGivingTrinket.isEnabledTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        ItemStack oldItem = event.getOldItem();
        ItemStack newItem = event.getNewItem();
        Player player = event.getPlayer();
        checkArmourChange(oldItem, newItem, player, nightVisionGoggles);
        checkArmourChange(oldItem, newItem, player, flippers);
    }

    private void checkArmourChange(ItemStack oldItem, ItemStack newItem, Player player, EffectGivingTrinket trinket) {
        if (trinket.isEnabledTrinket(oldItem) && trinketManager.isOwnedBy(oldItem, player)) {
            if ((!trinket.isEnabledTrinket(newItem) || !trinketManager.isOwnedBy(newItem, player)) &&
                    trinket.hasEffect(player)) {
                trinket.removeEffect(player);
            }
        } else if (trinket.isEnabledTrinket(newItem) && trinketManager.isOwnedBy(newItem, player)) {
            trinket.addEffect(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
        removeEffectOnLeave(helmet, player);
        removeEffectOnLeave(chestplate, player);
        removeEffectOnLeave(leggings, player);
        removeEffectOnLeave(boots, player);
    }

    private void removeEffectOnLeave(ItemStack itemStack, Player player) {
        Trinket trinket = trinketManager.getTrinket(itemStack);
        if (trinket instanceof EffectGivingTrinket effectTrinket && effectTrinket.isEnabledTrinket(itemStack) &&
                trinketManager.isOwnedBy(itemStack, player) && effectTrinket.hasEffect(player)) {
            effectTrinket.removeEffect(player);
        }
    }
}
