package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.projectile.MysteryEgg;
import com.astelon.aststrinkets.utils.Utils;
import com.destroystokyo.paper.event.entity.ThrownEggHatchEvent;
import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ProjectileListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final MobInfoManager mobInfoManager;
    private final MysteryEgg mysteryEgg;

    public ProjectileListener(AstsTrinkets plugin, TrinketManager trinketManager, MobInfoManager mobInfoManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.mobInfoManager = mobInfoManager;
        mysteryEgg = trinketManager.getMysteryEgg();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLaunchProjectile(PlayerLaunchProjectileEvent event) {
        ItemStack itemStack = event.getItemStack();
        Player player = event.getPlayer();
        if (mysteryEgg.isEnabledTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, player.getName())) {
            Projectile projectile = event.getProjectile();
            mysteryEgg.setProjectileTrinket(projectile, itemStack);
            plugin.getLogger().info("Player " + player.getName() + " threw a Mystery Egg from " +
                    Utils.locationToString(player.getLocation()) + ".");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onThrownEggHatch(ThrownEggHatchEvent event) {
        Egg egg = event.getEgg();
        if (mysteryEgg.isEnabledTrinket(egg)) {
            EntityType type = mysteryEgg.getRandomEntityType();
            event.setHatchingType(type);
            event.setHatching(true);
            plugin.getLogger().info("Mystery Egg hatched " + mobInfoManager.getTypeName(type) + " at " +
                    Utils.locationToString(egg.getLocation()) + ".");
        }
    }
}
