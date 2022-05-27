package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.managers.InvisibilityManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class InvisibilityListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final InvisibilityManager invisibilityManager;

    public InvisibilityListener(AstsTrinkets plugin, TrinketManager trinketManager, InvisibilityManager invisibilityManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.invisibilityManager = invisibilityManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("aststrinkets.trinket.seeinvisible")) {
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
        ItemStack itemStack = player.getInventory().getChestplate();
        Trinket trinket = trinketManager.getTrinket(itemStack);
        if (trinket != null && trinket.isEnabled()) {
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
    }

    //TODO sa pastrez invizibilitatea daca tunica ramane?
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!event.getKeepInventory()) {
            Player player = event.getEntity();
            invisibilityManager.removeInvisiblePlayer(player);
            invisibilityManager.removeTrulyInvisiblePlayer(player);
        }
    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        if (event.getSlotType() == PlayerArmorChangeEvent.SlotType.CHEST) {
            ItemStack oldItem = event.getOldItem();
            ItemStack newItem = event.getNewItem();
            Trinket oldTrinket = trinketManager.getTrinket(oldItem);
            Trinket newTrinket = trinketManager.getTrinket(newItem);
            if (oldTrinket == newTrinket)
                return;
            Player player = event.getPlayer();
            if (oldTrinket != null && oldTrinket.isEnabled()) {
                if (oldTrinket.getPower() == Power.INVISIBILITY) {
                    if (newTrinket == null || !newTrinket.isEnabled() || newTrinket.getPower() != Power.INVISIBILITY)
                        invisibilityManager.removeInvisiblePlayer(player);
                    else
                        return;
                } else if (oldTrinket.getPower() == Power.TRUE_INVISIBILITY) {
                    if (newTrinket == null || !newTrinket.isEnabled() || newTrinket.getPower() != Power.TRUE_INVISIBILITY)
                        invisibilityManager.removeTrulyInvisiblePlayer(player);
                    else
                        return;
                }
            }
            if (newTrinket != null && newTrinket.isEnabled()) {
                if (newTrinket.getPower() == Power.INVISIBILITY)
                    invisibilityManager.addInvisiblePlayer(player);
                else if (newTrinket.getPower() == Power.TRUE_INVISIBILITY)
                    invisibilityManager.addTrulyInvisiblePlayer(player);
            }
        }
    }
}
