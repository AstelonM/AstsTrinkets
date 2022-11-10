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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public class PlayerInteractListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final HashMap<Player, Long> cooldowns;
    private final YouthMilk youthMilk;
    private final DiamondTrap diamondTrap;
    private final EmeraldTrap emeraldTrap;
    private final AmethystTrap amethystTrap;
    private final NetherStarTrap netherStarTrap;

    public PlayerInteractListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        cooldowns = new HashMap<>();
        youthMilk = trinketManager.getYouthMilk();
        diamondTrap = trinketManager.getDiamondTrap();
        emeraldTrap = trinketManager.getEmeraldTrap();
        amethystTrap = trinketManager.getAmethystTrap();
        netherStarTrap = trinketManager.getNetherStarTrap();
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
        if (trinketManager.isOwnedBy(item, player.getName())) {
            if (youthMilk.isEnabled() && youthMilk.isTrinket(item) && event.getRightClicked() instanceof Ageable ageable) {
                if (!ageable.isAdult())
                    return;
                if (youthMilk.isTrinket(item)) {
                    event.setCancelled(true);
                    //TODO check ageLock?
                    ageable.setBaby();
                    Utils.transformItem(item, new ItemStack(Material.BUCKET), slot, inventory, player);
                    player.updateInventory();
                    plugin.getLogger().info("Youth milk used on " + Utils.getMobTypeAndName(ageable) + " at " +
                            Utils.locationToString(ageable.getLocation()) + " by player " + player.getName() + ".");
                }
            } else if (diamondTrap.isEnabled() && diamondTrap.isTrinket(item)) {
                trapEntity(diamondTrap, item, entity, slot, inventory, player);
            } else if (emeraldTrap.isEnabled() && emeraldTrap.isTrinket(item)) {
                trapEntity(emeraldTrap, item, entity, slot, inventory, player);
            } else if (amethystTrap.isEnabled() && amethystTrap.isTrinket(item)) {
                trapEntity(amethystTrap, item, entity, slot, inventory, player);
            } else if (netherStarTrap.isEnabled() && netherStarTrap.isTrinket(item)) {
                trapEntity(netherStarTrap, item, entity, slot, inventory, player);
            }
        }
    }

    private void trapEntity(CrystalTrap trap, ItemStack item, Entity entity, int slot, Inventory inventory, Player player) {
        long now = System.currentTimeMillis();
        if (now - cooldowns.getOrDefault(player, 0L) <= 1000)
            return;
        if (trap.hasTrappedCreature(item)) {
            player.sendMessage(Component.text("This crystal trap already has a creature inside.", NamedTextColor.YELLOW));
            return;
        }
        if (!trap.isAllowedMob(entity)) {
            player.sendMessage(Component.text("It cannot be contained within this crystal.", NamedTextColor.RED));
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
        plugin.getLogger().info(Utils.getMobTypeAndName(entity) + " trapped in a crystal trap at " +
                Utils.locationToString(entity.getLocation()) + " by player " + player.getName() + ".");
        cooldowns.put(player, now);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        Block block = event.getClickedBlock();
        // Only checking for OFF_HAND because it is never used for specific interactions (open chests etc.), and it
        // is always called otherwise. It is used for placing blocks from offhand, though, so checking for that as well
        if (block != null && event.getHand() == EquipmentSlot.OFF_HAND && !event.isBlockInHand()) {
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            ItemStack mainHandItem = inventory.getItemInMainHand();
            ItemStack offHandItem = inventory.getItemInOffHand();
            if (trinketManager.isTrinket(mainHandItem)) {
                useTrinkets(mainHandItem, player, block, inventory.getHeldItemSlot());
            } else if (trinketManager.isTrinket(offHandItem)) { //TODO use both hands at the same time?
                useTrinkets(mainHandItem, player, block, 40);
            }
        }
    }

    private void useTrinkets(ItemStack itemStack, Player player, Block block, int slot) {
        if (itemStack != null && trinketManager.isOwnedBy(itemStack, player.getName())) {
            if (diamondTrap.isEnabled() && diamondTrap.isTrinket(itemStack)) {
                releaseEntity(diamondTrap, itemStack, block, player, slot);
            } else if (emeraldTrap.isEnabled() && emeraldTrap.isTrinket(itemStack)) {
                releaseEntity(emeraldTrap, itemStack, block, player, slot);
            } else if (amethystTrap.isEnabled() && amethystTrap.isTrinket(itemStack)) {
                releaseEntity(amethystTrap, itemStack, block, player, slot);
            } else if (netherStarTrap.isEnabled() && netherStarTrap.isTrinket(itemStack)) {
                releaseEntity(netherStarTrap, itemStack, block, player, slot);
            }
        }
    }

    private void releaseEntity(CrystalTrap trap, ItemStack itemStack, Block block, Player player, int slot) {
        long now = System.currentTimeMillis();
        if (now - cooldowns.getOrDefault(player, 0L) <= 1000)
            return;
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
        if (trap instanceof NetherStarTrap)
            Utils.transformItem(itemStack, netherStarTrap.emptyTrap(itemStack), slot, player.getInventory(), player);
        else
            itemStack.subtract();
        player.updateInventory();
        player.sendMessage(Component.text("Successfully released the " + Utils.getMobTypeAndName(entity) + ".",
                NamedTextColor.GOLD));
        plugin.getLogger().info(Utils.getMobTypeAndName(entity) + " released from a crystal trap at " +
                Utils.locationToString(entity.getLocation()) + " by player " + player.getName() + ".");
        cooldowns.put(player, now);
    }

    private boolean hasEnoughSpace(Location location) {
        if (!location.getBlock().isPassable())
            return false;
        Location above = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ());
        return above.getBlock().isPassable();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cooldowns.remove(event.getPlayer());
    }
}
