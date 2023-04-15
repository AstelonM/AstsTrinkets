package com.astelon.aststrinkets;

import com.astelon.aststrinkets.commands.TrinketCommand;
import com.astelon.aststrinkets.listeners.*;
import com.astelon.aststrinkets.managers.CakeManager;
import com.astelon.aststrinkets.managers.InvisibilityManager;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.*;
import com.astelon.aststrinkets.trinkets.block.InfinityItem;
import com.astelon.aststrinkets.trinkets.block.MysteryCake;
import com.astelon.aststrinkets.trinkets.creature.*;
import com.astelon.aststrinkets.trinkets.creature.traps.AmethystTrap;
import com.astelon.aststrinkets.trinkets.creature.traps.DiamondTrap;
import com.astelon.aststrinkets.trinkets.creature.traps.EmeraldTrap;
import com.astelon.aststrinkets.trinkets.creature.traps.NetherStarTrap;
import com.astelon.aststrinkets.trinkets.projectile.MysteryEgg;
import com.astelon.aststrinkets.trinkets.rocket.ReignitableRocket;
import com.astelon.aststrinkets.trinkets.rocket.ReignitableRocketPrototype;
import com.astelon.aststrinkets.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class AstsTrinkets extends JavaPlugin {

    private TrinketManager trinketManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        MobInfoManager mobInfoManager = new MobInfoManager();
        InvisibilityManager invisibilityManager = new InvisibilityManager(this);
        CakeManager cakeManager = new CakeManager(this);
        cakeManager.init();
        trinketManager = new TrinketManager(this, mobInfoManager, invisibilityManager);
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new InvisibilityListener(this, trinketManager, invisibilityManager), this);
        pluginManager.registerEvents(new ShapeShifterListener(this, trinketManager), this);
        pluginManager.registerEvents(new CakeListener(this, trinketManager, cakeManager), this);
        pluginManager.registerEvents(new BlockListener(this, trinketManager), this);
        pluginManager.registerEvents(new InventoryUseListener(this, trinketManager), this);
        pluginManager.registerEvents(new PlayerInteractListener(this, mobInfoManager, trinketManager), this);
        pluginManager.registerEvents(new FireworkListener(this, trinketManager), this);
        pluginManager.registerEvents(new ArrowListener(this, trinketManager), this);
        pluginManager.registerEvents(new EntityDamageListener(this, trinketManager, mobInfoManager), this);
        pluginManager.registerEvents(new GrindstoneListener(trinketManager), this);
        pluginManager.registerEvents(new ProjectileListener(this, trinketManager, mobInfoManager), this);
        loadConfig();
        Objects.requireNonNull(getCommand("trinkets")).setExecutor(new TrinketCommand(this, trinketManager));
    }

    public void reload() {
        reloadConfig();
        loadConfig();
    }

    private void loadConfig() {
        FileConfiguration configuration = getConfig();
        for (Trinket trinket: trinketManager.getTrinkets()) {
            trinket.setEnabled(configuration.getBoolean(trinket.getName() + ".enabled"));
        }
        ShapeShifter shapeShifter = trinketManager.getShapeShifter();
        shapeShifter.removeItems(configuration.getStringList(shapeShifter.getName() + ".itemBlacklist"));
        MysteryCake mysteryCake = trinketManager.getMysteryCake();
        mysteryCake.setCheckHealth(configuration.getBoolean(mysteryCake.getName() + ".checkHealth"));
        InfinityItem infinityItem = trinketManager.getInfinityItem();
        List<Material> allowedBlocks = configuration.getStringList(infinityItem.getName() + ".allowedBlocks")
                .stream().map(Material::valueOf).filter(Material::isBlock).filter(Material::isItem).toList();
        infinityItem.setAllowedBlocks(allowedBlocks);
        YouthMilk youthMilk = trinketManager.getYouthMilk();
        youthMilk.setPetOwnerOnly(configuration.getBoolean(youthMilk.getName() + ".petOwnerOnly"));
        DiamondTrap diamondTrap = trinketManager.getDiamondTrap();
        diamondTrap.setPetOwnerOnly(configuration.getBoolean(diamondTrap.getName() + ".petOwnerOnly"));
        EmeraldTrap emeraldTrap = trinketManager.getEmeraldTrap();
        emeraldTrap.setPetOwnerOnly(configuration.getBoolean(emeraldTrap.getName() + ".petOwnerOnly"));
        AmethystTrap amethystTrap = trinketManager.getAmethystTrap();
        amethystTrap.setPetOwnerOnly(configuration.getBoolean(amethystTrap.getName() + ".petOwnerOnly"));
        NetherStarTrap netherStarTrap = trinketManager.getNetherStarTrap();
        netherStarTrap.setPetOwnerOnly(configuration.getBoolean(netherStarTrap.getName() + ".petOwnerOnly"));
        ReignitableRocketPrototype reignitableRocketPrototype = trinketManager.getReignitableRocketPrototype();
        double failureChancePrototype = configuration.getDouble(reignitableRocketPrototype.getName() + ".failureChance", 33.33);
        double criticalFailureChance = configuration.getDouble(reignitableRocketPrototype.getName() + ".criticalFailureChance", 1.0);
        boolean pluginExplosion = configuration.getBoolean(reignitableRocketPrototype.getName() + ".pluginExplosion");
        reignitableRocketPrototype.setFailures(Utils.ensurePercentage(failureChancePrototype, 33.33),
                Utils.ensurePercentage(criticalFailureChance, 1.0));
        reignitableRocketPrototype.setPluginExplosion(pluginExplosion);
        ReignitableRocket reignitableRocket = trinketManager.getReignitableRocket();
        double failureChance = configuration.getDouble(reignitableRocket.getName() + ".failureChance", 10.0);
        reignitableRocket.setFailureChance(Utils.ensurePercentage(failureChance, 10.0));
        Souleater souleater = trinketManager.getSouleater();
        int cooldown = configuration.getInt(souleater.getName() + ".cooldown", 60) * 1000;
        souleater.setCooldown(Utils.ensurePositive(cooldown, 60000));
        double eatChance = Utils.ensurePercentage(configuration.getDouble(souleater.getName() + ".eatChance", 1.0),
                1.0);
        souleater.setEatChance(Utils.normalizeRate(eatChance));
        MysteryEgg mysteryEgg = trinketManager.getMysteryEgg();
        List<String> blacklist = configuration.getStringList(mysteryEgg.getName() + ".blacklist");
        mysteryEgg.setAllowedEntities(getBlacklistedTypes(blacklist));
    }

    private HashSet<EntityType> getBlacklistedTypes(List<String> blacklist) {
        HashSet<EntityType> result = new HashSet<>();
        for (String typeName: blacklist) {
            try {
                result.add(EntityType.valueOf(typeName));
            } catch (IllegalArgumentException e) {
                getLogger().warning("Invalid entity type " + typeName + " in the Mystery Egg blacklist.");
            }
        }
        return result;
    }
}
