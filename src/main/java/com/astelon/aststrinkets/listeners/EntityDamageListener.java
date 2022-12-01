package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.DivingHelmet;
import com.astelon.aststrinkets.trinkets.HydraulicBoots;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class EntityDamageListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final DivingHelmet divingHelmet;
    private final HydraulicBoots hydraulicBoots;

    public EntityDamageListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        divingHelmet = trinketManager.getDivingHelmet();
        hydraulicBoots = trinketManager.getHydraulicBoots();
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity))
            return;
        EntityEquipment equipment = entity.getEquipment();
        if (equipment == null)
            return;
        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause == EntityDamageEvent.DamageCause.DROWNING) {
            ItemStack helmet = equipment.getHelmet();
            if (trinketManager.isOwnedWithRestrictions(helmet, entity)) {
                if (divingHelmet.isEnabled() && divingHelmet.isTrinket(helmet)) {
                    event.setCancelled(true);
                    entity.setRemainingAir(entity.getMaximumAir());
                }
            }
        } else if (cause == EntityDamageEvent.DamageCause.FALL) {
            ItemStack boots = equipment.getBoots();
            if (trinketManager.isOwnedWithRestrictions(boots, entity)) {
                if (hydraulicBoots.isEnabled() && hydraulicBoots.isTrinket(boots)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
