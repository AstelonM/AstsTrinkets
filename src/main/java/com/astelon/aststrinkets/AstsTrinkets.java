package com.astelon.aststrinkets;

import com.astelon.aststrinkets.api.TrinketApi;
import com.astelon.aststrinkets.commands.TrinketCommand;
import com.astelon.aststrinkets.listeners.*;
import com.astelon.aststrinkets.managers.*;
import com.astelon.aststrinkets.trinkets.*;
import com.astelon.aststrinkets.trinkets.block.InfinityItem;
import com.astelon.aststrinkets.trinkets.block.MysteryCake;
import com.astelon.aststrinkets.trinkets.block.Terrarium;
import com.astelon.aststrinkets.trinkets.block.VoidGateway;
import com.astelon.aststrinkets.trinkets.creature.*;
import com.astelon.aststrinkets.trinkets.inventory.ArcaneTome;
import com.astelon.aststrinkets.trinkets.projectile.ExperienceBottle;
import com.astelon.aststrinkets.trinkets.projectile.MysteryEgg;
import com.astelon.aststrinkets.trinkets.projectile.ProjectileTrinket;
import com.astelon.aststrinkets.trinkets.projectile.arrow.*;
import com.astelon.aststrinkets.trinkets.projectile.rocket.*;
import com.astelon.aststrinkets.utils.MaxLevelBehaviour;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

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
        pluginManager.registerEvents(new PlayerDeathListener(this, trinketManager), this);
        pluginManager.registerEvents(new LeashListener(this, trinketManager), this);
        pluginManager.registerEvents(new TeleportationListener(this, trinketManager), this);
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
            if (trinket instanceof CreatureAffectingTrinket creatureAffectingTrinket) {
                creatureAffectingTrinket.setPetOwnerOnly(configuration.getBoolean(trinket.getName() + ".petOwnerOnly"));
            }
            if (trinket instanceof ProjectileTrinket projectileTrinket) {
                projectileTrinket.setDispenserAllowed(configuration.getBoolean(trinket.getName() + ".dispenserAllowed"));
                if (projectileTrinket instanceof ArrowTrinket arrowTrinket) {
                    arrowTrinket.setMultishotAllowed(configuration.getBoolean(trinket.getName() + ".multishotAllowed"));
                    arrowTrinket.setPiercingAllowed(configuration.getBoolean(trinket.getName() + ".piercingAllowed"));
                }
            }
            if (trinket instanceof WeatherRocketTrinket weatherRocketTrinket) {
                weatherRocketTrinket.setMinWeatherDuration(Utils.ensurePositive(configuration.getInt(weatherRocketTrinket.getName() +
                        ".minWeatherDuration"), 600));
                weatherRocketTrinket.setMaxWeatherDuration(Utils.ensurePositive(configuration.getInt(weatherRocketTrinket.getName() +
                        ".maxWeatherDuration"), 1200));
            }
            if (trinket instanceof GolemBlueprintTrinket golemTrinket) {
                List<String> allowedHeads = configuration.getStringList(golemTrinket.getName() + ".allowedHeads");
                Set<Material> allowedHeadMaterials;
                if (allowedHeads.isEmpty()) {
                    allowedHeadMaterials = Set.of(Material.PUMPKIN, Material.CARVED_PUMPKIN, Material.JACK_O_LANTERN);
                } else {
                    try {
                        allowedHeadMaterials = allowedHeads.stream().map(head -> Material.valueOf(head.toUpperCase())).collect(Collectors.toSet());
                    } catch (IllegalArgumentException e) {
                        getLogger().warning("One or more of the head materials for the Snow Golem Blueprint are invalid.");
                        allowedHeadMaterials = Set.of();
                    }
                }
                golemTrinket.setAllowedHeads(allowedHeadMaterials);
            }
            if (trinket instanceof RandomTeleportationTrinket teleportationTrinket) {
                teleportationTrinket.setUseWorldDefault(configuration.getBoolean(teleportationTrinket.getName() +
                        ".useWorldDefault", true));
                teleportationTrinket.setSurfaceOnly(configuration.getBoolean(teleportationTrinket.getName() +
                        ".surfaceOnly", true));
                teleportationTrinket.setMinX(configuration.getInt(teleportationTrinket.getName() + ".minX"));
                teleportationTrinket.setMaxX(configuration.getInt(teleportationTrinket.getName() + ".maxX"));
                teleportationTrinket.setMinY(configuration.getInt(teleportationTrinket.getName() + ".minY"));
                teleportationTrinket.setMaxY(configuration.getInt(teleportationTrinket.getName() + ".maxY"));
                teleportationTrinket.setMinZ(configuration.getInt(teleportationTrinket.getName() + ".minZ"));
                teleportationTrinket.setMaxZ(configuration.getInt(teleportationTrinket.getName() + ".maxZ"));
            }
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
        ReignitableRocketPrototype reignitableRocketPrototype = trinketManager.getReignitableRocketPrototype();
        double failureChancePrototype = configuration.getDouble(reignitableRocketPrototype.getName() + ".failureChance", 33.33);
        double criticalFailureChance = configuration.getDouble(reignitableRocketPrototype.getName() + ".criticalFailureChance", 1.0);
        boolean pluginExplosion = configuration.getBoolean(reignitableRocketPrototype.getName() + ".pluginExplosion");
        reignitableRocketPrototype.setFailures(Utils.ensurePercentage(failureChancePrototype, 33.33),
                Utils.ensurePercentage(criticalFailureChance, 1.0));
        reignitableRocketPrototype.setAllowUseAsFirework(configuration.getBoolean(reignitableRocketPrototype.getName() +
                ".allowUseAsFirework", false));
        reignitableRocketPrototype.setPluginExplosion(pluginExplosion);
        reignitableRocketPrototype.setExplosionPower((float) configuration.getDouble(reignitableRocketPrototype.getName() +
                ".explosionPower"));
        reignitableRocketPrototype.setSetFire(configuration.getBoolean(reignitableRocketPrototype.getName() + ".setFire"));
        reignitableRocketPrototype.setBreakBlocks(configuration.getBoolean(reignitableRocketPrototype.getName() + ".breakBlocks"));
        ReignitableRocket reignitableRocket = trinketManager.getReignitableRocket();
        double failureChance = configuration.getDouble(reignitableRocket.getName() + ".failureChance", 10.0);
        reignitableRocket.setFailureChance(Utils.ensurePercentage(failureChance, 10.0));
        reignitableRocket.setAllowUseAsFirework(configuration.getBoolean(reignitableRocket.getName() +
                ".allowUseAsFirework", false));
        PerfectedReignitableRocket perfectedReignitableRocket = trinketManager.getPerfectedReignitableRocket();
        perfectedReignitableRocket.setAllowUseAsFirework(configuration.getBoolean(perfectedReignitableRocket.getName() +
                ".allowUseAsFirework", false));
        Souleater souleater = trinketManager.getSouleater();
        int cooldown = configuration.getInt(souleater.getName() + ".cooldown", 60) * 1000;
        souleater.setCooldown(Utils.ensurePositive(cooldown, 60000));
        double eatChance = Utils.ensurePercentage(configuration.getDouble(souleater.getName() + ".useChance", 1.0),
                1.0);
        souleater.setUseChance(Utils.normalizeRate(eatChance));
        MysteryEgg mysteryEgg = trinketManager.getMysteryEgg();
        List<String> blacklist = configuration.getStringList(mysteryEgg.getName() + ".blacklist");
        mysteryEgg.setAllowedEntities(getBlacklistedTypes(blacklist));
        Bait bait = trinketManager.getBait();
        double efficiency = Utils.ensurePercentage(configuration.getDouble(bait.getName() + ".efficiency", 50.0),
                50.0);
        bait.setEfficiency(Utils.normalizeRate(efficiency));
        double consumeChance = Utils.ensurePercentage(configuration.getDouble(bait.getName() + ".consumeChance", 10.0),
                10.0);
        bait.setConsumeChance(Utils.normalizeRate(consumeChance));
        ExperienceBottle experienceBottle = trinketManager.getExperienceBottle();
        ItemMagnet itemMagnet = trinketManager.getItemMagnet();
        int itemMagnetRange = Utils.ensurePositive(configuration.getInt(itemMagnet.getName() + ".range", 4), 4);
        itemMagnet.setRange(itemMagnetRange);
        Terrarium terrarium = trinketManager.getTerrarium();
        terrarium.setAllowEnderDragonCapture(configuration.getBoolean(terrarium.getName() + ".allowEnderDragonCapture"));
        ExplosiveArrow explosiveArrow = trinketManager.getExplosiveArrow();
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
        Die die = trinketManager.getDie();
        die.setSides(Utils.ensurePositive(configuration.getInt(die.getName() + ".sides"), 6));
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
        HuntingBow huntingBow = trinketManager.getHuntingBow();
        huntingBow.setLootingLevel(Utils.ensurePositive(configuration.getInt(huntingBow.getName() + ".lootingLevel", 3), 3));
        huntingBow.setLuckLevel(Utils.ensurePositive(configuration.getInt(huntingBow.getName() + ".luckLevel", 0), 0));
        MagicBerries magicBerries = trinketManager.getMagicBerries();
        magicBerries.setAllowCreepers(configuration.getBoolean(magicBerries.getName() + ".allowCreepers", true));
        magicBerries.setAllowFoxes(configuration.getBoolean(magicBerries.getName() + ".allowFoxes", true));
        magicBerries.setAllowGoats(configuration.getBoolean(magicBerries.getName() + ".allowGoats", true));
        magicBerries.setAllowMooshrooms(configuration.getBoolean(magicBerries.getName() + ".allowMooshrooms", true));
        magicBerries.setAllowPandas(configuration.getBoolean(magicBerries.getName() + ".allowPandas", true));
        magicBerries.setAllowKillerBunnies(configuration.getBoolean(magicBerries.getName() + ".allowKillerBunnies", true));
        magicBerries.setAllowVillagers(configuration.getBoolean(magicBerries.getName() + ".allowVillagers", true));
        magicBerries.setAllowZombieVillagers(configuration.getBoolean(magicBerries.getName() + ".allowZombieVillagers", true));
        readArcaneTomeConfig(configuration);
        HoldingBundle holdingBundle = trinketManager.getHoldingBundle();
        holdingBundle.setAllowVirtualInventories(configuration.getBoolean(holdingBundle.getName() + ".allowVirtualInventories",
                false));
        VoidGateway voidGateway = trinketManager.getVoidGateway();
        voidGateway.setDefaultX(configuration.getInt(voidGateway.getName() + ".defaultX", 0));
        voidGateway.setDefaultY(configuration.getInt(voidGateway.getName() + ".defaultY", 64));
        voidGateway.setDefaultZ(configuration.getInt(voidGateway.getName() + ".defaultZ", 0));
        voidGateway.setAllowEntities(configuration.getBoolean(voidGateway.getName() + ".allowEntities", false));
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

    private void readArcaneTomeConfig(FileConfiguration configuration) {
        // Split for now
        ArcaneTome arcaneTome = trinketManager.getArcaneTome();
        MaxLevelBehaviour behaviour = MaxLevelBehaviour.getBehaviourByName(
                configuration.getString(arcaneTome.getName() + ".behaviour"));
        if (behaviour == null) {
            getLogger().warning("Field behaviour in " + arcaneTome.getName() + " is invalid. Disabling the trinket.");
            arcaneTome.setEnabled(false);
        }
        int basicMaxLevelIncrease = Utils.ensurePositive(configuration.getInt(arcaneTome.getName() + ".maxLevelIncrease"), 0);
        ConfigurationSection customMaxLevelsSection = configuration.getConfigurationSection(arcaneTome.getName() + ".customMaxLevels");
        Map<Enchantment, Integer> customMaxLevels = new HashMap<>();
        if (customMaxLevelsSection != null) {
            for (String key : customMaxLevelsSection.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(key.toLowerCase()));
                if (enchantment == null) {
                    getLogger().warning("Enchantment + " + key + " in " + arcaneTome.getName() + " is invalid. " +
                            "Disabling the trinket.");
                    arcaneTome.setEnabled(false);
                } else {
                    int level = customMaxLevelsSection.getInt(key, -1);
                    if (level == -1) {
                        getLogger().warning("Enchantment + " + key + " in " + arcaneTome.getName() + " has an invalid level. " +
                                "Disabling the trinket.");
                        arcaneTome.setEnabled(false);
                    } else {
                        customMaxLevels.put(enchantment, level);
                    }
                }
            }
        }
        arcaneTome.setMaxLevelBehaviour(behaviour);
        arcaneTome.setBasicMaxLevelIncrease(basicMaxLevelIncrease);
        arcaneTome.setCustomMaxLevels(customMaxLevels);
    }
}
