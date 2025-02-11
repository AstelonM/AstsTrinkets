package com.astelon.aststrinkets;

public enum Power {

    INVISIBILITY("invisibility"),
    TRUE_INVISIBILITY("trueInvisibility"),
    SHAPE_SHIFTING("shapeShifting"),
    RANDOM_POTION_EFFECT("randomPotionEffect"),
    PLACE_WEBS("placeWebs"),
    MENDING("mending"),
    BINDING("binding"),
    TRUE_SIGHT("trueSight"),
    ABSORB_MENDING("absorbMending"),
    APPLY_MENDING("applyMending"),
    YOUTH("youth"),
    CAPTURE_ENTITIES("captureEntities"),
    CAPTURE_ANIMALS("captureAnimals"),
    CAPTURE_MONSTERS("captureMonsters"),
    STRONGER_CAPTURE_ENTITIES("strongerCaptureEntities"),
    INFINITE_PLACED_BLOCKS("infinitePlacedBlocks"),
    UNBINDING("unbinding"),
    REIGNITION_PROTOTYPE("reignitionPrototype"),
    REIGNITION("reignition"),
    PERFECTED_REIGNITION("perfectedReignition"),
    DEATH("death"),
    TRUE_DEATH("trueDeath"),
    STORE_SHULKER_BOXES("storeShulkerBoxes"),
    PREVENT_SUFFOCATION("preventSuffocation"),
    PREVENT_FALL_DAMAGE("preventFallDamage"),
    INVULNERABILITY("invulnerability"),
    CONSUME_ENCHANTMENTS("consumeEnchantments"),
    SPAWN_RANDOM_CREATURES("spawnRandomCreatures"),
    SPEED_UP_FISHING("speedUpFishing"),
    ABSORB_EXPERIENCE("absorbExperience"),
    SENTIENT_AXE("sentientAxe"),
    RUN_COMMANDS("runCommands"),
    CREATE_GATEWAYS("createGateways"),
    CREATE_BUDDING_AMETHYSTS("createBuddingAmethysts"),
    ATTRACT_ITEMS("attractItems"),
    CREATE_SPAWNERS("createSpawners"),
    SUMMON_LIGHTNING("summonLightning"),
    CREATE_EXPLOSION("createExplosion"),
    PREVENT_FIRE_DAMAGE("preventFireDamage"),
    TELEPORT_BACK_WITHOUT_ITEMS("teleportBackWithoutItems"),
    TELEPORT_BACK("teleportBack"),
    INFINITE_ITEMS_OF_A_KIND_STORED("infiniteItemsOfAKindStored"),
    TRINKET_IMMUNITY("trinketImmunity"),
    TRINKET_VULNERABILITY("trinketVulnerability"),
    IMPROVE_RANDOM_ENCHANTMENT("improveRandomEnchantment"),
    ATTRACT_PLAYERS("attractPlayers"),
    MAKE_ARMOUR_UNBREAKABLE("makeArmourUnbreakable"),
    RESTORE_FULL_HEALTH("restoreFullHealth"),
    INVINCIBILITY("invincibility"),
    ABSORB_HEALTH_ON_HIT("absorbHealthOnHit"),
    RANDOM_FIREWORK("randomFirework"),
    NIGHT_VISION("nightVision"),
    CREATE_RAIN("createRain"),
    SELECT_RANDOM_NUMBER_WHEN_THROWN("selectRandomNumberWhenThrown"),
    RANDOM_TELEPORTATION("randomTeleportation"),
    REUSABLE_RANDOM_TELEPORTATION("reusableRandomTeleportation"),
    NETHER_ZOMBIFICATION_CURE("netherZombiFicationCure"),
    MAKE_ITEM_FRAMES_INVISIBLE("makeItemFramesInvisible"),
    VIRUS("virus"),
    OXIDIZE_COPPER("oxidizeCopper"),
    DOLPHIN_GRACE("dolphinGrace"),
    NETHER_ZOMBIFICATION_REVERSAL("netherZombificationReversal"),
    BUILD_SNOW_GOLEM_FROM_INVENTORY("buildSnowGolemFromInventory"),
    BUILD_SNOW_GOLEM("buildSnowGolem"),
    REMOVE_INVULNERABILITY("removeInvulnerability"),
    MAKE_ADULT("makeAdult"),
    ETERNAL_YOUTH("eternalYouth"),
    TURN_COWS_INTO_MOOSHROOMS("turnCowsIntoMooshrooms"),;

    private final String powerName;

    Power(String powerName) {
        this.powerName = powerName;
    }

    public String powerName() {
        return powerName;
    }
}
