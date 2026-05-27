package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.ReusableExperienceBottle;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.trinkets.equipable.*;
import com.astelon.aststrinkets.trinkets.inventory.BindingPowder;
import com.astelon.aststrinkets.trinkets.projectile.potion.AgeingPotion;
import com.astelon.aststrinkets.trinkets.projectile.potion.LovePotion;
import com.astelon.aststrinkets.utils.Utils;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

//TODO merge player join/quit in one listener, rest in their own, maybe
public class PotionListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final MobInfoManager mobInfoManager;
    private final NightVisionGoggles nightVisionGoggles;
    private final Flippers flippers;
    private final AdvancedFlippers advancedFlippers;
    private final ReusableExperienceBottle reusableExperienceBottle;
    private final BindingPowder bindingPowder;
    private final FrogLegs frogLegs;
    private final AgeingPotion ageingPotion;
    private final LovePotion lovePotion;

    public PotionListener(AstsTrinkets plugin, TrinketManager trinketManager, MobInfoManager mobInfoManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        this.mobInfoManager = mobInfoManager;
        nightVisionGoggles = trinketManager.getNightVisionGoggles();
        flippers = trinketManager.getFlippers();
        advancedFlippers = trinketManager.getAdvancedFlippers();
        reusableExperienceBottle = trinketManager.getReusableExperienceBottle();
        bindingPowder = trinketManager.getBindingPowder();
        frogLegs = trinketManager.getFrogLegs();
        ageingPotion = trinketManager.getAgeingPotion();
        lovePotion = trinketManager.getLovePotion();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
        addEffectsOnJoin(helmet, player);
        addEffectsOnJoin(chestplate, player);
        addEffectsOnJoin(leggings, player);
        addEffectsOnJoin(boots, player);
    }

    private void addEffectsOnJoin(ItemStack itemStack, Player player) {
        Trinket trinket = trinketManager.getTrinket(itemStack);
        if (trinket instanceof EffectGivingTrinket effectGivingTrinket) {
            if (trinket.isEnabled() && trinketManager.isOwnedBy(itemStack, player))
                effectGivingTrinket.addEffects(player);
        }
    }

    @EventHandler
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;
        checkTrinketOnAdd(event, player, nightVisionGoggles);
        checkTrinketOnAdd(event, player, flippers);
        checkTrinketOnAdd(event, player, advancedFlippers);
        checkTrinketOnAdd(event, player, frogLegs);
    }

    private void checkTrinketOnAdd(EntityPotionEffectEvent event, Player player, EffectGivingTrinket effectGivingTrinket) {
        if (effectGivingTrinket.isEnabled()) {
            if (effectGivingTrinket.isEffect(event.getOldEffect()) && event.getAction() != EntityPotionEffectEvent.Action.ADDED) {
                ItemStack helmet = player.getInventory().getHelmet();
                ItemStack chestplate = player.getInventory().getChestplate();
                ItemStack leggings = player.getInventory().getLeggings();
                ItemStack boots = player.getInventory().getBoots();
                checkEffectOnAdd(helmet, player, event, effectGivingTrinket);
                checkEffectOnAdd(chestplate, player, event, effectGivingTrinket);
                checkEffectOnAdd(leggings, player, event, effectGivingTrinket);
                checkEffectOnAdd(boots, player, event, effectGivingTrinket);
            }
        }
    }

    private void checkEffectOnAdd(ItemStack itemStack, Player player, EntityPotionEffectEvent event,
                                  EffectGivingTrinket effectGivingTrinket) {
        if (effectGivingTrinket.isEnabledTrinket(itemStack) && trinketManager.isOwnedBy(itemStack, player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        ItemStack oldItem = event.getOldItem();
        ItemStack newItem = event.getNewItem();
        Player player = event.getPlayer();
        checkArmourChange(oldItem, newItem, player, nightVisionGoggles);
        checkArmourChange(oldItem, newItem, player, flippers);
        checkArmourChange(oldItem, newItem, player, advancedFlippers);
        checkArmourChange(oldItem, newItem, player, frogLegs);
    }

    private void checkArmourChange(ItemStack oldItem, ItemStack newItem, Player player, EffectGivingTrinket trinket) {
        if (trinket.isEnabledTrinket(oldItem) && trinketManager.isOwnedBy(oldItem, player)) {
            if ((!trinket.isEnabledTrinket(newItem) || !trinketManager.isOwnedBy(newItem, player)) &&
                    trinket.hasEffects(player)) {
                trinket.removeEffects(player);
            }
        } else if (trinket.isEnabledTrinket(newItem) && trinketManager.isOwnedBy(newItem, player)) {
            trinket.addEffects(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
        removeEffectOnLeave(helmet, player);
        removeEffectOnLeave(chestplate, player);
        removeEffectOnLeave(leggings, player);
        removeEffectOnLeave(boots, player);
    }

    private void removeEffectOnLeave(ItemStack itemStack, Player player) {
        Trinket trinket = trinketManager.getTrinket(itemStack);
        if (trinket instanceof EffectGivingTrinket effectTrinket && effectTrinket.isEnabledTrinket(itemStack) &&
                trinketManager.isOwnedBy(itemStack, player) && effectTrinket.hasEffects(player)) {
            effectTrinket.removeEffects(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        ItemStack itemStack = event.getItem();
        Player player = event.getPlayer();
        if (reusableExperienceBottle.isTrinket(itemStack)) {
            if (!reusableExperienceBottle.isEnabled() || !trinketManager.isOwnedBy(itemStack, player)) {
                event.setCancelled(true);
                return;
            }
            int experience = reusableExperienceBottle.getExperience(itemStack);
            player.giveExp(experience, true);
            ItemStack result = reusableExperienceBottle.getItemStack();
            if (trinketManager.getOwner(itemStack) != null)
                result = bindingPowder.bindTrinket(result, reusableExperienceBottle, player);
            event.setReplacement(result);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();
        if (ageingPotion.isEnabledTrinket(potion)) {
            Collection<LivingEntity> targets = event.getAffectedEntities();
            for (LivingEntity target : targets) {
                if (target instanceof Ageable ageable) {
                    double intensity = event.getIntensity(ageable);
                    int ticks = (int) (Utils.TICKS_TO_ADULTHOOD * intensity);
                    ageable.setAge(ageable.getAge() + ticks);
                }
            }
            if (!targets.isEmpty()) {
                logSplashPotion(targets, potion, "An ageing potion");
            }
        } else if (lovePotion.isEnabledTrinket(potion)) {
            Collection<LivingEntity> targets = event.getAffectedEntities();
            for (LivingEntity target : targets) {
                if (target instanceof Animals animal) {
                    double intensity = event.getIntensity(animal);
                    int ticks = (int) (lovePotion.getLoveTime() * intensity);
                    animal.setLoveModeTicks(ticks);
                    //TODO EntityEnterLoveModeEvent?
                }
            }
            if (!targets.isEmpty()) {
                logSplashPotion(targets, potion, "A love potion");
            }
        }
    }

    private void logSplashPotion(Collection<LivingEntity> targets, ThrownPotion potion, String potionName) {
        Map<String, Integer> affectedCount = targets.stream()
                .collect(Collectors.toMap(mobInfoManager::getTypeAndName, entity -> 1, Integer::sum));
        String affected = affectedCount.entrySet().stream()
                .map(entry -> entry.getValue() + " " + entry.getKey())
                .collect(Collectors.joining(", "));
        ProjectileSource source = potion.getShooter();
        String sourceName = source instanceof Player player ? "player " + player.getName() :
                (source instanceof BlockProjectileSource ? "a dispenser" : mobInfoManager.getTypeAndName((LivingEntity) source));
        String location = Utils.serializeCoordsLogging(potion.getLocation());
        plugin.getLogger().info(potionName + " thrown by " + sourceName + " landed at " + location + " and affected " +
                affected + ".");
    }
}
