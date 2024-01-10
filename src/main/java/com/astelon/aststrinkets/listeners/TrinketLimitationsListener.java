package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.SentientAxe;
import com.astelon.aststrinkets.trinkets.Souleater;
import com.astelon.aststrinkets.trinkets.VampiricSword;
import com.astelon.aststrinkets.trinkets.equipable.NightVisionGoggles;
import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;

public class TrinketLimitationsListener implements Listener {

    private final TrinketManager trinketManager;
    private final Souleater souleater;
    private final VampiricSword vampiricSword;
    private final SentientAxe sentientAxe;
    private final NightVisionGoggles nightVisionGoggles;

    public TrinketLimitationsListener(TrinketManager trinketManager) {
        this.trinketManager = trinketManager;
        souleater = trinketManager.getSouleater();
        vampiricSword = trinketManager.getVampiricSword();
        sentientAxe = trinketManager.getSentientAxe();
        nightVisionGoggles = trinketManager.getNightVisionGoggles();
    }

    @EventHandler
    public void onPrepareResult(PrepareResultEvent event) {
        if (event.getInventory() instanceof GrindstoneInventory inventory) {
            ItemStack itemStack = inventory.getResult();
            if (itemStack != null && trinketManager.isTrinket(itemStack) && !souleater.isTrinket(itemStack) &&
                    !vampiricSword.isTrinket(itemStack) && !nightVisionGoggles.isTrinket(itemStack)) {
                event.setResult(new ItemStack(itemStack.getType()));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEnchantItem(EnchantItemEvent event) {
        ItemStack itemStack = event.getItem();
        if (trinketManager.isTrinket(itemStack) && !souleater.isTrinket(itemStack) && !vampiricSword.isTrinket(itemStack) &&
                !sentientAxe.isTrinket(itemStack) && !nightVisionGoggles.isTrinket(itemStack)) { // Sentient Axe treated in its own listener
            event.setCancelled(true);
        }
    }
}
