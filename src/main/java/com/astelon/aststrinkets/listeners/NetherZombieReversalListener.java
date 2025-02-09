package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.managers.ZombieCuringManager;
import com.astelon.aststrinkets.trinkets.creature.CuringApple;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class NetherZombieReversalListener implements Listener {

    private final Random random;

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final ZombieCuringManager curingManager;
    private final NamespacedKeys keys;
    private final CuringApple curingApple;

    public NetherZombieReversalListener(AstsTrinkets plugin, TrinketManager trinketManager, ZombieCuringManager curingManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.curingManager = curingManager;
        keys = trinketManager.getNamespacedKeys();
        curingApple = trinketManager.getCuringApple();
        random = new Random();
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        EquipmentSlot hand = event.getHand();
        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = hand == EquipmentSlot.HAND ? inventory.getItemInMainHand() : inventory.getItemInOffHand();
        if (curingApple.isEnabledTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, player)) {
            if (Utils.isNotNetherZombie(entity)) {
                player.sendMessage(Component.text("This creature can't be cured.", NamedTextColor.RED));
                return;
            }
            LivingEntity livingEntity = (LivingEntity) entity;
            Collection<PotionEffect> effects = livingEntity.getActivePotionEffects();
            boolean hasWeakness = false;
            for (PotionEffect effect : effects) {
                if (effect.getType().equals(PotionEffectType.WEAKNESS)) {
                    hasWeakness = true;
                    break;
                }
            }
            if (!hasWeakness) {
                return;
            }
            if (isCuring(livingEntity)) {
                player.sendMessage(Component.text("This creature is already being cured.", NamedTextColor.YELLOW));
                return;
            }
            beginCuring(livingEntity, player);
            itemStack.subtract();
        }
    }

    public boolean isCuring(LivingEntity entity) {
        PersistentDataContainer container = entity.getPersistentDataContainer();
        return container.has(keys.endTimeKey);
    }

    public void beginCuring(LivingEntity entity, Player player) {
        entity.getWorld().playSound(entity, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.HOSTILE,
                random.nextFloat(1.0f, 2.0f),
                random.nextFloat(0.3f, 1.0f));
        int time = random.nextInt(curingApple.getMinDuration(), curingApple.getMaxDuration() + 1);
        long finalTime = System.currentTimeMillis() + time * 1000L;
        PersistentDataContainer container = entity.getPersistentDataContainer();
        container.set(keys.endTimeKey, PersistentDataType.LONG, finalTime);
        curingManager.startCuring(entity, player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (isCuring(entity))
            curingManager.stopCuring(entity);
    }

    @EventHandler
    public void onEntitiesUnload(EntitiesUnloadEvent event) {
        for (Entity entity : event.getEntities()) {
            if (entity instanceof LivingEntity livingEntity && isCuring(livingEntity)) {
                curingManager.stopCuring(livingEntity);
            }
        }
    }

    @EventHandler
    public void onEntitiesLoad(EntitiesLoadEvent event) {
        for (Entity entity : event.getEntities()) {
            if (entity instanceof LivingEntity livingEntity && isCuring(livingEntity)) {
                curingManager.startCuring(livingEntity, null);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTransform(EntityTransformEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof PiglinAbstract && event.getTransformReason() == EntityTransformEvent.TransformReason.PIGLIN_ZOMBIFIED) {
            List<Entity> entities = event.getTransformedEntities();
            Entity target = entities.get(0);
            PersistentDataContainer container = target.getPersistentDataContainer();
            container.set(keys.wasBruteKey, PersistentDataType.BYTE, (byte) (entity instanceof Piglin ? 0 : 1));
        }
    }
}
