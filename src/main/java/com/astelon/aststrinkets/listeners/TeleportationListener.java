package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.block.VoidGateway;
import com.astelon.aststrinkets.utils.Utils;
import com.destroystokyo.paper.event.entity.EntityTeleportEndGatewayEvent;
import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.block.EndGateway;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TeleportationListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final VoidGateway voidGateway;

    public TeleportationListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        voidGateway = trinketManager.getVoidGateway();
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleportEndGateway(PlayerTeleportEndGatewayEvent event) {
        EndGateway gateway = event.getGateway();
        Player player = event.getPlayer();
        Location originalLocation = event.getFrom();
        //TODO do I care about the owner?
        if (voidGateway.isTrinket(gateway)) {
            if (voidGateway.isEnabled()) {
                event.setCancelled(true);
                Location result = voidGateway.findLocation(gateway, player.getWorld());
                //TODO downsides?
                if (result == null) {
                    player.sendMessage(Component.text("You walk into the void, yet nothing happens.",
                            NamedTextColor.YELLOW));
                    plugin.getLogger().warning("Player " + player.getName() + " entered a Void Gateway at " +
                            Utils.serializeCoordsLogging(originalLocation) + " but could not find a location.");
                } else {
                    if (player.teleport(result)) {
                        player.sendMessage(Component.text("You walk into the void.", NamedTextColor.GREEN));
                        plugin.getLogger().info("Player " + player.getName() + " entered a Void Gateway and was " +
                                "teleported from " + Utils.serializeCoordsLogging(originalLocation) + " to " +
                                Utils.serializeCoordsLogging(result) + ".");
                    } else {
                        player.sendMessage(Component.text("You walk into the void, yet nothing happens.",
                                NamedTextColor.YELLOW));
                        plugin.getLogger().warning("Player " + player.getName() + " entered a Void Gateway at " +
                                Utils.serializeCoordsLogging(originalLocation) + " but could not be teleported to " +
                                Utils.serializeCoordsLogging(result) + ".");
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTeleportEndGateway(EntityTeleportEndGatewayEvent event) {
        if (!voidGateway.isAllowEntities())
            return;
        EndGateway gateway = event.getGateway();
        Entity entity = event.getEntity();
        Location originalLocation = event.getFrom();
        if (voidGateway.isTrinket(gateway)) {
            if (voidGateway.isEnabled()) {
                event.setCancelled(true);
                Location result = voidGateway.findLocation(gateway, entity.getWorld());
                if (result != null)  {
                    if (entity.teleport(result)) {
                        String type;
                        if (entity instanceof Item item) {
                            type = item.getItemStack().getType().name();
                        } else {
                            type = entity.getType().name();
                        }
                        plugin.getLogger().info("An entity of type " + type + " entered a Void Gateway and was " +
                                "teleported from " + Utils.serializeCoordsLogging(originalLocation) + " to " +
                                Utils.serializeCoordsLogging(result) + ".");
                    }
                }
            }
        }
    }
}
