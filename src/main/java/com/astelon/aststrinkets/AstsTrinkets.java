package com.astelon.aststrinkets;

import com.astelon.aststrinkets.commands.TrinketCommand;
import com.astelon.aststrinkets.listeners.*;
import com.astelon.aststrinkets.managers.CakeManager;
import com.astelon.aststrinkets.managers.InvisibilityManager;
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
        InvisibilityManager invisibilityManager = new InvisibilityManager(this);
        CakeManager cakeManager = new CakeManager(this);
        cakeManager.init();
        trinketManager = new TrinketManager(this, invisibilityManager);
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new InvisibilityListener(this, trinketManager, invisibilityManager), this);
        pluginManager.registerEvents(new ShapeShifterListener(this, trinketManager), this);
        pluginManager.registerEvents(new CakeListener(this, trinketManager, cakeManager), this);
        pluginManager.registerEvents(new BlockListener(this, trinketManager), this);
        pluginManager.registerEvents(new InventoryUseListener(this, trinketManager), this);
        pluginManager.registerEvents(new PlayerInteractListener(this, trinketManager), this);
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
    }
}
