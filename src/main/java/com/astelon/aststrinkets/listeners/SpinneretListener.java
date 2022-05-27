package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Spinneret;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SpinneretListener implements Listener {

    private final AstsTrinkets plugin;
    private final Spinneret spinneret;

    public SpinneretListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        spinneret = trinketManager.getSpinneret();
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (spinneret.isEnabled()) {
            Player player = event.getPlayer();
            EquipmentSlot hand = event.getHand();
            ItemStack otherItem;
            if (hand == EquipmentSlot.HAND)
                otherItem = player.getInventory().getItemInOffHand();
            else
                otherItem = player.getInventory().getItemInMainHand();
            if (spinneret.isTrinket(otherItem) && event.getItemInHand().getType() == Material.STRING)
                event.getBlock().setType(Material.COBWEB);
        }
    }
}
