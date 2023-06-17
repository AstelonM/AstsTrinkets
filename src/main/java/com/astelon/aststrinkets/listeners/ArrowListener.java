package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.projectile.arrow.*;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.Utils;
import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class ArrowListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final MobInfoManager mobInfoManager;
    private final DeathArrow deathArrow;
    private final TrueDeathArrow trueDeathArrow;
    private final SmitingArrow smitingArrow;
    private final ExplosiveArrow explosiveArrow;

    public ArrowListener(AstsTrinkets plugin, TrinketManager trinketManager, MobInfoManager mobInfoManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.mobInfoManager = mobInfoManager;
        deathArrow = trinketManager.getDeathArrow();
        trueDeathArrow = trinketManager.getTrueDeathArrow();
        smitingArrow = trinketManager.getSmitingArrow();
        explosiveArrow = trinketManager.getExplosiveArrow();
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
            if (!(shooter instanceof Entity shooterEntity))
                return;
            if (trinketManager.isOwnedWithRestrictions(arrow, shooterEntity)) {
                if (deathArrow.isEnabledTrinket(arrow)) {
                    event.setDamage(1000000);
                    livingEntity.setHealth(0);
                    logDeath(deathArrow, shooterEntity, livingEntity);
                    checkAndRemovePiercing(arrow);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileCollide(ProjectileCollideEvent event) {
        Projectile projectile = event.getEntity();
        Entity hit = event.getCollidedWith();
        if (projectile instanceof Arrow arrow && hit instanceof LivingEntity livingEntity) {
            ProjectileSource shooter = arrow.getShooter();
            if (!(shooter instanceof Entity shooterEntity))
                return;
            if (trinketManager.isOwnedWithRestrictions(arrow, shooterEntity)) {
                if (trueDeathArrow.isEnabledTrinket(arrow)) {
                    livingEntity.damage(1000000);
                    livingEntity.setHealth(0);
                    logDeath(trueDeathArrow, shooterEntity, livingEntity);
                    checkAndRemovePiercing(arrow);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile instanceof Arrow arrow) {
            ProjectileSource shooter = arrow.getShooter();
            if (!(shooter instanceof Entity shooterEntity))
                return;
            if (trinketManager.isOwnedWithRestrictions(arrow, shooterEntity)) {
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
    }

    private void logDeath(Trinket trinket, Entity killer, Entity killed) {
        String killerName = getProperName(killer);
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
}
