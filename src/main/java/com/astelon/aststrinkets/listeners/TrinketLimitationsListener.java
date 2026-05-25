package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.*;
import com.astelon.aststrinkets.trinkets.equipable.*;
import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import io.papermc.paper.event.entity.EntityDyeEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
    private final HuntingBow huntingBow;
    private final AdvancedFlippers advancedFlippers;
    private final ItemMagnet itemMagnet;
    private final FrogLegs frogLegs;
    private final UniversalFertilizer universalFertilizer;

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
        huntingBow = trinketManager.getHuntingBow();
        advancedFlippers = trinketManager.getAdvancedFlippers();
        itemMagnet = trinketManager.getItemMagnet();
        frogLegs = trinketManager.getFrogLegs();
        universalFertilizer = trinketManager.getUniversalFertilizer();
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftItem(CraftItemEvent event) { //TODO the rest of the noncraftable trinkets
        ItemStack[] items = event.getInventory().getMatrix();
        for (ItemStack item: items) {
            if (itemMagnet.isTrinket(item)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPrepareResult(PrepareResultEvent event) {
        Inventory inventory = event.getInventory();
        for (ItemStack item: inventory.getContents()) {
            if (item != null && itemMagnet.isTrinket(item)) {
                event.setResult(null);
            }
        }
        if (inventory instanceof GrindstoneInventory grindstoneInventory) {
            ItemStack itemStack = grindstoneInventory.getResult();
            if (itemStack != null && trinketManager.isTrinket(itemStack) && !souleater.isTrinket(itemStack) &&
                    !vampiricSword.isTrinket(itemStack) && !nightVisionGoggles.isTrinket(itemStack) && !flippers.isTrinket(itemStack) &&
                    !divingHelmet.isTrinket(itemStack) && !fireproofVest.isTrinket(itemStack) && !hydraulicBoots.isTrinket(itemStack) &&
                    !huntingBow.isTrinket(itemStack) && !advancedFlippers.isTrinket(itemStack) && !frogLegs.isTrinket(itemStack)) {
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
                !fireproofVest.isTrinket(itemStack) && !hydraulicBoots.isTrinket(itemStack) &&
                !huntingBow.isTrinket(itemStack) && !advancedFlippers.isTrinket(itemStack) &&
                !frogLegs.isTrinket(itemStack)) { // Sentient Axe treated in its own listener
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

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        PlayerInventory inventory = event.getPlayer().getInventory();
        ItemStack itemStack = inventory.getItem(event.getHand());
        EntityType type = event.getRightClicked().getType();
        if (type == EntityType.IRON_GOLEM) {
            if (itemMagnet.isTrinket(itemStack)) {
                event.setCancelled(true);
            }
        }
        if (universalFertilizer.isTrinket(itemStack)) {
            event.setCancelled(true);
        }
    }
}
