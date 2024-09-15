package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.ItemMagnet;
import com.astelon.aststrinkets.trinkets.block.GatewayAnchor;
import com.astelon.aststrinkets.trinkets.block.InfinityItem;
import com.astelon.aststrinkets.trinkets.ShulkerBoxContainmentUnit;
import com.astelon.aststrinkets.trinkets.block.Spinneret;
import com.astelon.aststrinkets.trinkets.block.Terrarium;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.EndGateway;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BlockListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final MobInfoManager mobInfoManager;
    private final Spinneret spinneret;
    private final InfinityItem infinityItem;
    private final ShulkerBoxContainmentUnit shulkerBoxContainmentUnit;
    private final GatewayAnchor gatewayAnchor;
    private final ItemMagnet itemMagnet;
    private final Terrarium terrarium;

    public BlockListener(AstsTrinkets plugin, TrinketManager trinketManager, MobInfoManager mobInfoManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.mobInfoManager = mobInfoManager;
        spinneret = trinketManager.getSpinneret();
        infinityItem = trinketManager.getInfinityItem();
        shulkerBoxContainmentUnit = trinketManager.getShulkerBoxContainmentUnit();
        gatewayAnchor = trinketManager.getGatewayAnchor();
        itemMagnet = trinketManager.getItemMagnet();
        terrarium = trinketManager.getTerrarium();
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        EquipmentSlot hand = event.getHand();
        ItemStack placedItem;
        ItemStack otherItem;
        PlayerInventory inventory = player.getInventory();
        boolean mainHand = hand == EquipmentSlot.HAND;
        if (mainHand) {
            placedItem = inventory.getItemInMainHand();
            otherItem = inventory.getItemInOffHand();
        } else {
            placedItem = inventory.getItemInOffHand();
            otherItem = inventory.getItemInMainHand();
        }
        if (trinketManager.isOwnedBy(placedItem, player.getName())) {
            if (infinityItem.isEnabledTrinket(placedItem)) {
                //TODO Find a way to place the block without consuming the item and without cancelling the event
                inventory.setItemInMainHand(placedItem);
            } else if (shulkerBoxContainmentUnit.isEnabledTrinket(placedItem)) {
                if (shulkerBoxContainmentUnit.hasShulkerBox(placedItem)) {
                    ItemStack shulkerItem = shulkerBoxContainmentUnit.getContainedShulkerBox(placedItem);
                    if (shulkerItem == null) {
                        player.sendMessage(Component.text("This containment unit has become corrupted. The " +
                                "shulker box within could not be retrieved.", NamedTextColor.RED));
                        event.setCancelled(true);
                        return;
                    }
                    BlockStateMeta meta = (BlockStateMeta) shulkerItem.getItemMeta();
                    ShulkerBox shulkerBlockState = (ShulkerBox) meta.getBlockState();
                    ItemStack[] shulkerInventory = shulkerBlockState.getInventory().getContents();
                    Block block = event.getBlock();
                    block.setType(shulkerItem.getType());
                    ShulkerBox oldState = (ShulkerBox) block.getState();
                    oldState.customName(meta.displayName());
                    oldState.update(true);
                    oldState = (ShulkerBox) block.getState();
                    oldState.getInventory().setContents(shulkerInventory);
                } else {
                    event.setCancelled(true);
                }
            } else if (gatewayAnchor.isEnabledTrinket(placedItem)) {
                if (!gatewayAnchor.hasLocation(placedItem)) {
                    player.sendMessage(Component.text("This anchor has no location linked.", NamedTextColor.RED));
                    event.setCancelled(true);
                    return;
                }
                Location location = gatewayAnchor.getLocation(placedItem);
                if (location == null) {
                    player.sendMessage(Component.text("The location this anchor is linked to is corrupted.", NamedTextColor.RED));
                    event.setCancelled(true);
                    return;
                }
                if (!player.getWorld().equals(location.getWorld())) {
                    player.sendMessage(Component.text("This anchor is linked to a location in a different world.",
                            NamedTextColor.RED));
                    event.setCancelled(true);
                    return;
                }
                Block block = event.getBlock();
                block.setType(Material.END_GATEWAY);
                EndGateway gateway = (EndGateway) block.getState();
                gateway.setAge(Long.MIN_VALUE);
                gateway.setExactTeleport(true);
                gateway.setExitLocation(location);
                gateway.update(true);
                plugin.getLogger().info("Player " + player.getName() + " created an end gateway at " +
                        Utils.serializeCoordsLogging(block.getLocation()) + " using a Gateway Anchor " +
                        "leading to " + Utils.serializeCoordsLogging(location));
            } else if (terrarium.isEnabledTrinket(placedItem)) {
                if (!terrarium.canUse(placedItem)) {
                    event.setCancelled(true);
                    return;
                }
                if (!terrarium.hasTrappedCreature(placedItem)) {
                    player.sendMessage(Component.text("This terrarium is empty.", NamedTextColor.RED));
                    event.setCancelled(true);
                    return;
                }
                Entity entity = terrarium.getTrappedCreature(placedItem, player.getWorld());
                if (entity == null) {
                    player.sendMessage(Component.text("The creature could not be released. The terrarium might be corrupted.",
                            NamedTextColor.RED));
                    event.setCancelled(true);
                    return;
                }
                EntityType entityType = entity.getType();
                Block block = event.getBlock();
                block.setType(Material.SPAWNER);
                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                spawner.setSpawnedType(entityType);
                spawner.update(true);
                plugin.getLogger().info("Player " + player.getName() + " created a " + mobInfoManager.getTypeName(entityType) +
                        " spawner at " + Utils.serializeCoords(block.getLocation()) + " using a Terrarium.");
            }
        }
        if (trinketManager.isOwnedBy(otherItem, player.getName())) {
            if (spinneret.isEnabledTrinket(otherItem)) {
                if (event.getItemInHand().getType() == Material.STRING)
                    event.getBlock().setType(Material.COBWEB);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDropItem(BlockDropItemEvent event) {
        if (itemMagnet.isEnabled()) {
            List<Item> items = event.getItems();
            if (items.isEmpty())
                return;
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = inventory.getItemInMainHand();
            if (!itemMagnet.isTrinket(itemStack) || !trinketManager.isOwnedBy(itemStack, player.getName())) {
                itemStack = inventory.getItemInOffHand();
                if (!itemMagnet.isTrinket(itemStack) || !trinketManager.isOwnedBy(itemStack, player.getName()))
                    return;
            }
            Location location = player.getLocation();
            Iterator<Item> iterator = items.iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                ItemStack toAdd = item.getItemStack();
                iterator.remove();
                HashMap<Integer, ItemStack> notAdded = inventory.addItem(toAdd);
                for (ItemStack remaining: notAdded.values())
                    player.getWorld().dropItem(location, remaining);
            }
        }
    }
}
