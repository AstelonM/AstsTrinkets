package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.DeathArrow;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.trinkets.TrueDeathArrow;
import com.astelon.aststrinkets.utils.Utils;
import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class ArrowListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final DeathArrow deathArrow;
    private final TrueDeathArrow trueDeathArrow;

    public ArrowListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.deathArrow = trinketManager.getDeathArrow();
        this.trueDeathArrow = trinketManager.getTrueDeathArrow();
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityShootBow(EntityShootBowEvent event) {
        Entity shooter = event.getEntity();
        ItemStack itemStack = event.getConsumable();
        Entity projectile = event.getProjectile();
        if (!(projectile instanceof Arrow arrow))
            return;
        if (itemStack != null && isOwnedWithRestrictions(itemStack, shooter)) {
            if (deathArrow.isEnabled() && deathArrow.isTrinket(itemStack)) {
                deathArrow.setProjectileTrinket(arrow, itemStack);
            } else if (trueDeathArrow.isEnabled() && trueDeathArrow.isTrinket(itemStack)) {
                trueDeathArrow.setProjectileTrinket(arrow, itemStack);
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
            if (isOwnedWithRestrictions(arrow, shooterEntity)) {
                if (deathArrow.isEnabled() && deathArrow.isTrinket(arrow)) {
                    livingEntity.damage(1000000, shooterEntity);
                    logDeath(deathArrow, shooterEntity, livingEntity);
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
            if (isOwnedWithRestrictions(arrow, shooterEntity)) {
                if (trueDeathArrow.isEnabled() && trueDeathArrow.isTrinket(arrow)) {
                    livingEntity.damage(1000000);
                    livingEntity.setHealth(0);
                    logDeath(trueDeathArrow, shooterEntity, livingEntity);
                }
            }
        }
    }

    private boolean isOwnedWithRestrictions(ItemStack itemStack, Entity entity) {
        String owner = trinketManager.getOwner(itemStack);
        if (owner != null) {
            if (entity instanceof Player player && !trinketManager.isOwnedBy(itemStack, player.getName()))
                return false;
            else
                return entity instanceof Player;
        }
        return true;
    }

    private boolean isOwnedWithRestrictions(Arrow arrow, Entity entity) {
        String owner = trinketManager.getOwner(arrow);
        if (owner != null) {
            if (entity instanceof Player player && !trinketManager.isOwnedBy(arrow, player.getName()))
                return false;
            else
                return entity instanceof Player;
        }
        return true;
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
            return trinketManager.getMobInfoManager().getTypeAndName(entity);
    }
}
