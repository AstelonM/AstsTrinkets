package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Die;
import com.astelon.aststrinkets.trinkets.SentientAxe;
import com.astelon.aststrinkets.trinkets.Souleater;
import com.astelon.aststrinkets.trinkets.VampiricSword;
import com.astelon.aststrinkets.trinkets.equipable.*;
import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;

public class TrinketLimitationsListener implements Listener {

    private final TrinketManager trinketManager;
    private final Souleater souleater;
    private final VampiricSword vampiricSword;
    private final SentientAxe sentientAxe;
    private final NightVisionGoggles nightVisionGoggles;
    private final Die die;
    private final Flippers flippers;
    private final DivingHelmet divingHelmet;
    private final FireproofVest fireproofVest;
    private final HydraulicBoots hydraulicBoots;

    public TrinketLimitationsListener(TrinketManager trinketManager) {
        this.trinketManager = trinketManager;
        souleater = trinketManager.getSouleater();
        vampiricSword = trinketManager.getVampiricSword();
        sentientAxe = trinketManager.getSentientAxe();
        nightVisionGoggles = trinketManager.getNightVisionGoggles();
        die = trinketManager.getDie();
        flippers = trinketManager.getFlippers();
        divingHelmet = trinketManager.getDivingHelmet();
        fireproofVest = trinketManager.getFireproofVest();
        hydraulicBoots = trinketManager.getHydraulicBoots();
    }

    @EventHandler
    public void onPrepareResult(PrepareResultEvent event) {
        if (event.getInventory() instanceof GrindstoneInventory inventory) {
            ItemStack itemStack = inventory.getResult();
            if (itemStack != null && trinketManager.isTrinket(itemStack) && !souleater.isTrinket(itemStack) &&
                    !vampiricSword.isTrinket(itemStack) && !nightVisionGoggles.isTrinket(itemStack) && !flippers.isTrinket(itemStack) &&
                    !divingHelmet.isTrinket(itemStack) && !fireproofVest.isTrinket(itemStack) && !hydraulicBoots.isTrinket(itemStack)) {
                event.setResult(new ItemStack(itemStack.getType()));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEnchantItem(EnchantItemEvent event) {
        ItemStack itemStack = event.getItem();
        if (trinketManager.isTrinket(itemStack) && !souleater.isTrinket(itemStack) && !vampiricSword.isTrinket(itemStack) &&
                !sentientAxe.isTrinket(itemStack) && !nightVisionGoggles.isTrinket(itemStack) &&
                !flippers.isTrinket(itemStack) && !divingHelmet.isTrinket(itemStack) &&
                !fireproofVest.isTrinket(itemStack) && !hydraulicBoots.isTrinket(itemStack)) { // Sentient Axe treated in its own listener
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack itemStack = event.getItemInHand();
        if (die.isTrinket(itemStack)) {
            event.setCancelled(true);
        }
    }
}
