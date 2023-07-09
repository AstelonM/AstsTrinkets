package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.CakeManager;
import com.astelon.aststrinkets.trinkets.block.MysteryCake;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.utils.Utils;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Cake;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Objects;

public class CakeListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final CakeManager cakeManager;
    private final MysteryCake mysteryCake;

    public CakeListener(AstsTrinkets plugin, TrinketManager trinketManager, CakeManager cakeManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.cakeManager = cakeManager;
        mysteryCake = trinketManager.getMysteryCake();
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        String playerName = event.getPlayer().getName();
        Location location = event.getBlock().getLocation();
        if (mysteryCake.isTrinket(item) && trinketManager.isOwnedBy(item, playerName)) {
            if (mysteryCake.isEnabled()) {
                cakeManager.addCake(location);
                plugin.getLogger().info("Mystery cake created at " + Utils.locationToString(location) + " by player " +
                        playerName + ".");
            }
        } else if (cakeManager.isCakeLocation(location)) {
            cakeManager.removeCake(location);
            plugin.getLogger().info("Mystery cake destroyed at " + Utils.locationToString(location) + " by an " +
                    "unknown cause at an unknown time.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractHighest(PlayerInteractEvent event) {
        if (mysteryCake.isUseLowestPriorityListener())
            return;
        playerInteractExecution(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractLowest(PlayerInteractEvent event) {
        if (!mysteryCake.isUseLowestPriorityListener())
            return;
        playerInteractExecution(event);
    }

    private void playerInteractExecution(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null) {
            Location location = block.getLocation();
            if (cakeManager.isCakeLocation(location)) {
                if (!(block.getBlockData() instanceof Cake)) {
                    cakeManager.removeCake(location);
                    plugin.getLogger().info("Mystery cake destroyed at " + Utils.locationToString(location) + " by an " +
                            "unknown cause at an unknown time.");
                }
                if (event.useInteractedBlock() == Event.Result.DENY && ! mysteryCake.isIgnoreBlockRestrictions())
                    return;
                Player player = event.getPlayer();
                boolean canEat = mysteryCake.isCheckHealth() ?
                        player.getHealth() < Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue() :
                        player.getFoodLevel() < 20;
                if (canEat) {
                    Cake cake = (Cake) block.getBlockData();
                    PotionEffect effect = mysteryCake.applyRandomEffect(player);
                    if (cake.getBites() == cake.getMaximumBites()) {
                        if (mysteryCake.isConsumeCakeEnabled()) {
                            block.setType(Material.AIR);
                            event.setUseInteractedBlock(Event.Result.DENY);
                        }
                        cakeManager.removeCake(location);
                        plugin.getLogger().info("Player " + player.getName() + " consumed a mystery cake at " +
                                Utils.locationToString(location) + " and was given " + effect.getType().getName());
                    } else {
                        if (mysteryCake.isConsumeCakeEnabled()) {
                            cake.setBites(cake.getBites() + 1);
                            block.setBlockData(cake);
                            event.setUseInteractedBlock(Event.Result.DENY);
                        }
                        plugin.getLogger().info("Player " + player.getName() + " bit a mystery cake at " +
                                Utils.locationToString(location) + " and was given " + effect.getType().getName());
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        if (cakeManager.isCakeLocation(location)) {
            String playerName = event.getPlayer().getName();
            cakeManager.removeCake(location);
            plugin.getLogger().info("Mystery cake destroyed at " + Utils.locationToString(location) + " by player " +
                    playerName + ".");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDestroy(BlockDestroyEvent event) {
        Location location = event.getBlock().getLocation();
        if (cakeManager.isCakeLocation(location)) {
            cakeManager.removeCake(location);
            plugin.getLogger().info("Mystery cake destroyed at " + Utils.locationToString(location) + " as a " +
                    "secondary effect (probably the block underneath was broken).");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        for (Block block: event.blockList()) {
            Location location = block.getLocation();
            if (cakeManager.isCakeLocation(location)) {
                cakeManager.removeCake(location);
                plugin.getLogger().info("Mystery cake destroyed at " + Utils.locationToString(location) + " by a " +
                        "block explosion.");
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block: event.blockList()) {
            Location location = block.getLocation();
            if (cakeManager.isCakeLocation(location)) {
                cakeManager.removeCake(location);
                plugin.getLogger().info("Mystery cake destroyed at " + Utils.locationToString(location) + " by an " +
                        "entity explosion.");
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        for (Block block: event.getBlocks()) {
            Location location = block.getLocation();
            if (cakeManager.isCakeLocation(block.getLocation())) {
                cakeManager.removeCake(block.getLocation());
                plugin.getLogger().info("Mystery cake destroyed at " + Utils.locationToString(location) + " by a " +
                        "piston.");
            }
        }
    }
}
