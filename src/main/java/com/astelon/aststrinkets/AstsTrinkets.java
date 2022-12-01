package com.astelon.aststrinkets;

import com.astelon.aststrinkets.commands.TrinketCommand;
import com.astelon.aststrinkets.listeners.*;
import com.astelon.aststrinkets.managers.CakeManager;
import com.astelon.aststrinkets.managers.InvisibilityManager;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
        pluginManager.registerEvents(new EntityDamageListener(this, trinketManager), this);
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
        reignitableRocketPrototype.setFailures(ensurePercentage(failureChancePrototype, 33.33),
                ensurePercentage(criticalFailureChance, 1.0));
        ReignitableRocket reignitableRocket = trinketManager.getReignitableRocket();
        double failureChance = configuration.getDouble(reignitableRocket.getName() + ".failureChance", 10.0);
        reignitableRocket.setFailureChance(ensurePercentage(failureChance, 10.0));
    }

    private double ensurePercentage(double source, double defaultPercentage) {
        if (source < 0 || source > 100)
            return defaultPercentage;
        return source;
    }
}
