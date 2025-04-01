package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.*;
import com.astelon.aststrinkets.trinkets.block.GatewayAnchor;
import com.astelon.aststrinkets.trinkets.block.Terrarium;
import com.astelon.aststrinkets.trinkets.creature.*;
import com.astelon.aststrinkets.trinkets.creature.traps.*;
import com.astelon.aststrinkets.trinkets.InvisibilityPowder;
import com.astelon.aststrinkets.trinkets.projectile.ExperienceBottle;
import com.astelon.aststrinkets.utils.CommandEvent;
import com.astelon.aststrinkets.utils.Utils;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.astelon.aststrinkets.utils.Utils.isNothing;

public class PlayerInteractListener implements Listener {

    private static final Pattern WORLD_PATTERN = Pattern.compile("<world:(.+)>");
    private static final Pattern X_COORD_PATTERN = Pattern.compile("<x:([0-9-]+)>");
    private static final Pattern Y_COORD_PATTERN = Pattern.compile("<y:([0-9-]+)>");
    private static final Pattern Z_COORD_PATTERN = Pattern.compile("<z:([0-9-]+)>");
    private static final Pattern TARGET_X_COORD_PATTERN = Pattern.compile("<targetX:([0-9-]+)>");
    private static final Pattern TARGET_Y_COORD_PATTERN = Pattern.compile("<targetY:([0-9-]+)>");
    private static final Pattern TARGET_Z_COORD_PATTERN = Pattern.compile("<targetZ:([0-9-]+)>");

    private final AstsTrinkets plugin;
    private final MobInfoManager mobInfoManager;
    private final TrinketManager trinketManager;

    private final HashMap<Player, Long> cooldowns;
    private final HashMap<Player, Long> terrariumLockAttempts;

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
    private final ItemMagnet itemMagnet;
    private final Terrarium terrarium;
    private final TimeMachinePrototype timeMachinePrototype;
    private final TrinketImmunitySponge trinketImmunitySponge;
    private final TrinketVulnerabilitySponge trinketVulnerabilitySponge;
    private final PlayerMagnet playerMagnet;
    private final HealingHerb healingHerb;
    private final MysteryShell mysteryShell;
    private final AbyssShell abyssShell;
    private final SurfaceCure surfaceCure;
    private final InvisibilityPowder invisibilityPowder;
    private final SnowGolemBlueprint snowGolemBlueprint;
    private final TaintedLifeWater taintedLifeWater;
    private final SpoiledYouthMilk spoiledYouthMilk;
    private final EternalYouthCookie eternalYouthCookie;
    private final InfestedWheat infestedWheat;
    private final Treats treats;
    private final MagicBerries magicBerries;

    public PlayerInteractListener(AstsTrinkets plugin, MobInfoManager mobInfoManager, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.mobInfoManager = mobInfoManager;
        this.trinketManager = trinketManager;
        cooldowns = new HashMap<>();
        terrariumLockAttempts = new HashMap<>();
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
        itemMagnet = trinketManager.getItemMagnet();
        terrarium = trinketManager.getTerrarium();
        timeMachinePrototype = trinketManager.getTimeMachinePrototype();
        trinketImmunitySponge = trinketManager.getTrinketImmunitySponge();
        trinketVulnerabilitySponge = trinketManager.getTrinketVulnerabilitySponge();
        playerMagnet = trinketManager.getPlayerMagnet();
        healingHerb = trinketManager.getHealingHerb();
        mysteryShell = trinketManager.getMysteryShell();
        abyssShell = trinketManager.getAbyssShell();
        surfaceCure = trinketManager.getSurfaceCure();
        invisibilityPowder = trinketManager.getInvisibilityPowder();
        snowGolemBlueprint = trinketManager.getSnowGolemBlueprint();
        taintedLifeWater = trinketManager.getTaintedLifeWater();
        spoiledYouthMilk = trinketManager.getSpoiledYouthMilk();
        eternalYouthCookie = trinketManager.getEternalYouthCookie();
        infestedWheat = trinketManager.getInfestedWheat();
        treats = trinketManager.getTreats();
        magicBerries = trinketManager.getMagicBerries();
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
            if (youthMilk.isEnabledTrinket(itemStack) && entity instanceof Ageable ageable) {
                if (!ageable.isAdult())
                    return;
                if (trinketManager.isTrinketImmune(ageable)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this entity.", NamedTextColor.RED));
                    return;
                }
                if (youthMilk.petOwnedByOtherPlayer(ageable, player)) {
                    player.sendMessage(Component.text("You can't use this on someone else's pet.", NamedTextColor.RED));
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
                if (trinketManager.isTrinketImmune(mob)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this entity.", NamedTextColor.RED));
                    return;
                }
                if (lifeWater.petOwnedByOtherPlayer(mob, player)) {
                    player.sendMessage(Component.text("You can't use this on someone else's pet.", NamedTextColor.RED));
                    return;
                }
                event.setCancelled(true);
                lifeWater.makeInvulnerable(mob);
                Utils.transformItem(itemStack, new ItemStack(Material.GLASS_BOTTLE), slot, inventory, player);
                player.updateInventory();
                plugin.getLogger().info("Life water used on " + mobInfoManager.getTypeAndName(mob) + " at " +
                        Utils.locationToString(mob.getLocation()) + " by player " + player.getName() + ".");
            } else if (spellbook.isEnabledTrinket(itemStack)) {
                if (!player.isSneaking())
                    return;
                event.setCancelled(true);
                HashMap<String, String> placeholders = new HashMap<>();
                placeholders.put("<playerName>", player.getName());
                placeholders.put("<playerCoords>", Utils.serializeCoordsCommand(player.getLocation()));
                placeholders.put("<targetCoords>", Utils.serializeCoordsCommand(entity.getLocation()));
                placeholders.put("<playerFacing>", player.getFacing().name().toLowerCase());
                if (slot == Utils.OFF_HAND_SLOT)
                    placeholders.put("<otherHandItemType>", player.getInventory().getItemInMainHand().getType().name());
                else
                    placeholders.put("<otherHandItemType>", player.getInventory().getItemInOffHand().getType().name());
                if (entity instanceof Player targetPlayer) {
                    placeholders.put("<targetPlayerName>", targetPlayer.getName());
                    useSpellbook(itemStack, player, placeholders, CommandEvent.INTERACT_PLAYER, slot, targetPlayer.getLocation());
                } else {
                    placeholders.put("<mobType>", entity.getType().name());
                    useSpellbook(itemStack, player, placeholders, CommandEvent.INTERACT_MOB, slot, entity.getLocation());
                }
            } else if (terrarium.isEnabledTrinket(itemStack)) {
                if (!terrarium.canUse(itemStack))
                    return;
                if (terrarium.hasTrappedCreature(itemStack)) {
                    player.sendMessage(Component.text("This terrarium already has a creature inside.", NamedTextColor.YELLOW));
                    return;
                }
                if (trinketManager.isTrinketImmune(entity)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this entity.", NamedTextColor.RED));
                    return;
                }
                if (!terrarium.canTrap(entity)) {
                    player.sendMessage(Component.text("This creature cannot be trapped in the terrarium.", NamedTextColor.RED));
                    return;
                }
                if (terrarium.petOwnedByOtherPlayer(entity, player)) {
                    player.sendMessage(Component.text("You can't trap someone else's pet.", NamedTextColor.RED));
                    return;
                }
                Entity vehicle = entity.getVehicle();
                if (vehicle != null) {
                    vehicle.removePassenger(entity);
                }
                ItemStack result = terrarium.trapCreature(itemStack, entity);
                if (result == null)
                    return;
                entity.remove();
                Utils.transformItem(itemStack, result, slot, inventory, player);
                player.updateInventory();
                String mobName = mobInfoManager.getTypeAndName(entity);
                player.sendMessage(Component.text("You trapped the " + mobName + " in the terrarium.", NamedTextColor.GOLD));
                plugin.getLogger().info(mobName + " trapped in a Terrarium at " +
                        Utils.locationToString(entity.getLocation()) + " by player " + player.getName() + ".");
            } else if (trinketImmunitySponge.isEnabledTrinket(itemStack)) {
                if (trinketManager.isTrinketImmune(entity)) {
                    player.sendMessage(Component.text("This entity is already trinket immune.", NamedTextColor.RED));
                    return;
                }
                trinketManager.makeTrinketImmune(entity);
                player.sendMessage(Component.text("This entity is now trinket immune."));
            } else if (trinketVulnerabilitySponge.isEnabledTrinket(itemStack)) {
                if (!trinketManager.isTrinketImmune(entity)) {
                    player.sendMessage(Component.text("This entity is not trinket immune.", NamedTextColor.RED));
                    return;
                }
                trinketManager.removeTrinketImmunity(entity);
                player.sendMessage(Component.text("This entity is no longer trinket immune."));
            } else if (healingHerb.isEnabledTrinket(itemStack)) {
                if (trinketManager.isTrinketImmune(entity)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this entity.", NamedTextColor.RED));
                    return;
                }
                restoreHealth(entity, itemStack, player);
            } else if (surfaceCure.isEnabledTrinket(itemStack)) {
                if (!(entity instanceof PiglinAbstract || entity instanceof Hoglin))
                    return;
                if (isImmuneToZombification(entity))
                    return;
                if (trinketManager.isTrinketImmune(entity)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this entity.", NamedTextColor.RED));
                    return;
                }
                event.setCancelled(true);
                surfaceCure.makeImmuneToZombification(entity);
                Utils.transformItem(itemStack, new ItemStack(Material.GLASS_BOTTLE), slot, inventory, player);
                player.updateInventory();
                plugin.getLogger().info("Surface cure used on " + mobInfoManager.getTypeAndName(entity) + " at " +
                        Utils.locationToString(entity.getLocation()) + " by player " + player.getName() + ".");
            } else if (taintedLifeWater.isEnabledTrinket(itemStack)) {
                if (!(entity instanceof Mob mob))
                    return;
                if (!mob.isInvulnerable())
                    return;
                if (trinketManager.isTrinketImmune(mob)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this entity.", NamedTextColor.RED));
                    return;
                }
                if (taintedLifeWater.petOwnedByOtherPlayer(mob, player)) {
                    player.sendMessage(Component.text("You can't use this on someone else's pet.", NamedTextColor.RED));
                    return;
                }
                event.setCancelled(true);
                taintedLifeWater.removeInvulnerability(mob);
                Utils.transformItem(itemStack, new ItemStack(Material.GLASS_BOTTLE), slot, inventory, player);
                player.updateInventory();
                plugin.getLogger().info("Tainted life water used on " + mobInfoManager.getTypeAndName(mob) + " at " +
                        Utils.locationToString(mob.getLocation()) + " by player " + player.getName() + ".");
            } else if (spoiledYouthMilk.isEnabledTrinket(itemStack) && entity instanceof Ageable ageable) {
                if (ageable.isAdult())
                    return;
                if (trinketManager.isTrinketImmune(ageable)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this entity.", NamedTextColor.RED));
                    return;
                }
                if (spoiledYouthMilk.petOwnedByOtherPlayer(ageable, player)) {
                    player.sendMessage(Component.text("You can't use this on someone else's pet.", NamedTextColor.RED));
                    return;
                }
                event.setCancelled(true);
                ageable.setAdult();
                if (ageable instanceof Breedable breedable)
                    breedable.setAgeLock(false);
                Utils.transformItem(itemStack, new ItemStack(Material.BUCKET), slot, inventory, player);
                player.updateInventory();
                plugin.getLogger().info("Spoiled youth milk used on " + mobInfoManager.getTypeAndName(ageable) + " at " +
                        Utils.locationToString(ageable.getLocation()) + " by player " + player.getName() + ".");
            } else if (eternalYouthCookie.isEnabledTrinket(itemStack) && entity instanceof Ageable ageable) {
                if (trinketManager.isTrinketImmune(ageable)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this entity.", NamedTextColor.RED));
                    return;
                }
                if (eternalYouthCookie.petOwnedByOtherPlayer(ageable, player)) {
                    player.sendMessage(Component.text("You can't use this on someone else's pet.", NamedTextColor.RED));
                    return;
                }
                event.setCancelled(true);
                ageable.setBaby();
                if (ageable instanceof Breedable breedable)
                    breedable.setAgeLock(true);
                itemStack.subtract();
                player.updateInventory();
                plugin.getLogger().info("Eternal youth cookie used on " + mobInfoManager.getTypeAndName(ageable) + " at " +
                        Utils.locationToString(ageable.getLocation()) + " by player " + player.getName() + ".");
            } else if (infestedWheat.isEnabledTrinket(itemStack) && entity instanceof Cow cow) {
                event.setCancelled(true);
                if (trinketManager.isTrinketImmune(cow)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this entity.", NamedTextColor.RED));
                    return;
                }
                Location location = cow.getLocation();
                MushroomCow mooshroom = location.getWorld().spawn(location, MushroomCow.class);
                if (mooshroom.isValid()) {
                    Utils.copyCommonEntityAttributes(cow, mooshroom);
                    cow.remove();
                    itemStack.subtract();
                } else {
                    player.sendMessage(Component.text("Could not transform this creature.", NamedTextColor.RED));
                }
            } else if (treats.isEnabledTrinket(itemStack)) {
                event.setCancelled(true);
                if (trinketManager.isTrinketImmune(entity)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this entity.", NamedTextColor.RED));
                    return;
                }
                if (entity instanceof Tameable tameable) {
                    if (tameable.getOwnerUniqueId() != null) {
                        player.sendMessage(Component.text("This creature is already tamed.", NamedTextColor.RED));
                        return;
                    }
                    tameable.setOwner(player);
                    itemStack.subtract();
                    player.getWorld().spawnParticle(Particle.HEART, tameable.getLocation(), 3);
                } else if (entity instanceof Fox fox) {
                    if (fox.getFirstTrustedPlayer() == null) {
                        fox.setFirstTrustedPlayer(player);
                        itemStack.subtract();
                        player.getWorld().spawnParticle(Particle.HEART, fox.getLocation(), 3);
                    } else {
                        if (fox.getSecondTrustedPlayer() == null) {
                            fox.setSecondTrustedPlayer(player);
                            itemStack.subtract();
                            player.getWorld().spawnParticle(Particle.HEART, fox.getLocation(), 3);
                        } else {
                            player.sendMessage(Component.text("This fox already trusts others.", NamedTextColor.RED));
                        }
                    }
                } else if (entity instanceof Ocelot ocelot) {
                    if (ocelot.isTrusting()) {
                        player.sendMessage(Component.text("This ocelot is already trusting.", NamedTextColor.RED));
                        return;
                    }
                    ocelot.setTrusting(true);
                    itemStack.subtract();
                    player.getWorld().spawnParticle(Particle.HEART, ocelot.getLocation(), 3);
                } else {
                    player.sendMessage(Component.text("This creature cannot be tamed.", NamedTextColor.RED));
                }
            } else if (magicBerries.isEnabledTrinket(itemStack)) {
                event.setCancelled(true);
                if (trinketManager.isTrinketImmune(entity)) {
                    player.sendMessage(Component.text("Trinkets cannot be used on this entity.", NamedTextColor.RED));
                    return;
                }
                if (magicBerries.petOwnedByOtherPlayer(entity, player)) {
                    player.sendMessage(Component.text("You can't use this on someone else's pet.", NamedTextColor.RED));
                    return;
                }
                String originalName = mobInfoManager.getTypeAndName(entity);
                if (magicBerries.transformCreature(entity)) {
                    itemStack.subtract();
                    plugin.getLogger().info("Player " + player.getName() + " used Magic Berries on " + originalName +
                            " at " + Utils.serializeCoordsLogging(entity.getLocation()));
                } else {
                    player.sendMessage(Component.text("This creature cannot be transformed.", NamedTextColor.RED));
                }
            }
        }
    }

    private boolean isImmuneToZombification(LivingEntity entity) {
        if (entity instanceof PiglinAbstract piglin)
            return piglin.isImmuneToZombification();
        else if (entity instanceof Hoglin hoglin)
            return hoglin.isImmuneToZombification();
        return true;
    }

    private void trapEntity(CrystalTrap trap, ItemStack item, Entity entity, int slot, Inventory inventory, Player player) {
        long now = System.currentTimeMillis();
        if (now - cooldowns.getOrDefault(player, 0L) <= 1000)
            return;
        if (trap.hasTrappedCreature(item)) {
            player.sendMessage(Component.text("This crystal trap already has a creature inside.", NamedTextColor.YELLOW));
            return;
        }
        if (trinketManager.isTrinketImmune(entity)) {
            player.sendMessage(Component.text("Trinkets cannot be used on this entity.", NamedTextColor.RED));
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
        Entity vehicle = entity.getVehicle();
        if (vehicle != null) {
            vehicle.removePassenger(entity);
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
            if (!isNothing(itemStack) && trinketManager.isOwnedBy(itemStack, player)) {
                if (player.isSneaking()) {
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
                    } else if (itemMagnet.isEnabledTrinket(itemStack)) {
                        if (!itemMagnet.canUse(itemStack)) {
                            player.sendMessage(Component.text("You can't use this trinket yet.", NamedTextColor.RED));
                            return;
                        }
                        int range = itemMagnet.getRange(itemStack);
                        List<Entity> entities = player.getNearbyEntities(range, range, range);
                        entities.stream().filter(entity -> entity instanceof Item)
                                .forEach(item -> item.teleport(player));
                        itemMagnet.use(itemStack);
                    } else if (timeMachinePrototype.isEnabledTrinket(itemStack)) {
                        long now = System.currentTimeMillis();
                        if (now - cooldowns.getOrDefault(player, 0L) <= 1000)
                            return;
                        if (timeMachinePrototype.hasLocation(itemStack)) {
                            Location location = timeMachinePrototype.getLocation(itemStack);
                            if (location == null) {
                                player.sendMessage(Component.text("This time machine prototype is broken.", NamedTextColor.RED));
                                return;
                            }
                            World world = player.getWorld();
                            if (!world.equals(location.getWorld())) {
                                player.sendMessage(Component.text("This time machine prototype is linked to a location " +
                                        "in a different world.", NamedTextColor.RED));
                                return;
                            }
                            Location playerLocation = player.getLocation();
                            boolean dropped = false;
                            for (int i = 0; i < inventory.getSize(); i++) {
                                ItemStack inventoryItem = inventory.getItem(i);
                                if (inventoryItem != null && !timeMachinePrototype.isTrinket(inventoryItem)) {
                                    world.dropItem(playerLocation, inventoryItem);
                                    inventory.setItem(i, null);
                                    dropped = true;
                                }
                            }
                            player.teleport(location);
                            if (dropped)
                                player.sendMessage(Component.text("The items you had in your inventory had been " +
                                        "dropped when you used the time machine.", NamedTextColor.YELLOW));
                            itemStack.subtract();
                            player.updateInventory();
                            cooldowns.put(player, now);
                        } else {
                            Location location = player.getLocation();
                            ItemStack result = timeMachinePrototype.setLocation(itemStack, location);
                            Utils.transformItem(itemStack, result, slot, inventory, player);
                            player.updateInventory();
                            cooldowns.put(player, now);
                        }
                    } else if (playerMagnet.isEnabledTrinket(itemStack)) {
                        if (!playerMagnet.canUse(itemStack)) {
                            player.sendMessage(Component.text("You can't use this trinket yet.", NamedTextColor.RED));
                            return;
                        }
                        int range = playerMagnet.getRange(itemStack);
                        Collection<Player> players = player.getWorld().getNearbyPlayers(player.getLocation(), range);
                        players.stream().filter(nearbyPlayer -> !players.equals(nearbyPlayer))
                                .forEach(nearbyPlayer -> nearbyPlayer.teleport(player));
                        playerMagnet.use(itemStack);
                    } else if (mysteryShell.isEnabledTrinket(itemStack)) {
                        long now = System.currentTimeMillis();
                        if (now - cooldowns.getOrDefault(player, 0L) <= 1000)
                            return;
                        Location originalLocation = player.getLocation();
                        Location result = mysteryShell.getRandomLocation(itemStack, player.getWorld());
                        itemStack.subtract();
                        player.updateInventory();
                        if (result == null) {
                            player.sendMessage(Component.text("The shell crumbles into dust, yet nothing happens.",
                                    NamedTextColor.YELLOW));
                            plugin.getLogger().warning("Player " + player.getName() + " used a Mystery Shell at " +
                                    Utils.serializeCoordsLogging(originalLocation) + " but " +
                                    "could not find a location.");
                        } else {
                            if (player.teleport(result)) {
                                player.sendMessage(Component.text("The shell crumbles into dust.", NamedTextColor.GREEN));
                                plugin.getLogger().info("Player " + player.getName() + " used a Mystery Shell and was " +
                                        "teleported from " + Utils.serializeCoordsLogging(originalLocation) + " to " +
                                        Utils.serializeCoordsLogging(result) + ".");
                            } else {
                                player.sendMessage(Component.text("The shell crumbles into dust, yet nothing happens.",
                                        NamedTextColor.YELLOW));
                                plugin.getLogger().warning("Player " + player.getName() + " used a Mystery Shell at " +
                                        Utils.serializeCoordsLogging(originalLocation) + " but could not be teleported to " +
                                        Utils.serializeCoordsLogging(result) + ".");
                            }
                        }
                        cooldowns.put(player, now);
                    } else if (abyssShell.isEnabledTrinket(itemStack)) {
                        long now = System.currentTimeMillis();
                        if (now - cooldowns.getOrDefault(player, 0L) <= 1000)
                            return;
                        Location originalLocation = player.getLocation();
                        Location result = abyssShell.getRandomLocation(itemStack, player.getWorld());
                        if (!trinketManager.isStrictlyOwnedBy(itemStack, player)) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 240, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
                        }
                        if (result == null) {
                            player.sendMessage(Component.text("You stare into the shell, yet nothing happens.",
                                    NamedTextColor.YELLOW));
                            plugin.getLogger().warning("Player " + player.getName() + " used an Abyss Shell at " +
                                    Utils.serializeCoordsLogging(originalLocation) + " but " +
                                    "could not find a location.");
                        } else {
                            if (player.teleport(result)) {
                                player.sendMessage(Component.text("You stare into the shell.", NamedTextColor.GREEN));
                                plugin.getLogger().info("Player " + player.getName() + " used an Abyss Shell and was " +
                                        "teleported from " + Utils.serializeCoordsLogging(originalLocation) + " to " +
                                        Utils.serializeCoordsLogging(result) + ".");
                            } else {
                                player.sendMessage(Component.text("You stare into the shell, yet nothing happens.",
                                        NamedTextColor.YELLOW));
                                plugin.getLogger().warning("Player " + player.getName() + " used an Abyss Shell at " +
                                        Utils.serializeCoordsLogging(originalLocation) + " but could not be teleported to " +
                                        Utils.serializeCoordsLogging(result) + ".");
                            }
                        }
                        cooldowns.put(player, now);
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
            if (spellbook.isEnabledTrinket(handItem) && trinketManager.isOwnedBy(handItem, player)) {
                if (!player.isSneaking())
                    return;
                event.setUseItemInHand(Event.Result.DENY);
                HashMap<String, String> placeholders = new HashMap<>();
                placeholders.put("<playerName>", player.getName());
                placeholders.put("<playerCoords>", Utils.serializeCoordsCommand(player.getLocation()));
                placeholders.put("<blockType>", block.getType().name());
                placeholders.put("<targetCoords>", Utils.serializeCoordsCommand(block.getLocation()));
                placeholders.put("<playerFacing>", player.getFacing().name().toLowerCase());
                if (slot == Utils.OFF_HAND_SLOT)
                    placeholders.put("<otherHandItemType>", player.getInventory().getItemInMainHand().getType().name());
                else
                    placeholders.put("<otherHandItemType>", player.getInventory().getItemInOffHand().getType().name());
                useSpellbook(handItem, player, placeholders, CommandEvent.INTERACT_BLOCK, slot, block.getLocation());
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
            if (event.isBlockInHand() && player.isSneaking()) {
                if (terrarium.isEnabledTrinket(handItem) && trinketManager.isOwnedBy(handItem, player)) {
                    event.setUseItemInHand(Event.Result.DENY);
                    if (!terrarium.canUse(handItem))
                        return;
                    if (!terrarium.hasTrappedCreature(handItem))
                        return;
                    Location originalLocation = block.getLocation();
                    Location spawnLocation = new Location(originalLocation.getWorld(), originalLocation.getBlockX() + 0.5,
                            originalLocation.getBlockY() + 1, originalLocation.getBlockZ() + 0.5);
                    if (Utils.hasNoSpace(spawnLocation)) {
                        player.sendMessage(Component.text("You need at least two blocks of free space to release the creature.",
                                NamedTextColor.RED));
                        return;
                    }
                    Entity entity = terrarium.getTrappedCreature(handItem, spawnLocation.getWorld());
                    if (entity == null) {
                        player.sendMessage(Component.text("The creature could not be released. The terrarium might be corrupted.",
                                NamedTextColor.RED));
                        return;
                    }
                    if (!entity.spawnAt(spawnLocation)) {
                        player.sendMessage(Component.text("You can't release this creature here. Something stopped it from " +
                                "spawning.", NamedTextColor.RED));
                        return;
                    }
                    if (terrarium.isLocked(handItem))
                        handItem.subtract();
                    else
                        Utils.transformItem(handItem, terrarium.emptyTerrarium(handItem), slot, player.getInventory(), player);
                    player.updateInventory();
                    String mobName = mobInfoManager.getTypeAndName(entity);
                    player.sendMessage(Component.text("Successfully released the " + mobName + ".", NamedTextColor.GOLD));
                    plugin.getLogger().info(mobName + " released from a Terrarium at " +
                            Utils.locationToString(entity.getLocation()) + " by player " + player.getName() + ".");
                }
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();
            if (!player.isSneaking())
                return;
            ItemStack itemStack = event.getItem();
            if (isNothing(itemStack))
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
                    placeholders.put("<playerFacing>", player.getFacing().name().toLowerCase());
                    useSpellbook(itemStack, player, placeholders, null, slot, null);
                } else if (gatewayAnchor.isEnabledTrinket(itemStack)) {
                    Location location = player.getLocation();
                    ItemStack result = gatewayAnchor.setLocation(itemStack, location);
                    Utils.transformItem(itemStack, result, slot, inventory, player);
                    player.updateInventory();
                } else if (terrarium.isEnabledTrinket(itemStack)) {
                    if (!terrarium.canUse(itemStack) || !terrarium.hasTrappedCreature(itemStack) || terrarium.isLocked(itemStack))
                        return;
                    long lastAttempt = terrariumLockAttempts.getOrDefault(player, 0L);
                    long now = System.currentTimeMillis();
                    if (now - lastAttempt >= 3000) {
                        player.sendMessage(Component.text("Are you sure you want to lock this terrarium? " +
                                "Right click air while sneaking again to confirm.", NamedTextColor.GREEN));
                        terrariumLockAttempts.put(player, now);
                    } else {
                        terrariumLockAttempts.remove(player);
                        ItemStack result = terrarium.lock(itemStack, player.getWorld());
                        if (result == null) {
                            player.sendMessage(Component.text("This terrarium could not be locked.", NamedTextColor.RED));
                            return;
                        }
                        Utils.transformItem(itemStack, result, slot, inventory, player);
                        player.updateInventory();
                    }
                } else if (healingHerb.isEnabledTrinket(itemStack)) {
                    restoreHealth(player, itemStack, player);
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
            } else if (snowGolemBlueprint.isEnabledTrinket(itemStack)) {
                long now = System.currentTimeMillis();
                int cooldownAmount = snowGolemBlueprint.getCooldown(itemStack);
                if (cooldownAmount == -1) {
                    if (now - cooldowns.getOrDefault(player, 0L) <= 1000)
                        return;
                } else {
                    if (now - cooldowns.getOrDefault(player, 0L) <= cooldownAmount * 1000L)
                        return;
                }
                Location originalLocation = block.getLocation();
                Location spawnLocation = new Location(originalLocation.getWorld(), originalLocation.getBlockX() + 0.5,
                        originalLocation.getBlockY() + 1, originalLocation.getBlockZ() + 0.5);
                if (Utils.hasNoSpace(spawnLocation)) {
                    player.sendMessage(Component.text("You need at least two blocks of free space.", NamedTextColor.RED));
                    return;
                }
                ItemStack[] materials = snowGolemBlueprint.getMaterials(player);
                if (materials == null) {
                    player.sendMessage(Component.text("You don't have enough materials.", NamedTextColor.RED));
                    return;
                }
                Entity result = player.getWorld().spawn(spawnLocation, Snowman.class);
                if (result.isValid()) {
                    for (ItemStack material : materials) {
                        material.subtract();
                    }
                    plugin.getLogger().info("Player " + player.getName() + " built a snow golem using a blueprint at " +
                            Utils.serializeCoordsLogging(spawnLocation));
                } else {
                    player.sendMessage(Component.text("Could not create the snow golem there.", NamedTextColor.RED));
                }
                cooldowns.put(player, now);
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
        if (Utils.hasNoSpace(spawnLocation)) {
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

    private void parseCoords(String text, Map<String, String> placeholders, Location location) {
        Matcher matcher = X_COORD_PATTERN.matcher(text);
        if (matcher.find()) {
            String placeholder = matcher.group(0);
            int number = Integer.parseInt(matcher.group(1));
            placeholders.put(placeholder, String.valueOf(location.getBlockX() + number));
        }
        matcher = Y_COORD_PATTERN.matcher(text);
        if (matcher.find()) {
            String placeholder = matcher.group(0);
            int number = Integer.parseInt(matcher.group(1));
            placeholders.put(placeholder, String.valueOf(location.getBlockY() + number));
        }
        matcher = Z_COORD_PATTERN.matcher(text);
        if (matcher.find()) {
            String placeholder = matcher.group(0);
            int number = Integer.parseInt(matcher.group(1));
            placeholders.put(placeholder, String.valueOf(location.getBlockZ() + number));
        }
    }

    private void parseTargetCoords(String text, Map<String, String> placeholders, Location location) {
        Matcher matcher = TARGET_X_COORD_PATTERN.matcher(text);
        if (matcher.find()) {
            String placeholder = matcher.group(0);
            int number = Integer.parseInt(matcher.group(1));
            placeholders.put(placeholder, String.valueOf(location.getBlockX() + number));
        }
        matcher = TARGET_Y_COORD_PATTERN.matcher(text);
        if (matcher.find()) {
            String placeholder = matcher.group(0);
            int number = Integer.parseInt(matcher.group(1));
            placeholders.put(placeholder, String.valueOf(location.getBlockY() + number));
        }
        matcher = TARGET_Z_COORD_PATTERN.matcher(text);
        if (matcher.find()) {
            String placeholder = matcher.group(0);
            int number = Integer.parseInt(matcher.group(1));
            placeholders.put(placeholder, String.valueOf(location.getBlockZ() + number));
        }
    }

    private void useSpellbook(ItemStack itemStack, Player player, Map<String, String> placeholders, CommandEvent actionEvent,
                              int slot, Location interactLocation) {
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
        if (!spellbook.canUseHere(itemStack, player.getWorld())) {
            player.sendMessage(Component.text("You can't use this spellbook in this world.", NamedTextColor.RED));
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
            parseCoords(toExecute, placeholders, player.getLocation());
            if (interactLocation != null) {
                parseTargetCoords(toExecute, placeholders, interactLocation);
            }
            if (neededEvent == CommandEvent.TARGET_MOB) {
                if (!targetEntityComputed) {
                    targetEntity = player.getTargetEntity(120);
                    targetEntityComputed = true;
                }
                if (!(targetEntity instanceof LivingEntity targetMob))
                    continue;
                placeholders.put("<mobType>", targetMob.getType().name());
                placeholders.put("<targetCoords>", Utils.serializeCoordsCommand(targetMob.getLocation()));
                parseTargetCoords(toExecute, placeholders, targetMob.getLocation());
            } else if (neededEvent == CommandEvent.TARGET_PLAYER) {
                if (!targetEntityComputed) {
                    targetEntity = player.getTargetEntity(120);
                    targetEntityComputed = true;
                }
                if (!(targetEntity instanceof Player targetPlayer))
                    continue;
                placeholders.put("<targetPlayerName>", targetPlayer.getName());
                placeholders.put("<targetCoords>", Utils.serializeCoordsCommand(targetPlayer.getLocation()));
                parseTargetCoords(toExecute, placeholders, targetPlayer.getLocation());
            } else if (neededEvent == CommandEvent.TARGET_BLOCK) {
                if (!targetBlockComputed) {
                    targetBlock = player.getTargetBlockExact(400);
                    targetBlockComputed = true;
                }
                if (targetBlock == null)
                    continue;
                placeholders.put("<blockType>", targetBlock.getType().name());
                placeholders.put("<targetCoords>", Utils.serializeCoordsCommand(targetBlock.getLocation()));
                parseTargetCoords(toExecute, placeholders, targetBlock.getLocation());
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

    private void restoreHealth(LivingEntity entity, ItemStack itemStack, Player player) {
        double health = entity.getHealth();
        AttributeInstance healthAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute == null)
            return;
        double maxHealth = healthAttribute.getValue();
        if (health >= maxHealth)
            return;
        entity.setHealth(maxHealth);
        itemStack.subtract();
        player.updateInventory();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemFrameChange(PlayerItemFrameChangeEvent event) {
        ItemFrame itemFrame = event.getItemFrame();
        if (!itemFrame.isVisible())
            return;
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = inventory.getItemInMainHand();
        if (invisibilityPowder.isEnabledTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, player)) {
            event.setCancelled(true);
            itemFrame.setVisible(false);
            itemStack.subtract();
            player.updateInventory();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cooldowns.remove(event.getPlayer());
        terrariumLockAttempts.remove(event.getPlayer());
    }
}
