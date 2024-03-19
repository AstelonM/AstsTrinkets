package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Die;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;

    private final Die die;

    public ItemListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        die = trinketManager.getDie();
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();
        Player player = event.getPlayer();
        if (player.isSneaking())
            return;
        if (die.isEnabledTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, player)) {
            List<Integer> numbers = die.rollDie(item);
            plugin.getLogger().info("Player " + player.getName() + " threw a die and got " + numbers + ".");
        }
    }
}
