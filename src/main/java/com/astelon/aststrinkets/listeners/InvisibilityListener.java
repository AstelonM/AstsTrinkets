package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.managers.InvisibilityManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.trinkets.equipable.TrueSightCap;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InvisibilityListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final InvisibilityManager invisibilityManager;

    private final TrueSightCap trueSightCap;

    public InvisibilityListener(AstsTrinkets plugin, TrinketManager trinketManager, InvisibilityManager invisibilityManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.invisibilityManager = invisibilityManager;
        trueSightCap = trinketManager.getTrueSightCap();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack helmet = inventory.getHelmet();
        ItemStack chestplate = inventory.getChestplate();
        ItemStack leggings = inventory.getLeggings();
        ItemStack boots = inventory.getBoots();
        checkTrueSight(helmet, player);
        checkTrueSight(chestplate, player);
        checkTrueSight(leggings, player);
        checkTrueSight(boots, player);
        if (!player.hasPermission("aststrinkets.trinket.seeinvisible") && !invisibilityManager.isTrueSightPlayer(player)) {
            for (String inviPlayerName: invisibilityManager.getInvisiblePlayers()) {
                Player inviPlayer = Bukkit.getPlayer(inviPlayerName);
                if (inviPlayer == null) {
                    plugin.getLogger().warning("Player " + inviPlayerName + " is offline but counts as invisible.");
                    continue;
                }
                player.hidePlayer(plugin, inviPlayer);
            }
        }
        for (String inviPlayerName: invisibilityManager.getTrulyInvisiblePlayers()) {
            Player inviPlayer = Bukkit.getPlayer(inviPlayerName);
            if (inviPlayer == null) {
                plugin.getLogger().warning("Player " + inviPlayerName + " is offline but counts as truly invisible.");
                continue;
            }
            player.hidePlayer(plugin, inviPlayer);
        }
        checkInvisibility(helmet, player);
        checkInvisibility(chestplate, player);
        checkInvisibility(leggings, player);
        checkInvisibility(boots, player);
    }

    private void checkTrueSight(ItemStack itemStack, Player player) {
        if (trueSightCap.isTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, player))
            invisibilityManager.addTrueSightPlayer(player);
    }

    private void checkInvisibility(ItemStack itemStack, Player player) {
        Trinket trinket = trinketManager.getTrinket(itemStack);
        if (trinket != null && trinket.isEnabled() && trinketManager.isOwnedBy(itemStack, player.getName())) {
            if (trinket.getPower() == Power.INVISIBILITY)
                invisibilityManager.addInvisiblePlayer(player);
            if (trinket.getPower() == Power.TRUE_INVISIBILITY)
                invisibilityManager.addTrulyInvisiblePlayer(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        invisibilityManager.removeInvisiblePlayer(player);
        invisibilityManager.removeTrulyInvisiblePlayer(player);
        invisibilityManager.removeTrueSightPlayer(player, false);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!event.getKeepInventory()) {
            Player player = event.getEntity();
            invisibilityManager.removeInvisiblePlayer(player);
            invisibilityManager.removeTrulyInvisiblePlayer(player);
            invisibilityManager.removeTrueSightPlayer(player, true);
        }
    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        ItemStack oldItem = event.getOldItem();
        ItemStack newItem = event.getNewItem();
        Trinket oldTrinket = trinketManager.getTrinket(oldItem);
        Trinket newTrinket = trinketManager.getTrinket(newItem);
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (oldTrinket == newTrinket && trinketManager.isOwnedBy(oldItem, playerName) && trinketManager.isOwnedBy(newItem, playerName))
            return;
        if (oldTrinket != null && oldTrinket.isEnabled() && trinketManager.isOwnedBy(oldItem, playerName)) {
            if (oldTrinket.getPower() == Power.INVISIBILITY) {
                if (newTrinket == null || !newTrinket.isEnabled() || newTrinket.getPower() != Power.INVISIBILITY ||
                        !trinketManager.isOwnedBy(newItem, playerName))
                    invisibilityManager.removeInvisiblePlayer(player);
                else
                    return;
            } else if (oldTrinket.getPower() == Power.TRUE_INVISIBILITY) {
                if (newTrinket == null || !newTrinket.isEnabled() || newTrinket.getPower() != Power.TRUE_INVISIBILITY ||
                        !trinketManager.isOwnedBy(newItem, playerName))
                    invisibilityManager.removeTrulyInvisiblePlayer(player);
                else
                    return;
            }
        }
        if (newTrinket != null && newTrinket.isEnabled() && trinketManager.isOwnedBy(newItem, playerName)) {
            if (newTrinket.getPower() == Power.INVISIBILITY)
                invisibilityManager.addInvisiblePlayer(player);
            else if (newTrinket.getPower() == Power.TRUE_INVISIBILITY)
                invisibilityManager.addTrulyInvisiblePlayer(player);
        }
        if (!trueSightCap.isEnabled())
            return;
        if (trueSightCap.isTrinket(oldItem) && trinketManager.isOwnedBy(oldItem, playerName)) {
            if (!trueSightCap.isTrinket(newItem) || !trinketManager.isOwnedBy(newItem, playerName)) {
                invisibilityManager.removeTrueSightPlayer(player, true);
            }
        } else if (trueSightCap.isTrinket(newItem) && trinketManager.isOwnedBy(newItem, playerName)) {
            invisibilityManager.addTrueSightPlayer(player);
        }
    }
}
