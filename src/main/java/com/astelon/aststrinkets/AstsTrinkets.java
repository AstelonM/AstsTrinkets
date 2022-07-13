package com.astelon.aststrinkets;

import com.astelon.aststrinkets.commands.TrinketCommand;
import com.astelon.aststrinkets.listeners.*;
import com.astelon.aststrinkets.managers.CakeManager;
import com.astelon.aststrinkets.managers.InvisibilityManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.MysteryCake;
import com.astelon.aststrinkets.trinkets.ShapeShifter;
import com.astelon.aststrinkets.trinkets.Trinket;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
        pluginManager.registerEvents(new SpinneretListener(this, trinketManager), this);
        pluginManager.registerEvents(new InventoryUseListener(this, trinketManager), this);
        loadConfig();
        Objects.requireNonNull(getCommand("trinkets")).setExecutor(new TrinketCommand(trinketManager));
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
    }
}
