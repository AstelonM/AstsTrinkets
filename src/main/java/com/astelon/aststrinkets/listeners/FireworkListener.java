package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.rocket.MysteryFirework;
import com.astelon.aststrinkets.trinkets.rocket.PerfectedReignitableRocket;
import com.astelon.aststrinkets.trinkets.rocket.ReignitableRocket;
import com.astelon.aststrinkets.trinkets.rocket.ReignitableRocketPrototype;
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Random;

public class FireworkListener implements Listener {

    private final Random random;
    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final ReignitableRocketPrototype reignitableRocketPrototype;
    private final ReignitableRocket reignitableRocket;
    private final PerfectedReignitableRocket perfectedReignitableRocket;
    private final MysteryFirework mysteryFirework;

    public FireworkListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        random = new Random();
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.reignitableRocketPrototype = trinketManager.getReignitableRocketPrototype();
        this.reignitableRocket = trinketManager.getReignitableRocket();
        this.perfectedReignitableRocket = trinketManager.getPerfectedReignitableRocket();
        this.mysteryFirework = trinketManager.getMysteryFirework();
    }

    @EventHandler
    public void onPlayerElytraBoost(PlayerElytraBoostEvent event) {
        Player player = event.getPlayer();
        ItemStack rocket = event.getItemStack();
        if (trinketManager.isOwnedBy(rocket, player.getName())) {
            if (reignitableRocketPrototype.isEnabledTrinket(rocket)) {
                double roll = random.nextDouble();
                if (roll < reignitableRocketPrototype.getCriticalFailureChance(rocket)) {
                    Location location = player.getLocation();
                    location.createExplosion(player, 8f, true, true);
                    if (reignitableRocketPrototype.isPluginExplosion())
                        location.createExplosion(8f, false, false);
                } else if (roll >= reignitableRocketPrototype.getFailureChance(rocket)) {
                    event.setShouldConsume(false);
                }
            } else if (reignitableRocket.isEnabledTrinket(rocket)) {
                double roll = random.nextDouble();
                if (roll >= reignitableRocket.getFailureChance(rocket)) {
                    event.setShouldConsume(false);
                }
            } else if (perfectedReignitableRocket.isEnabledTrinket(rocket)) {
                event.setShouldConsume(false);
            } else if (mysteryFirework.isEnabledTrinket(rocket)) {
                Firework firework = event.getFirework();
                //TODO is possible random duration an issue?
                mysteryFirework.setRandomEffects(firework);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = event.getHand() == EquipmentSlot.HAND ? inventory.getItemInMainHand() : inventory.getItemInOffHand();
            if (reignitableRocketPrototype.isTrinket(itemStack) && !reignitableRocketPrototype.isAllowUseAsFirework() ||
                    reignitableRocket.isTrinket(itemStack) && !reignitableRocket.isAllowUseAsFirework() ||
                    perfectedReignitableRocket.isTrinket(itemStack) && !perfectedReignitableRocket.isAllowUseAsFirework())
                event.setUseItemInHand(Event.Result.DENY);
        }
    }

    @EventHandler
    public void onPlayerLaunchProjectile(PlayerLaunchProjectileEvent event) {
        if (event.getProjectile() instanceof Firework firework) {
            ItemStack itemStack = event.getItemStack();
            if (mysteryFirework.isEnabledTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, event.getPlayer()))
                mysteryFirework.setRandomEffects(firework);
        }
    }
}
