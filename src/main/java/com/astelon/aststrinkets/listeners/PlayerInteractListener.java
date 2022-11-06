package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.*;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInteractListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final YouthMilk youthMilk;
    private final DiamondTrap diamondTrap;
    private final EmeraldTrap emeraldTrap;
    private final AmethystTrap amethystTrap;

    public PlayerInteractListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        youthMilk = trinketManager.getYouthMilk();
        diamondTrap = trinketManager.getDiamondTrap();
        emeraldTrap = trinketManager.getEmeraldTrap();
        amethystTrap = trinketManager.getAmethystTrap();
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        EquipmentSlot hand = event.getHand();
        PlayerInventory inventory = player.getInventory();
        ItemStack item = hand == EquipmentSlot.HAND ? inventory.getItemInMainHand() : inventory.getItemInOffHand();
        Entity entity = event.getRightClicked();
        // Offhand slot is 40, might replace with a non-hardcoded method later
        int slot = hand == EquipmentSlot.HAND ? inventory.getHeldItemSlot() : 40;
        //TODO ownership
        if (youthMilk.isEnabled() && youthMilk.isTrinket(item) && event.getRightClicked() instanceof Ageable ageable) {
            if (!ageable.isAdult())
                return;
            if (youthMilk.isTrinket(item)) {
                event.setCancelled(true);
                //TODO check ageLock?
                ageable.setBaby();
                Utils.transformItem(item, new ItemStack(Material.BUCKET), slot, inventory, player);
                player.updateInventory();
            }
        } else if (diamondTrap.isEnabled() && diamondTrap.isTrinket(item) && diamondTrap.isAllowedMob(entity)) {
            trapEntity(diamondTrap, item, entity, slot, inventory, player);
        } else if (emeraldTrap.isEnabled() && emeraldTrap.isTrinket(item) && emeraldTrap.isAllowedMob(entity)) {
            trapEntity(emeraldTrap, item, entity, slot, inventory, player);
        } else if (amethystTrap.isEnabled() && amethystTrap.isTrinket(item) && amethystTrap.isAllowedMob(entity)) {
            trapEntity(amethystTrap, item, entity, slot, inventory, player);
        }
    }

    private void trapEntity(CrystalTrap trap, ItemStack item, Entity entity, int slot, Inventory inventory, Player player) {
        if (trap.hasTrappedCreature(item)) {
            player.sendMessage(Component.text("This crystal trap already has a creature inside.", NamedTextColor.YELLOW));
            return;
        }
        ItemStack result = trap.trapCreature(item, entity);
        if (result == null)
            return;
        entity.remove();
        Utils.transformItem(item, result, slot, inventory, player);
        player.updateInventory();
        player.sendMessage(Component.text("You caught the " + Utils.getMobTypeAndName(entity) + " in a crystal " +
                "trap.", NamedTextColor.GOLD));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        Block block = event.getClickedBlock();
        ItemStack itemStack = event.getItem();
        Player player = event.getPlayer();
        if (block != null && itemStack != null && trinketManager.isOwnedBy(itemStack, player.getName())) {
            if (diamondTrap.isEnabled() && diamondTrap.isTrinket(itemStack)) {
                releaseEntity(diamondTrap, itemStack, block, player);
            } else if (emeraldTrap.isEnabled() && emeraldTrap.isTrinket(itemStack)) {
                releaseEntity(emeraldTrap, itemStack, block, player);
            } else if (amethystTrap.isEnabled() && amethystTrap.isTrinket(itemStack)) {
                releaseEntity(amethystTrap, itemStack, block, player);
            }
        }
    }

    private void releaseEntity(CrystalTrap trap, ItemStack itemStack, Block block, Player player) {
        if (!trap.hasTrappedCreature(itemStack))
            return;
        Location originalLocation = block.getLocation();
        Location spawnLocation = new Location(originalLocation.getWorld(), originalLocation.getBlockX() + 0.5,
                originalLocation.getBlockY() + 1, originalLocation.getBlockZ() + 0.5);
        if (!hasEnoughSpace(spawnLocation)) {
            player.sendMessage(Component.text("You need at least two blocks of free space to release the creature.",
                    NamedTextColor.RED));
            return;
        }
        Entity entity = trap.freeCreature(itemStack, spawnLocation.getWorld());
        if (entity == null) {
            player.sendMessage(Component.text("The creature could not be released. The trap might be corrupted.",
                    NamedTextColor.RED));
            return;
        }
        entity.spawnAt(spawnLocation);
        itemStack.subtract();
        player.updateInventory();
        player.sendMessage(Component.text("Successfully released the " + Utils.getMobTypeAndName(entity) + ".",
                NamedTextColor.GOLD));
    }

    private boolean hasEnoughSpace(Location location) {
        if (!location.getBlock().isPassable())
            return false;
        Location above = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ());
        return above.getBlock().isPassable();
    }
}
