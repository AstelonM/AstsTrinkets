package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.YouthMilk;
import com.astelon.aststrinkets.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class EntityInteractListener implements Listener {

    private final AstsTrinkets plugin;
    private final YouthMilk youthMilk;

    public EntityInteractListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        youthMilk = trinketManager.getYouthMilk();
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (youthMilk.isEnabled() && event.getRightClicked() instanceof Ageable ageable) {
            if (!ageable.isAdult())
                return;
            Player player = event.getPlayer();
            EquipmentSlot hand = event.getHand();
            PlayerInventory inventory = player.getInventory();
            ItemStack item = hand == EquipmentSlot.HAND ? inventory.getItemInMainHand() : inventory.getItemInOffHand();
            if (youthMilk.isTrinket(item)) {
                event.setCancelled(true);
                //TODO check ageLock?
                ageable.setBaby();
                // Offhand slot is 40, might replace with a non-hardcoded method later
                int slot = hand == EquipmentSlot.HAND ? inventory.getHeldItemSlot() : 40;
                Utils.transformItem(item, new ItemStack(Material.BUCKET), slot, inventory, player);
                player.updateInventory();
            }
        }
    }
}
