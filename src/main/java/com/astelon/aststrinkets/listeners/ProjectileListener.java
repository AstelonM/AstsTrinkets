package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.projectile.ExperienceBottle;
import com.astelon.aststrinkets.trinkets.projectile.MysteryEgg;
import com.astelon.aststrinkets.utils.Utils;
import com.destroystokyo.paper.event.entity.ThrownEggHatchEvent;
import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class ProjectileListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final MobInfoManager mobInfoManager;
    private final MysteryEgg mysteryEgg;
    private final ExperienceBottle experienceBottle;

    public ProjectileListener(AstsTrinkets plugin, TrinketManager trinketManager, MobInfoManager mobInfoManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.mobInfoManager = mobInfoManager;
        mysteryEgg = trinketManager.getMysteryEgg();
        experienceBottle = trinketManager.getExperienceBottle();
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
}
