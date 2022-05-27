package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.CakeManager;
import com.astelon.aststrinkets.trinkets.MysteryCake;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Cake;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class CakeListener implements Listener {

    private final AstsTrinkets plugin;
    private final CakeManager cakeManager;
    private final MysteryCake mysteryCake;

    public CakeListener(AstsTrinkets plugin, TrinketManager trinketManager, CakeManager cakeManager) {
        this.plugin = plugin;
        this.cakeManager = cakeManager;
        mysteryCake = trinketManager.getMysteryCake();
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (mysteryCake.isTrinket(event.getItemInHand())) {
            if (mysteryCake.isEnabled())
                cakeManager.addCake(event.getBlock().getLocation());
        } else if (cakeManager.isCakeLocation(event.getBlock().getLocation())) {
            cakeManager.removeCake(event.getBlock().getLocation());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (cakeManager.isCakeLocation(event.getBlock().getLocation()))
            cakeManager.removeCake(event.getBlock().getLocation());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDestroy(BlockDestroyEvent event) {
        if (cakeManager.isCakeLocation(event.getBlock().getLocation()))
            cakeManager.removeCake(event.getBlock().getLocation());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        for (Block block: event.blockList()) {
            if (cakeManager.isCakeLocation(block.getLocation()))
                cakeManager.removeCake(block.getLocation());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block: event.blockList()) {
            if (cakeManager.isCakeLocation(block.getLocation()))
                cakeManager.removeCake(block.getLocation());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null) {
            if (cakeManager.isCakeLocation(block.getLocation())) {
                if (!(block.getBlockData() instanceof Cake))
                    cakeManager.removeCake(block.getLocation());
                Player player = event.getPlayer();
                boolean canEat = mysteryCake.isCheckHealth() ?
                        player.getHealth() < Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue() :
                        player.getFoodLevel() < 20;
                if (canEat) {
                    Cake cake = (Cake) block.getBlockData();
                    mysteryCake.applyRandomEffect(player);
                    if (cake.getBites() == 6)
                        cakeManager.removeCake(block.getLocation());
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        for (Block block: event.getBlocks()) {
            if (cakeManager.isCakeLocation(block.getLocation()))
                cakeManager.removeCake(block.getLocation());
        }
    }
}
