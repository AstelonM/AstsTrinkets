package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Souleater;
import com.astelon.aststrinkets.trinkets.VampiricSword;
import com.astelon.aststrinkets.trinkets.equipable.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class EntityDamageListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final MobInfoManager mobInfoManager;
    private final DivingHelmet divingHelmet;
    private final HydraulicBoots hydraulicBoots;
    private final Souleater souleater;
    private final FireproofVest fireproofVest;
    private final InvincibilityBelt invincibilityBelt;
    private final VampiricSword vampiricSword;
    private final UnbreakableTurtleShell unbreakableTurtleShell;

    public EntityDamageListener(AstsTrinkets plugin, TrinketManager trinketManager, MobInfoManager mobInfoManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.mobInfoManager = mobInfoManager;
        divingHelmet = trinketManager.getDivingHelmet();
        hydraulicBoots = trinketManager.getHydraulicBoots();
        souleater = trinketManager.getSouleater();
        fireproofVest = trinketManager.getFireproofVest();
        invincibilityBelt = trinketManager.getInvincibilityBelt();
        vampiricSword = trinketManager.getVampiricSword();
        unbreakableTurtleShell = trinketManager.getUnbreakableTurtleShell();
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity))
            return;
        EntityEquipment equipment = entity.getEquipment();
        if (equipment == null)
            return;
        ItemStack leggings = equipment.getLeggings();
        if (invincibilityBelt.isEnabledTrinket(leggings) && trinketManager.isOwnedWithRestrictions(leggings, entity))
            event.setCancelled(true);
        ItemStack helmet = equipment.getHelmet();
        if (unbreakableTurtleShell.isEnabledTrinket(helmet) && trinketManager.isOwnedWithRestrictions(helmet, entity))
            event.setCancelled(true);
        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause == EntityDamageEvent.DamageCause.DROWNING) {
            if (trinketManager.isOwnedWithRestrictions(helmet, entity)) {
                if (divingHelmet.isEnabledTrinket(helmet)) {
                    event.setCancelled(true);
                    entity.setRemainingAir(entity.getMaximumAir());
                }
            }
        } else if (cause == EntityDamageEvent.DamageCause.FALL) {
            ItemStack boots = equipment.getBoots();
            if (trinketManager.isOwnedWithRestrictions(boots, entity)) {
                if (hydraulicBoots.isEnabledTrinket(boots)) {
                    event.setCancelled(true);
                }
            }
        } else if (cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK) {
            ItemStack chestplate = equipment.getChestplate();
            if (trinketManager.isOwnedWithRestrictions(chestplate, entity)) {
                if (fireproofVest.isEnabledTrinket(chestplate)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK &&
                event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
            return;
        if (!(event.getEntity() instanceof LivingEntity defender))
            return;
        if (!(event.getDamager() instanceof LivingEntity attacker))
            return;
        EntityEquipment attackerEquipment = attacker.getEquipment();
        if (attackerEquipment == null)
            return;
        ItemStack weapon = attackerEquipment.getItemInMainHand();
        if (trinketManager.isOwnedWithRestrictions(weapon, attacker)) {
            if (souleater.isEnabledTrinket(weapon)) {
                if (trinketManager.isTrinketImmune(event.getEntity()))
                    return; //TODO message?
                if (attacker instanceof Player player && player.getAttackCooldown() < 0.9)
                    return;
                if (!souleater.canEat(weapon))
                    return;
                EntityEquipment defenderEquipment = defender.getEquipment();
                if (defenderEquipment == null)
                    return;
                if (souleater.eatEnchantment(weapon, defenderEquipment)) {
                    String attackerName;
                    if (attacker instanceof Player player)
                        attackerName = "Player " + player.getName();
                    else
                        attackerName = mobInfoManager.getTypeAndName(attacker);
                    String defenderName;
                    if (defender instanceof Player player)
                        defenderName = "Player " + player.getName();
                    else
                        defenderName = mobInfoManager.getTypeAndName(defender);
                    plugin.getLogger().info("The Souleater of " + attackerName + " consumed an enchantment of " +
                            defenderName + ".");
                }
            } else if (vampiricSword.isEnabledTrinket(weapon)) {
                if (attacker instanceof Player player && player.getAttackCooldown() < 0.9)
                    return;
                if (!vampiricSword.canUse(weapon))
                    return;
                double damage = event.getFinalDamage();
                vampiricSword.tryAbsorbHealth(weapon, damage, attacker);
            }
        }
    }
}
