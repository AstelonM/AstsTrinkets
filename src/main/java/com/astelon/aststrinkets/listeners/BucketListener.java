package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.inventory.PortableConcreteMixer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BucketListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final PortableConcreteMixer portableConcreteMixer;

    public BucketListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        portableConcreteMixer = trinketManager.getPortableConcreteMixer();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        PlayerInventory inventory = event.getPlayer().getInventory();
        ItemStack bucket = inventory.getItem(event.getHand());
        if (portableConcreteMixer.isTrinket(bucket)) {
            Block block = event.getBlock();
            if (block.getType() == Material.WATER ||
                    (block.getBlockData() instanceof Waterlogged waterlogged && waterlogged.isWaterlogged())) {
                ItemStack result = portableConcreteMixer.fillMixer(bucket);
                event.setItemStack(result);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        PlayerInventory inventory = event.getPlayer().getInventory();
        ItemStack bucket = inventory.getItem(event.getHand());
        if (portableConcreteMixer.isTrinket(bucket)) {
            ItemStack result = portableConcreteMixer.emptyMixer(bucket);
            event.setItemStack(result);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketEntity(PlayerBucketEntityEvent event) {
        ItemStack bucket = event.getOriginalBucket();
        if (portableConcreteMixer.isTrinket(bucket))
            event.setCancelled(true);
    }

    // Preferable over the bucket fill method, that one causes bucket to appear as a milk bucket until interacted with
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        PlayerInventory inventory = event.getPlayer().getInventory();
        ItemStack bucket = inventory.getItem(event.getHand());
        if (portableConcreteMixer.isTrinket(bucket)) {
            Entity entity = event.getRightClicked();
            EntityType type =  entity.getType();
            if (type == EntityType.COW || type == EntityType.GOAT) {
                event.setCancelled(true);
            }
        }
    }
}
