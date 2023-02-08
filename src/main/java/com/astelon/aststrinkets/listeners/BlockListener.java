package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.block.InfinityItem;
import com.astelon.aststrinkets.trinkets.ShulkerBoxContainmentUnit;
import com.astelon.aststrinkets.trinkets.block.Spinneret;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;

public class BlockListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final Spinneret spinneret;
    private final InfinityItem infinityItem;
    private final ShulkerBoxContainmentUnit shulkerBoxContainmentUnit;

    public BlockListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        spinneret = trinketManager.getSpinneret();
        infinityItem = trinketManager.getInfinityItem();
        shulkerBoxContainmentUnit = trinketManager.getShulkerBoxContainmentUnit();
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
                }
            }
        }
        if (trinketManager.isOwnedBy(otherItem, player.getName())) {
            if (spinneret.isEnabledTrinket(otherItem)) {
                if (event.getItemInHand().getType() == Material.STRING)
                    event.getBlock().setType(Material.COBWEB);
            }
        }
    }
}
