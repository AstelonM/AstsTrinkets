package com.astelon.aststrinkets.managers;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.trinkets.creature.CuringApple;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ZombieCuringManager {

    private final Random random;

    private final AstsTrinkets plugin;
    private final MobInfoManager mobInfoManager;
    private final NamespacedKeys keys;
    private final CuringApple curingApple;

    private final Map<LivingEntity, BukkitTask> curingTasks;

    public ZombieCuringManager(AstsTrinkets plugin, TrinketManager trinketManager, MobInfoManager mobInfoManager) {
        this.plugin = plugin;
        this.mobInfoManager = mobInfoManager;
        this.keys = trinketManager.getNamespacedKeys();
        curingApple = trinketManager.getCuringApple();
        curingTasks = new HashMap<>();
        random = new Random();
    }

    public void startCuring(LivingEntity entity, Player player) {
        long endTime = getCuringTime(entity);
        if (endTime == -1) {
            if (player != null) {
                player.sendMessage(Component.text("This creature could not be cured.", NamedTextColor.RED));
            }
            plugin.getLogger().warning("Missing end time for curing Nether zombie " + mobInfoManager.getTypeAndName(entity) + " at " +
                    Utils.serializeCoordsLogging(entity.getLocation()));
            return;
        }
        // Duration in ms, then in seconds, then in ticks
        long ticks = (endTime - System.currentTimeMillis()) / 1000 * 20;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            boolean cured = cureEntity(entity);
            curingTasks.remove(entity);
            if (cured) {
                plugin.getLogger().info("Cured entity " + mobInfoManager.getTypeAndName(entity) + " at " +
                        Utils.serializeCoordsLogging(entity.getLocation()) + ".");
            }
        }, ticks);
        plugin.getLogger().info("Started curing entity " + mobInfoManager.getTypeAndName(entity) + " at " +
                Utils.serializeCoordsLogging(entity.getLocation()) + " in " + (ticks / 20) + " seconds.");
    }

    public void stopCuring(LivingEntity entity) {
        BukkitTask task = curingTasks.remove(entity);
        if (task != null) {
            task.cancel();
        }
        plugin.getLogger().info("Stopped curing entity " + mobInfoManager.getTypeAndName(entity) + " at " +
                Utils.serializeCoordsLogging(entity.getLocation()) + ".");
    }

    private long getCuringTime(Entity entity) {
        PersistentDataContainer container = entity.getPersistentDataContainer();
        return container.getOrDefault(keys.endTimeKey, PersistentDataType.LONG, -1L);
    }

    private boolean cureEntity(LivingEntity entity) {
        if (entity == null || entity.isDead())
            return false;
        if (entity instanceof PigZombie zombiePiglin) {
            curePiglin(zombiePiglin);
            return true;
        } else if (entity instanceof Zoglin zoglin) {
            cureZoglin(zoglin);
            return true;
        }
        return false;
    }

    private void curePiglin(PigZombie zombiePiglin) {
        Location location = zombiePiglin.getLocation();
        PersistentDataContainer container = zombiePiglin.getPersistentDataContainer();

        byte wasBrute = container.getOrDefault(keys.wasBruteKey, PersistentDataType.BYTE, (byte) -1);
        PiglinAbstract piglin = switch (wasBrute) {
            case 1 -> zombiePiglin.getWorld().spawn(location, PiglinBrute.class);
            case 0 -> zombiePiglin.getWorld().spawn(location, Piglin.class);
            default -> {
                double roll = random.nextDouble();
                if (roll < curingApple.getBruteChance())
                    yield zombiePiglin.getWorld().spawn(location, PiglinBrute.class);
                else
                    yield zombiePiglin.getWorld().spawn(location, Piglin.class);
            }
        };
        piglin.setImmuneToZombification(true);
        Utils.copyCommonEntityAttributes(zombiePiglin, piglin);
        zombiePiglin.remove();
        piglin.getWorld().playSound(piglin, Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.HOSTILE, 2.0f,
                random.nextFloat(0.8f, 1.2f));
    }

    private void cureZoglin(Zoglin zoglin) {
        Location location = zoglin.getLocation();
        Hoglin hoglin = zoglin.getWorld().spawn(location, Hoglin.class);
        hoglin.setImmuneToZombification(true);
        Utils.copyCommonEntityAttributes(zoglin, hoglin);
        zoglin.remove();
    }
}
