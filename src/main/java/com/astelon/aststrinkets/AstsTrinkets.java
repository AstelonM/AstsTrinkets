package com.astelon.aststrinkets;

import com.astelon.aststrinkets.api.TrinketApi;
import com.astelon.aststrinkets.commands.TrinketCommand;
import com.astelon.aststrinkets.listeners.*;
import com.astelon.aststrinkets.managers.*;
import com.astelon.aststrinkets.trinkets.*;
import com.astelon.aststrinkets.trinkets.block.InfinityItem;
import com.astelon.aststrinkets.trinkets.block.MysteryCake;
import com.astelon.aststrinkets.trinkets.block.Terrarium;
import com.astelon.aststrinkets.trinkets.creature.*;
import com.astelon.aststrinkets.trinkets.creature.traps.AmethystTrap;
import com.astelon.aststrinkets.trinkets.creature.traps.DiamondTrap;
import com.astelon.aststrinkets.trinkets.creature.traps.EmeraldTrap;
import com.astelon.aststrinkets.trinkets.creature.traps.NetherStarTrap;
import com.astelon.aststrinkets.trinkets.projectile.ExperienceBottle;
import com.astelon.aststrinkets.trinkets.projectile.MysteryEgg;
import com.astelon.aststrinkets.trinkets.projectile.arrow.DeathArrow;
import com.astelon.aststrinkets.trinkets.projectile.arrow.ExplosiveArrow;
import com.astelon.aststrinkets.trinkets.projectile.arrow.SmitingArrow;
import com.astelon.aststrinkets.trinkets.projectile.arrow.TrueDeathArrow;
import com.astelon.aststrinkets.trinkets.projectile.rocket.*;
import com.astelon.aststrinkets.utils.NamespacedKeys;
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
    private TrinketApi trinketApi;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        MobInfoManager mobInfoManager = new MobInfoManager();
        InvisibilityManager invisibilityManager = new InvisibilityManager(this);
        CakeManager cakeManager = new CakeManager(this);
        cakeManager.init();
        SentientAxeMessageManager sentientAxeMessageManager = new SentientAxeMessageManager();
        NamespacedKeys keys = new NamespacedKeys(this);
        trinketManager = new TrinketManager(this, mobInfoManager, invisibilityManager, keys);
        trinketApi = new TrinketApi(trinketManager, keys, mobInfoManager);
        SentientAxeTaskManager sentientAxeTaskManager = new SentientAxeTaskManager(this, trinketManager,
                sentientAxeMessageManager);
        ZombieCuringManager zombieCuringManager = new ZombieCuringManager(this, trinketManager, mobInfoManager);
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new InvisibilityListener(this, trinketManager, invisibilityManager), this);
        pluginManager.registerEvents(new ShapeShifterListener(this, trinketManager), this);
        pluginManager.registerEvents(new CakeListener(this, trinketManager, cakeManager), this);
        pluginManager.registerEvents(new BlockListener(this, trinketManager, mobInfoManager), this);
        pluginManager.registerEvents(new InventoryCustomUseListener(this, trinketManager), this);
        pluginManager.registerEvents(new PlayerInteractListener(this, mobInfoManager, trinketManager), this);
        pluginManager.registerEvents(new FireworkListener(this, trinketManager), this);
        pluginManager.registerEvents(new EntityDamageListener(this, trinketManager, mobInfoManager), this);
        pluginManager.registerEvents(new TrinketLimitationsListener(trinketManager), this);
        pluginManager.registerEvents(new ProjectileListener(this, trinketManager, mobInfoManager), this);
        pluginManager.registerEvents(new FishingListener(this, trinketManager), this);
        pluginManager.registerEvents(new SentientAxeListener(this, trinketManager, sentientAxeMessageManager,
                sentientAxeTaskManager), this);
        pluginManager.registerEvents(new BookListener(this, trinketManager), this);
        pluginManager.registerEvents(new PotionListener(this, trinketManager), this);
        pluginManager.registerEvents(new ItemListener(this, trinketManager), this);
        pluginManager.registerEvents(new InventoryRegularUseListener(this, trinketManager), this);
        pluginManager.registerEvents(new VirusListener(this, trinketManager), this);
        pluginManager.registerEvents(new NetherZombieReversalListener(this, trinketManager, zombieCuringManager), this);
        loadConfig();
        Objects.requireNonNull(getCommand("trinkets")).setExecutor(new TrinketCommand(this, trinketManager));
    }

    public void reload() {
        reloadConfig();
        loadConfig();
    }

    public TrinketApi getTrinketApi() {
        return trinketApi;
    }

    private void loadConfig() {
        FileConfiguration configuration = getConfig();
        for (Trinket trinket: trinketManager.getTrinkets()) {
            trinket.setEnabled(configuration.getBoolean(trinket.getName() + ".enabled"));
        }
        getLogger().info("Loaded " + trinketManager.getTrinkets().size() + " trinkets.");
        ShapeShifter shapeShifter = trinketManager.getShapeShifter();
        shapeShifter.removeItems(configuration.getStringList(shapeShifter.getName() + ".itemBlacklist"));
        MysteryCake mysteryCake = trinketManager.getMysteryCake();
        mysteryCake.setCheckHealth(configuration.getBoolean(mysteryCake.getName() + ".checkHealth"));
        mysteryCake.setIgnoreBlockRestrictions(configuration.getBoolean(mysteryCake.getName() + ".ignoreBlockRestrictions"));
        mysteryCake.setUseLowestPriorityListener(configuration.getBoolean(mysteryCake.getName() + ".useLowestPriorityListener"));
        mysteryCake.setConsumeCakeEnabled(configuration.getBoolean(mysteryCake.getName() + ".consumeCakeEnabled"));
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
        reignitableRocketPrototype.setAllowUseAsFirework(configuration.getBoolean(reignitableRocketPrototype.getName() +
                ".allowUseAsFirework", false));
        reignitableRocketPrototype.setPluginExplosion(pluginExplosion);
        ReignitableRocket reignitableRocket = trinketManager.getReignitableRocket();
        double failureChance = configuration.getDouble(reignitableRocket.getName() + ".failureChance", 10.0);
        reignitableRocket.setFailureChance(Utils.ensurePercentage(failureChance, 10.0));
        reignitableRocket.setAllowUseAsFirework(configuration.getBoolean(reignitableRocket.getName() +
                ".allowUseAsFirework", false));
        PerfectedReignitableRocket perfectedReignitableRocket = trinketManager.getPerfectedReignitableRocket();
        perfectedReignitableRocket.setAllowUseAsFirework(configuration.getBoolean(perfectedReignitableRocket.getName() +
                ".allowUseAsFirework", false));
        DeathArrow deathArrow = trinketManager.getDeathArrow();
        deathArrow.setPiercingAllowed(configuration.getBoolean(deathArrow.getName() + ".piercingAllowed"));
        deathArrow.setMultishotAllowed(configuration.getBoolean(deathArrow.getName() + ".multishotAllowed"));
        deathArrow.setDispenserAllowed(configuration.getBoolean(deathArrow.getName() + ".dispenserAllowed"));
        TrueDeathArrow trueDeathArrow = trinketManager.getTrueDeathArrow();
        trueDeathArrow.setPiercingAllowed(configuration.getBoolean(trueDeathArrow.getName() + ".piercingAllowed"));
        trueDeathArrow.setMultishotAllowed(configuration.getBoolean(trueDeathArrow.getName() + ".multishotAllowed"));
        trueDeathArrow.setDispenserAllowed(configuration.getBoolean(trueDeathArrow.getName() + ".dispenserAllowed"));
        Souleater souleater = trinketManager.getSouleater();
        int cooldown = configuration.getInt(souleater.getName() + ".cooldown", 60) * 1000;
        souleater.setCooldown(Utils.ensurePositive(cooldown, 60000));
        double eatChance = Utils.ensurePercentage(configuration.getDouble(souleater.getName() + ".useChance", 1.0),
                1.0);
        souleater.setUseChance(Utils.normalizeRate(eatChance));
        MysteryEgg mysteryEgg = trinketManager.getMysteryEgg();
        List<String> blacklist = configuration.getStringList(mysteryEgg.getName() + ".blacklist");
        mysteryEgg.setAllowedEntities(getBlacklistedTypes(blacklist));
        mysteryEgg.setDispenserAllowed(configuration.getBoolean(mysteryEgg.getName() + ".dispenserAllowed"));
        Bait bait = trinketManager.getBait();
        double efficiency = Utils.ensurePercentage(configuration.getDouble(bait.getName() + ".efficiency", 50.0),
                50.0);
        bait.setEfficiency(Utils.normalizeRate(efficiency));
        double consumeChance = Utils.ensurePercentage(configuration.getDouble(bait.getName() + ".consumeChance", 10.0),
                10.0);
        bait.setConsumeChance(Utils.normalizeRate(consumeChance));
        ExperienceBottle experienceBottle = trinketManager.getExperienceBottle();
        experienceBottle.setDispenserAllowed(configuration.getBoolean(experienceBottle.getName() + ".dispenserAllowed"));
        ItemMagnet itemMagnet = trinketManager.getItemMagnet();
        int itemMagnetRange = Utils.ensurePositive(configuration.getInt(itemMagnet.getName() + ".range", 4), 4);
        itemMagnet.setRange(itemMagnetRange);
        Terrarium terrarium = trinketManager.getTerrarium();
        terrarium.setAllowEnderDragonCapture(configuration.getBoolean(terrarium.getName() + ".allowEnderDragonCapture"));
        terrarium.setPetOwnerOnly(configuration.getBoolean(terrarium.getName() + ".petOwnerOnly"));
        SmitingArrow smitingArrow = trinketManager.getSmitingArrow();
        smitingArrow.setPiercingAllowed(configuration.getBoolean(smitingArrow.getName() + ".piercingAllowed"));
        smitingArrow.setMultishotAllowed(configuration.getBoolean(smitingArrow.getName() + ".multishotAllowed"));
        smitingArrow.setDispenserAllowed(configuration.getBoolean(smitingArrow.getName() + ".dispenserAllowed"));
        ExplosiveArrow explosiveArrow = trinketManager.getExplosiveArrow();
        explosiveArrow.setPiercingAllowed(configuration.getBoolean(explosiveArrow.getName() + ".piercingAllowed"));
        explosiveArrow.setMultishotAllowed(configuration.getBoolean(explosiveArrow.getName() + ".multishotAllowed"));
        explosiveArrow.setDispenserAllowed(configuration.getBoolean(explosiveArrow.getName() + ".dispenserAllowed"));
        explosiveArrow.setExplosionPower((float) configuration.getDouble(explosiveArrow.getName() + ".explosionPower"));
        explosiveArrow.setSetFire(configuration.getBoolean(explosiveArrow.getName() + ".setFire"));
        explosiveArrow.setBreakBlocks(configuration.getBoolean(explosiveArrow.getName() + ".breakBlocks"));
        PlayerMagnet playerMagnet = trinketManager.getPlayerMagnet();
        int playerMagnetRange = Utils.ensurePositive(configuration.getInt(playerMagnet.getName() + ".range", 4), 4);
        playerMagnet.setRange(playerMagnetRange);
        VampiricSword vampiricSword = trinketManager.getVampiricSword();
        cooldown = configuration.getInt(vampiricSword.getName() + ".cooldown", 60) * 1000;
        vampiricSword.setCooldown(Utils.ensurePositive(cooldown, 60000));
        double useChance = Utils.ensurePercentage(configuration.getDouble(vampiricSword.getName() + ".useChance", 1.0),
                1.0);
        vampiricSword.setUseChance(Utils.normalizeRate(useChance));
        double percentageAbsorbed = Utils.ensurePercentage(configuration.getDouble(vampiricSword.getName() +
                ".percentageAbsorbed", 10.0), 10.0);
        vampiricSword.setPercentageAbsorbed(Utils.normalizeRate(percentageAbsorbed));
        MysteryFirework mysteryFirework = trinketManager.getMysteryFirework();
        mysteryFirework.setMinFlightDuration(Math.max(configuration.getInt(mysteryFirework.getName() +
                ".minFlightDuration", 1), 1));
        mysteryFirework.setMaxFlightDuration(Utils.ensurePositive(configuration.getInt(mysteryFirework.getName() +
                ".maxFlightDuration", 3), 3));
        mysteryFirework.setMinEffectAmount(Math.max(configuration.getInt(mysteryFirework.getName() +
                ".minEffectAmount", 1), 1));
        mysteryFirework.setMaxEffectAmount(Utils.ensurePositive(configuration.getInt(mysteryFirework.getName() +
                ".maxEffectAmount", 3), 3));
        mysteryFirework.setMinPrimaryColours(Math.max(configuration.getInt(mysteryFirework.getName() +
                ".minPrimaryColours", 1), 1));
        mysteryFirework.setMaxPrimaryColours(Utils.ensurePositive(configuration.getInt(mysteryFirework.getName() +
                ".maxPrimaryColours", 3), 3));
        mysteryFirework.setMinFadeColours(Math.max(configuration.getInt(mysteryFirework.getName() +
                ".minPrimaryColours", 0), 0));
        mysteryFirework.setMaxFadeColours(Utils.ensurePositive(configuration.getInt(mysteryFirework.getName() +
                ".maxPrimaryColours", 3), 3));
        mysteryFirework.setAllowCustomColours(configuration.getBoolean(mysteryFirework.getName() + ".allowCustomColours"));
        CloudSeeder cloudSeeder = trinketManager.getCloudSeeder();
        cloudSeeder.setMinWeatherDuration(Utils.ensurePositive(configuration.getInt(cloudSeeder.getName() +
                ".minWeatherDuration"), 600));
        cloudSeeder.setMaxWeatherDuration(Utils.ensurePositive(configuration.getInt(cloudSeeder.getName() +
                ".maxWeatherDuration"), 1200));
        Die die = trinketManager.getDie();
        die.setSides(Utils.ensurePositive(configuration.getInt(die.getName() + ".sides"), 6));
        MysteryShell mysteryShell = trinketManager.getMysteryShell();
        mysteryShell.setUseWorldDefault(configuration.getBoolean(mysteryShell.getName() + ".useWorldDefault", true));
        mysteryShell.setSurfaceOnly(configuration.getBoolean(mysteryShell.getName() + ".surfaceOnly", true));
        mysteryShell.setMinX(configuration.getInt(mysteryShell.getName() + ".minX"));
        mysteryShell.setMaxX(configuration.getInt(mysteryShell.getName() + ".maxX"));
        mysteryShell.setMinY(configuration.getInt(mysteryShell.getName() + ".minY"));
        mysteryShell.setMaxY(configuration.getInt(mysteryShell.getName() + ".maxY"));
        mysteryShell.setMinZ(configuration.getInt(mysteryShell.getName() + ".minZ"));
        mysteryShell.setMaxZ(configuration.getInt(mysteryShell.getName() + ".maxZ"));
        AbyssShell abyssShell = trinketManager.getAbyssShell();
        abyssShell.setUseWorldDefault(configuration.getBoolean(abyssShell.getName() + ".useWorldDefault", true));
        abyssShell.setSurfaceOnly(configuration.getBoolean(abyssShell.getName() + ".surfaceOnly", true));
        abyssShell.setMinX(configuration.getInt(abyssShell.getName() + ".minX"));
        abyssShell.setMaxX(configuration.getInt(abyssShell.getName() + ".maxX"));
        abyssShell.setMinY(configuration.getInt(abyssShell.getName() + ".minY"));
        abyssShell.setMaxY(configuration.getInt(abyssShell.getName() + ".maxY"));
        abyssShell.setMinZ(configuration.getInt(abyssShell.getName() + ".minZ"));
        abyssShell.setMaxZ(configuration.getInt(abyssShell.getName() + ".maxZ"));
        SpoiledEgg spoiledEgg = trinketManager.getSpoiledEgg();
        spoiledEgg.setInfectivity(Utils.normalizeRate(Utils
                .ensurePercentage(configuration.getDouble(spoiledEgg.getName() + ".infectivity"), 25.0)));
        spoiledEgg.setMinSpread(Utils.ensureBoundsInclusive(configuration.getInt(spoiledEgg.getName() + ".minSpread"), 1, 54));
        spoiledEgg.setMaxSpread(Utils.ensureBoundsInclusive(configuration.getInt(spoiledEgg.getName() + ".maxSpread"), 1, 54));
        spoiledEgg.setLethality(Utils.normalizeRate(Utils
                .ensurePercentage(configuration.getDouble(spoiledEgg.getName() + ".lethality"), 10.0)));
        CuringApple curingApple = trinketManager.getCuringApple();
        curingApple.setMinDuration(Utils.ensurePositive(configuration.getInt(curingApple.getName() + ".minDuration", 180),
                180));
        curingApple.setMaxDuration(Utils.ensurePositive(configuration.getInt(curingApple.getName() + ".maxDuration", 300),
                300));
        curingApple.setBruteChance(Utils.normalizeRate(
                Utils.ensurePercentage(configuration.getDouble(curingApple.getName() + ".bruteChance", 0.1), 0.1)));
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
