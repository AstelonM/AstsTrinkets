package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.PerfectedReignitableRocket;
import com.astelon.aststrinkets.trinkets.ReignitableRocket;
import com.astelon.aststrinkets.trinkets.ReignitableRocketPrototype;
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FireworkListener implements Listener {

    private final Random random;
    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final ReignitableRocketPrototype reignitableRocketPrototype;
    private final ReignitableRocket reignitableRocket;
    private final PerfectedReignitableRocket perfectedReignitableRocket;

    public FireworkListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        random = new Random();
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.reignitableRocketPrototype = trinketManager.getReignitableRocketPrototype();
        this.reignitableRocket = trinketManager.getReignitableRocket();
        this.perfectedReignitableRocket = trinketManager.getPerfectedReignitableRocket();
    }

    @EventHandler
    public void onPlayerElytraBoost(PlayerElytraBoostEvent event) {
        Player player = event.getPlayer();
        ItemStack rocket = event.getItemStack();
        if (trinketManager.isOwnedBy(rocket, player.getName())) {
            if (reignitableRocketPrototype.isEnabled() && reignitableRocketPrototype.isTrinket(rocket)) {
                double roll = random.nextDouble();
                if (roll < reignitableRocketPrototype.getCriticalFailureChance(rocket)) {
                    Location location = player.getLocation();
                    location.createExplosion(player, 8f, true, true);
                } else if (roll >= reignitableRocketPrototype.getFailureChance(rocket)) {
                    event.setShouldConsume(false);
                }
            } else if (reignitableRocket.isEnabled() && reignitableRocket.isTrinket(rocket)) {
                double roll = random.nextDouble();
                if (roll >= reignitableRocket.getFailureChance(rocket)) {
                    event.setShouldConsume(false);
                }
            } else if (perfectedReignitableRocket.isEnabled() && perfectedReignitableRocket.isTrinket(rocket)) {
                event.setShouldConsume(false);
            }
        }
    }
}