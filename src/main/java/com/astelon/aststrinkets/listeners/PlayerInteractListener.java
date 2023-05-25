package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.*;
import com.astelon.aststrinkets.trinkets.block.GatewayAnchor;
import com.astelon.aststrinkets.trinkets.creature.*;
import com.astelon.aststrinkets.trinkets.creature.traps.*;
import com.astelon.aststrinkets.trinkets.projectile.ExperienceBottle;
import com.astelon.aststrinkets.utils.CommandEvent;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerInteractListener implements Listener {

    private static final Pattern WORLD_PATTERN = Pattern.compile("<world:(.+)>");

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
    private final ExperienceBottle experienceBottle;
    private final Spellbook spellbook;
    private final GatewayAnchor gatewayAnchor;

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
        experienceBottle = trinketManager.getExperienceBottle();
        spellbook = trinketManager.getSpellbook();
        gatewayAnchor = trinketManager.getGatewayAnchor();
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        EquipmentSlot hand = event.getHand();
        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = hand == EquipmentSlot.HAND ? inventory.getItemInMainHand() : inventory.getItemInOffHand();
        Entity rightClicked = event.getRightClicked();
        if (!(rightClicked instanceof LivingEntity entity))
            return;
        int slot = hand == EquipmentSlot.HAND ? inventory.getHeldItemSlot() : Utils.OFF_HAND_SLOT;
        if (trinketManager.isOwnedBy(itemStack, player.getName())) {
            if (youthMilk.isEnabledTrinket(itemStack) && event.getRightClicked() instanceof Ageable ageable) {
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
                Utils.transformItem(itemStack, new ItemStack(Material.BUCKET), slot, inventory, player);
                player.updateInventory();
                plugin.getLogger().info("Youth milk used on " + mobInfoManager.getTypeAndName(ageable) + " at " +
                        Utils.locationToString(ageable.getLocation()) + " by player " + player.getName() + ".");
            } else if (diamondTrap.isEnabledTrinket(itemStack)) {
                trapEntity(diamondTrap, itemStack, entity, slot, inventory, player);
            } else if (emeraldTrap.isEnabledTrinket(itemStack)) {
                trapEntity(emeraldTrap, itemStack, entity, slot, inventory, player);
            } else if (amethystTrap.isEnabledTrinket(itemStack)) {
                trapEntity(amethystTrap, itemStack, entity, slot, inventory, player);
            } else if (netherStarTrap.isEnabledTrinket(itemStack)) {
                trapEntity(netherStarTrap, itemStack, entity, slot, inventory, player);
            } else if (lifeWater.isEnabledTrinket(itemStack)) {
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
                Utils.transformItem(itemStack, new ItemStack(Material.GLASS_BOTTLE), slot, inventory, player);
                player.updateInventory();
                plugin.getLogger().info("Life water used on " + mobInfoManager.getTypeAndName(mob) + " at " +
                        Utils.locationToString(mob.getLocation()) + " by player " + player.getName() + ".");
            } else if (spellbook.isEnabledTrinket(itemStack)) {
                event.setCancelled(true);
                HashMap<String, String> placeholders = new HashMap<>();
                placeholders.put("<playerName>", player.getName());
                placeholders.put("<playerCoords>", Utils.serializeCoordsCommand(player.getLocation()));
                placeholders.put("<targetCoords>", Utils.serializeCoordsCommand(entity.getLocation()));
                if (entity instanceof Player targetPlayer) {
                    placeholders.put("<targetPlayerName>", targetPlayer.getName());
                    useSpellbook(itemStack, player, placeholders, CommandEvent.INTERACT_PLAYER, slot);
                } else {
                    placeholders.put("<mobType>", entity.getType().name());
                    useSpellbook(itemStack, player, placeholders, CommandEvent.INTERACT_MOB, slot);
                }
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
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack itemStack = event.getItem();
            if (player.isSneaking() && itemStack != null) {
                if (trinketManager.isOwnedBy(itemStack, player.getName())) {
                    PlayerInventory inventory = player.getInventory();
                    int slot = event.getHand() == EquipmentSlot.HAND ? inventory.getHeldItemSlot() : Utils.OFF_HAND_SLOT;
                    if (experienceBottle.isEnabledTrinket(itemStack)) {
                        if (experienceBottle.hasExperience(itemStack))
                            return;
                        int experience = Utils.getTotalExperience(player);
                        if (experience == 0) {
                            player.sendMessage(Component.text("You don't have any experience to store in the bottle.",
                                    NamedTextColor.YELLOW));
                            return;
                        }
                        ItemStack result = experienceBottle.fillExperienceBottle(itemStack, experience);
                        Utils.transformItem(itemStack, result, slot, inventory, player);
                        player.updateInventory();
                        player.setTotalExperience(0);
                        player.setLevel(0);
                        player.setExp(0);
                        return;
                    }
                }
            }
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block == null)
                return;
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            // If the book is in main hand, no event is triggered for offhand like it happens with regular items
            ItemStack handItem = event.getHand() == EquipmentSlot.HAND ? inventory.getItemInMainHand() : inventory.getItemInOffHand();
            int slot = event.getHand() == EquipmentSlot.HAND ? inventory.getHeldItemSlot() : Utils.OFF_HAND_SLOT;
            if (spellbook.isEnabledTrinket(handItem)) {
                event.setUseItemInHand(Event.Result.DENY);
                HashMap<String, String> placeholders = new HashMap<>();
                placeholders.put("<playerName>", player.getName());
                placeholders.put("<playerCoords>", Utils.serializeCoordsCommand(player.getLocation()));
                placeholders.put("<blockType>", block.getType().name());
                placeholders.put("<targetCoords>", Utils.serializeCoordsCommand(block.getLocation()));
                useSpellbook(handItem, player, placeholders, CommandEvent.INTERACT_BLOCK, slot);
                //TODO exclude other trinkets possibly held in the other hand?
                return;
            }
            // Only checking for OFF_HAND because it is never used for specific interactions (open chests etc.), and it
            // is always called otherwise. It is used for placing blocks from offhand, though, so checking for that as well
            if (event.getHand() == EquipmentSlot.OFF_HAND && !event.isBlockInHand()) {
                ItemStack mainHandItem = inventory.getItemInMainHand();
                ItemStack offHandItem = inventory.getItemInOffHand();
                if (trinketManager.isTrinket(mainHandItem)) {
                    useTrinkets(mainHandItem, player, block, inventory.getHeldItemSlot());
                } else if (trinketManager.isTrinket(offHandItem)) {
                    useTrinkets(offHandItem, player, block, Utils.OFF_HAND_SLOT);
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
                PlayerInventory inventory = player.getInventory();
                int slot = event.getHand() == EquipmentSlot.HAND ? inventory.getHeldItemSlot() : Utils.OFF_HAND_SLOT;
                if (shulkerBoxContainmentUnit.isEnabledTrinket(itemStack)) {
                    if (!shulkerBoxContainmentUnit.hasShulkerBox(itemStack))
                        return;
                    ItemStack result = shulkerBoxContainmentUnit.getContainedShulkerBox(itemStack);
                    if (result == null) {
                        player.sendMessage(Component.text("This containment unit has become corrupted. The " +
                                "shulker box within could not be retrieved.", NamedTextColor.RED));
                        return;
                    }
                    Utils.transformItem(itemStack, result, slot, inventory, player);
                    player.updateInventory();
                } else if (spellbook.isEnabledTrinket(itemStack)) {
                    event.setUseItemInHand(Event.Result.DENY);
                    HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("<playerName>", player.getName());
                    placeholders.put("<playerCoords>", Utils.serializeCoordsCommand(player.getLocation()));
                    if (slot == Utils.OFF_HAND_SLOT)
                        placeholders.put("<otherHandItemType>", player.getInventory().getItemInMainHand().getType().name());
                    else
                        placeholders.put("<otherHandItemType>", player.getInventory().getItemInOffHand().getType().name());
                    useSpellbook(itemStack, player, placeholders, null, slot);
                } else if (gatewayAnchor.isEnabledTrinket(itemStack)) {
                    Location location = player.getLocation();
                    ItemStack result = gatewayAnchor.setLocation(itemStack, location);
                    Utils.transformItem(itemStack, result, slot, inventory, player);
                    player.updateInventory();
                }
            }
        }
    }

    private void useTrinkets(ItemStack itemStack, Player player, Block block, int slot) {
        if (itemStack != null && trinketManager.isOwnedBy(itemStack, player.getName())) {
            if (diamondTrap.isEnabledTrinket(itemStack)) {
                releaseEntity(diamondTrap, itemStack, block, player, slot);
            } else if (emeraldTrap.isEnabledTrinket(itemStack)) {
                releaseEntity(emeraldTrap, itemStack, block, player, slot);
            } else if (amethystTrap.isEnabledTrinket(itemStack)) {
                releaseEntity(amethystTrap, itemStack, block, player, slot);
            } else if (netherStarTrap.isEnabledTrinket(itemStack)) {
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

    private void useSpellbook(ItemStack itemStack, Player player, Map<String, String> placeholders, CommandEvent actionEvent,
                              int slot) {
        if (spellbook.isEmpty(itemStack))
            return;
        String[] commands = spellbook.getCommands(itemStack);
        if (commands == null || commands.length == 0) {
            player.sendMessage(Component.text("This spellbook has become corrupted, and cannot be " +
                    "used anymore.", NamedTextColor.RED));
            return;
        }
        if (!spellbook.canUse(itemStack)) {
            player.sendMessage(Component.text("You can't use this spellbook yet.", NamedTextColor.YELLOW));
            return;
        }
        if (!spellbook.isFunctional(itemStack)) {
            player.sendMessage(Component.text("This spellbook copy cannot be used.", NamedTextColor.RED));
            return;
        }
        Entity targetEntity = null;
        Block targetBlock = null;
        boolean targetEntityComputed = false;
        boolean targetBlockComputed = false;
        boolean used = false;
        for (String command: commands) {
            String[] components = command.split("/", 2);
            String runConfig = components[0];
            String toExecute = components[1];
            CommandEvent neededEvent = null;
            if (!runConfig.isEmpty())
                neededEvent = CommandEvent.getCommandEvent(runConfig);
            if (!CommandEvent.matches(neededEvent, actionEvent))
                continue;

            if (neededEvent == CommandEvent.TARGET_MOB) {
                if (!targetEntityComputed) {
                    targetEntity = player.getTargetEntity(120);
                    targetEntityComputed = true;
                }
                if (!(targetEntity instanceof LivingEntity targetMob))
                    continue;
                placeholders.put("<mobType>", targetMob.getType().name());
                placeholders.put("<targetCoords>", Utils.serializeCoordsCommand(targetMob.getLocation()));
            } else if (neededEvent == CommandEvent.TARGET_PLAYER) {
                if (!targetEntityComputed) {
                    targetEntity = player.getTargetEntity(120);
                    targetEntityComputed = true;
                }
                if (!(targetEntity instanceof Player targetPlayer))
                    continue;
                placeholders.put("<targetPlayerName>", targetPlayer.getName());
                placeholders.put("<targetCoords>", Utils.serializeCoordsCommand(targetPlayer.getLocation()));
            } else if (neededEvent == CommandEvent.TARGET_BLOCK) {
                if (!targetBlockComputed) {
                    targetBlock = player.getTargetBlockExact(400);
                    targetBlockComputed = true;
                }
                if (targetBlock == null)
                    continue;
                placeholders.put("<blockType>", targetBlock.getType().name());
                placeholders.put("<targetCoords>", Utils.serializeCoordsCommand(targetBlock.getLocation()));
            }
            boolean playerRun = runConfig.contains("playerRun");
            boolean otherHandEmpty = Material.AIR.name().equals(placeholders.get("<otherHandItemType>"));
            if (runConfig.contains("otherHandItem") && otherHandEmpty)
                continue;
            if (runConfig.contains("otherHandEmpty") && !otherHandEmpty)
                continue;
            for (Map.Entry<String, String> entry: placeholders.entrySet()) {
                toExecute = toExecute.replace(entry.getKey(), entry.getValue());
            }
            Matcher matcher = WORLD_PATTERN.matcher(runConfig);
            if (matcher.find()) {
                String worldName = matcher.group(1);
                if (worldName != null && !player.getWorld().getName().equals(worldName))
                    continue;
            }
            try {
                CommandSender sender = playerRun ? player : Bukkit.getConsoleSender();
                plugin.getLogger().info("Player " + player.getName() + " used a spellbook that ran the command: /" + toExecute);
                Bukkit.dispatchCommand(sender, toExecute);
                used = true;
            } catch (CommandException ignore) {}
        }
        if (used) {
            ItemStack result = spellbook.use(itemStack);
            if (result == null)
                itemStack.subtract();
            else
                Utils.transformItem(itemStack, result, slot, player.getInventory(), player);
            player.updateInventory();

        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cooldowns.remove(event.getPlayer());
    }
}
