package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.trinkets.projectile.ExperienceBottle;
import com.astelon.aststrinkets.trinkets.projectile.MysteryEgg;
import com.astelon.aststrinkets.trinkets.projectile.ProjectileTrinket;
import com.astelon.aststrinkets.trinkets.projectile.arrow.*;
import com.astelon.aststrinkets.utils.Utils;
import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import com.destroystokyo.paper.event.entity.ThrownEggHatchEvent;
import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;

public class ProjectileListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final MobInfoManager mobInfoManager;

    private final HashMap<Block, ItemStack> launchedTrinkets;

    private final DeathArrow deathArrow;
    private final TrueDeathArrow trueDeathArrow;
    private final SmitingArrow smitingArrow;
    private final ExplosiveArrow explosiveArrow;
    private final MysteryEgg mysteryEgg;
    private final ExperienceBottle experienceBottle;

    public ProjectileListener(AstsTrinkets plugin, TrinketManager trinketManager, MobInfoManager mobInfoManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.mobInfoManager = mobInfoManager;
        launchedTrinkets = new HashMap<>();
        deathArrow = trinketManager.getDeathArrow();
        trueDeathArrow = trinketManager.getTrueDeathArrow();
        smitingArrow = trinketManager.getSmitingArrow();
        explosiveArrow = trinketManager.getExplosiveArrow();
        mysteryEgg = trinketManager.getMysteryEgg();
        experienceBottle = trinketManager.getExperienceBottle();
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityShootBow(EntityShootBowEvent event) {
        LivingEntity shooter = event.getEntity();
        ItemStack itemStack = event.getConsumable();
        if (!(shooter instanceof Player)) {
            EntityEquipment equipment = shooter.getEquipment();
            if (equipment != null) {
                ItemStack offHand = equipment.getItemInOffHand();
                if (offHand.getType().name().endsWith("ARROW"))
                    itemStack = offHand;
                else {
                    ItemStack mainHand = equipment.getItemInMainHand();
                    if (mainHand.getType().name().endsWith("ARROW"))
                        itemStack = mainHand;
                }
            }
        }
        Entity projectile = event.getProjectile();
        if (!(projectile instanceof Arrow arrow))
            return;
        if (itemStack != null && trinketManager.isOwnedWithRestrictions(itemStack, shooter)) {
            Trinket trinket = trinketManager.getTrinket(itemStack);
            if (trinket instanceof ArrowTrinket arrowTrinket) {
                ItemStack weapon = event.getBow();
                if (weapon != null && weapon.getType() == Material.CROSSBOW) {
                    if (!arrowTrinket.isMultishotAllowed() && weapon.containsEnchantment(Enchantment.MULTISHOT) &&
                            arrow.getPickupStatus() == AbstractArrow.PickupStatus.CREATIVE_ONLY &&
                            !(shooter instanceof Player player && player.getGameMode() == GameMode.CREATIVE))
                        return;
                }
            }

            if (deathArrow.isEnabledTrinket(itemStack)) {
                deathArrow.setProjectileTrinket(arrow, itemStack);
            } else if (trueDeathArrow.isEnabledTrinket(itemStack)) {
                trueDeathArrow.setProjectileTrinket(arrow, itemStack);
            } else if (smitingArrow.isEnabledTrinket(itemStack)) {
                smitingArrow.setProjectileTrinket(arrow, itemStack);
            } else if (explosiveArrow.isEnabledTrinket(itemStack)) {
                explosiveArrow.setProjectileTrinket(arrow, itemStack);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();
        if (damager instanceof Arrow arrow && damaged instanceof LivingEntity livingEntity) {
            ProjectileSource shooter = arrow.getShooter();
            Entity shooterEntity = shooter instanceof Entity ? (Entity) shooter : null;
            if (shooterEntity != null && !trinketManager.isOwnedWithRestrictions(arrow, shooterEntity) ||
                    shooterEntity == null && trinketManager.getOwner(arrow) != null)
                return;
            if (deathArrow.isEnabledTrinket(arrow)) {
                event.setDamage(1000000);
                livingEntity.setHealth(0);
                logDeath(deathArrow, shooterEntity, livingEntity);
                checkAndRemovePiercing(arrow);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileCollide(ProjectileCollideEvent event) {
        Projectile projectile = event.getEntity();
        Entity hit = event.getCollidedWith();
        if (projectile instanceof Arrow arrow && hit instanceof LivingEntity livingEntity) {
            ProjectileSource shooter = arrow.getShooter();
            Entity shooterEntity = shooter instanceof Entity ? (Entity) shooter : null;
            if (shooterEntity != null && !trinketManager.isOwnedWithRestrictions(arrow, shooterEntity) ||
                    shooterEntity == null && trinketManager.getOwner(arrow) != null)
                return;
            if (trueDeathArrow.isEnabledTrinket(arrow)) {
                livingEntity.damage(1000000);
                livingEntity.setHealth(0);
                logDeath(trueDeathArrow, shooterEntity, livingEntity);
                checkAndRemovePiercing(arrow);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile instanceof Arrow arrow) {
            ProjectileSource shooter = arrow.getShooter();
            Entity shooterEntity = shooter instanceof Entity ? (Entity) shooter : null;
            if (shooterEntity != null && !trinketManager.isOwnedWithRestrictions(arrow, shooterEntity) ||
                    shooterEntity == null && trinketManager.getOwner(arrow) != null)
                return;
            if (smitingArrow.isEnabledTrinket(arrow)) {
                World world = projectile.getWorld();
                Location location = projectile.getLocation();
                world.strikeLightning(location);
                checkAndRemovePiercing(arrow);
            } else if (explosiveArrow.isEnabledTrinket(arrow)) {
                World world = projectile.getWorld();
                Location location = projectile.getLocation();
                world.createExplosion(location, 2f, false, false, shooterEntity);
                checkAndRemovePiercing(arrow);
            }
        }
    }

    private void logDeath(Trinket trinket, Entity killer, Entity killed) {
        String killerName;
        if (killer == null)
            killerName = "Dispenser";
        else
            killerName = getProperName(killer);
        String killedName = getProperName(killed);
        plugin.getLogger().info(killerName + " killed " + killedName + " at " +
                Utils.locationToString(killed.getLocation()) + " using a " + trinket.getName() +  " trinket.");
    }

    private String getProperName(Entity entity) {
        if (entity instanceof Player player)
            return "Player " + player.getName();
        else
            return mobInfoManager.getTypeAndName(entity);
    }

    private void checkAndRemovePiercing(Arrow arrow) {
        Trinket trinket = trinketManager.getTrinket(arrow);
        if (trinket instanceof ArrowTrinket arrowTrinket) {
            if (arrow.getPierceLevel() != 0 && !arrowTrinket.isPiercingAllowed())
                arrowTrinket.removeProjectileTrinket(arrow);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLaunchProjectile(PlayerLaunchProjectileEvent event) {
        ItemStack itemStack = event.getItemStack();
        Player player = event.getPlayer();
        if (trinketManager.isOwnedBy(itemStack, player.getName())) {
            if (mysteryEgg.isEnabledTrinket(itemStack)) {
                Projectile projectile = event.getProjectile();
                mysteryEgg.setProjectileTrinket(projectile, itemStack);
            } else if (experienceBottle.isEnabledTrinket(itemStack)) {
                long lastUse = experienceBottle.getLastUse(itemStack);
                if (System.currentTimeMillis() - lastUse <= 500L) {
                    event.setCancelled(true);
                    return;
                }
                int experience = experienceBottle.getExperience(itemStack);
                if (experience == -1) {
                    player.sendMessage(Component.text("The experience stored in this bottle became spoiled.", NamedTextColor.RED));
                    return;
                }
                Projectile projectile = event.getProjectile();
                experienceBottle.setProjectileTrinket(projectile, itemStack);
            }
        }
    }

    @EventHandler
    public void onThrownEggHatch(ThrownEggHatchEvent event) {
        Egg egg = event.getEgg();
        if (mysteryEgg.isEnabledTrinket(egg)) {
            EntityType type = mysteryEgg.getRandomEntityType();
            event.setHatchingType(type);
            event.setHatching(true);
            if (event.getNumHatches() == 0)
                event.setNumHatches((byte) 1);
            ProjectileSource thrower = egg.getShooter();
            if (thrower instanceof Player player) {
                plugin.getLogger().info("Player " + player.getName() + " threw a Mystery Egg at " +
                        Utils.locationToString(egg.getLocation()) + " and it might have hatched " +
                        mobInfoManager.getTypeName(type) + ".");
            } else {
                plugin.getLogger().info("Mystery Egg might have hatched " + mobInfoManager.getTypeName(type) + " at " +
                        Utils.locationToString(egg.getLocation()) + ".");
            }
        }
    }

    @EventHandler
    public void onExpBottle(ExpBottleEvent event) {
        ThrownExpBottle bottle = event.getEntity();
        if (experienceBottle.isEnabledTrinket(bottle)) {
            int experience = experienceBottle.getExperience(bottle);
            if (experience == -1)
                return;
            event.setExperience(experience);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDispense(BlockDispenseEvent event) {
        ItemStack itemStack = event.getItem();
        Trinket trinket = trinketManager.getTrinket(itemStack);
        if (!(trinket instanceof ProjectileTrinket projectileTrinket) || trinketManager.getOwner(itemStack) != null ||
                !trinket.isEnabled() || !projectileTrinket.isDispenserAllowed())
            return;
        Block block = event.getBlock();
        launchedTrinkets.put(block, itemStack);
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        ProjectileSource projectileSource = projectile.getShooter();
        if (!(projectileSource instanceof BlockProjectileSource blockProjectileSource))
            return;
        Block block = blockProjectileSource.getBlock();
        if (event.isCancelled()) {
            launchedTrinkets.remove(block);
            return;
        }
        ItemStack itemStack = launchedTrinkets.remove(block);
        ProjectileTrinket trinket = (ProjectileTrinket) trinketManager.getTrinket(itemStack);
        if (trinket != null) { //TODO check if it's enabled again in case it somehow gets disabled in the meantime?
            trinket.setProjectileTrinket(projectile, itemStack);
        }
    }
}
