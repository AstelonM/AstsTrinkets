package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.*;
import com.astelon.aststrinkets.trinkets.creature.*;
import com.astelon.aststrinkets.trinkets.creature.traps.*;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
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
    private final MobInfoManager mobInfoManager;
    private final TrinketManager trinketManager;
    private final HashMap<Player, Long> cooldowns;
    private final YouthMilk youthMilk;
    private final DiamondTrap diamondTrap;
    private final EmeraldTrap emeraldTrap;
    private final AmethystTrap amethystTrap;
    private final NetherStarTrap netherStarTrap;
    private final ShulkerBoxContainmentUnit shulkerBoxContainmentUnit;
    private final LifeWater lifeWater;

    public PlayerInteractListener(AstsTrinkets plugin, MobInfoManager mobInfoManager, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.mobInfoManager = mobInfoManager;
        this.trinketManager = trinketManager;
        cooldowns = new HashMap<>();
        youthMilk = trinketManager.getYouthMilk();
        diamondTrap = trinketManager.getDiamondTrap();
        emeraldTrap = trinketManager.getEmeraldTrap();
        amethystTrap = trinketManager.getAmethystTrap();
        netherStarTrap = trinketManager.getNetherStarTrap();
        shulkerBoxContainmentUnit = trinketManager.getShulkerBoxContainmentUnit();
        lifeWater = trinketManager.getLifeWater();
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        EquipmentSlot hand = event.getHand();
        PlayerInventory inventory = player.getInventory();
        ItemStack item = hand == EquipmentSlot.HAND ? inventory.getItemInMainHand() : inventory.getItemInOffHand();
        Entity rightClicked = event.getRightClicked();
        if (!(rightClicked instanceof LivingEntity entity))
            return;
        int slot = hand == EquipmentSlot.HAND ? inventory.getHeldItemSlot() : Utils.OFF_HAND_SLOT;
        if (trinketManager.isOwnedBy(item, player.getName())) {
            if (youthMilk.isEnabled() && youthMilk.isTrinket(item) && event.getRightClicked() instanceof Ageable ageable) {
                if (!ageable.isAdult())
                    return;
                if (youthMilk.petOwnedByOtherPlayer(ageable, player)) {
                    player.sendMessage(Component.text("You can't use this on someone else's pet.", NamedTextColor.RED));
                    return;
                }
                if (youthMilk.isInvulnerableToPlayer(ageable, player)) {
                    player.sendMessage(Component.text("This creature is too strong to be affected.", NamedTextColor.RED));
                    return;
                }
                event.setCancelled(true);
                //TODO check ageLock?
                ageable.setBaby();
                Utils.transformItem(item, new ItemStack(Material.BUCKET), slot, inventory, player);
                player.updateInventory();
                plugin.getLogger().info("Youth milk used on " + mobInfoManager.getTypeAndName(ageable) + " at " +
                        Utils.locationToString(ageable.getLocation()) + " by player " + player.getName() + ".");
            } else if (diamondTrap.isEnabled() && diamondTrap.isTrinket(item)) {
                trapEntity(diamondTrap, item, entity, slot, inventory, player);
            } else if (emeraldTrap.isEnabled() && emeraldTrap.isTrinket(item)) {
                trapEntity(emeraldTrap, item, entity, slot, inventory, player);
            } else if (amethystTrap.isEnabled() && amethystTrap.isTrinket(item)) {
                trapEntity(amethystTrap, item, entity, slot, inventory, player);
            } else if (netherStarTrap.isEnabled() && netherStarTrap.isTrinket(item)) {
                trapEntity(netherStarTrap, item, entity, slot, inventory, player);
            } else if (lifeWater.isEnabled() && lifeWater.isTrinket(item)) {
                if (!(entity instanceof Mob mob))
                    return;
                if (mob.isInvulnerable())
                    return;
                if (lifeWater.petOwnedByOtherPlayer(mob, player)) {
                    player.sendMessage(Component.text("You can't use this on someone else's pet.", NamedTextColor.RED));
                    return;
                }
                event.setCancelled(true);
                lifeWater.makeInvulnerable(mob, player);
                Utils.transformItem(item, new ItemStack(Material.GLASS_BOTTLE), slot, inventory, player);
                player.updateInventory();
                plugin.getLogger().info("Life water used on " + mobInfoManager.getTypeAndName(mob) + " at " +
                        Utils.locationToString(mob.getLocation()) + " by player " + player.getName() + ".");
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
        if (trap.petOwnedByOtherPlayer(entity, player)) {
            player.sendMessage(Component.text("You can't trap someone else's pet.", NamedTextColor.RED));
            return;
        }
        if (trap.isInvulnerableToPlayer(entity, player)) {
            player.sendMessage(Component.text("This creature is too strong to be trapped.", NamedTextColor.RED));
            return;
        }
        ItemStack result = trap.trapCreature(item, entity);
        if (result == null)
            return;
        entity.remove();
        Utils.transformItem(item, result, slot, inventory, player);
        player.updateInventory();
        String mobName = mobInfoManager.getTypeAndName(entity);
        player.sendMessage(Component.text("You caught the " + mobName + " in a crystal trap.", NamedTextColor.GOLD));
        plugin.getLogger().info(mobName + " trapped in a crystal trap at " +
                Utils.locationToString(entity.getLocation()) + " by player " + player.getName() + ".");
        cooldowns.put(player, now);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
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
                } else if (trinketManager.isTrinket(offHandItem)) {
                    useTrinkets(mainHandItem, player, block, Utils.OFF_HAND_SLOT);
                }
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();
            if (!player.isSneaking())
                return;
            ItemStack itemStack = event.getItem();
            if (itemStack == null)
                return;
            if (trinketManager.isOwnedBy(itemStack, player.getName())) {
                if (shulkerBoxContainmentUnit.isEnabled() && shulkerBoxContainmentUnit.isTrinket(itemStack)) {
                    if (!shulkerBoxContainmentUnit.hasShulkerBox(itemStack))
                        return;
                    ItemStack result = shulkerBoxContainmentUnit.getContainedShulkerBox(itemStack);
                    if (result == null) {
                        player.sendMessage(Component.text("This containment unit has become corrupted. The " +
                                "shulker box within could not be retrieved.", NamedTextColor.RED));
                        return;
                    }
                    PlayerInventory inventory = player.getInventory();
                    int slot = event.getHand() == EquipmentSlot.HAND ? inventory.getHeldItemSlot() : Utils.OFF_HAND_SLOT;
                    Utils.transformItem(itemStack, result, slot, inventory, player);
                    player.updateInventory();
                }
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
        Entity entity = trap.getTrappedCreature(itemStack, spawnLocation.getWorld());
        if (entity == null) {
            player.sendMessage(Component.text("The creature could not be released. The trap might be corrupted.",
                    NamedTextColor.RED));
            return;
        }
        if (!entity.spawnAt(spawnLocation)) {
            player.sendMessage(Component.text("You can't release this creature here. Something stopped it from " +
                    "spawning.", NamedTextColor.RED));
            return;
        }
        if (trap instanceof NetherStarTrap)
            Utils.transformItem(itemStack, netherStarTrap.emptyTrap(itemStack), slot, player.getInventory(), player);
        else
            itemStack.subtract();
        player.updateInventory();
        String mobName = mobInfoManager.getTypeAndName(entity);
        player.sendMessage(Component.text("Successfully released the " + mobName + ".", NamedTextColor.GOLD));
        plugin.getLogger().info(mobName + " released from a crystal trap at " +
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
