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
    INVULNERABILTY("invulnerability"),
    CONSUME_ENCHANTMENTS("consumeEnchantments"),
    SPAWN_RANDOM_CREATURES("spawnRandomCreatures"),
    SPEED_UP_FISHING("speedUpFishing"),
    ABSORB_EXPERIENCE("absorbExperience"),
    SENTIENT_AXE("sentientAxe"),
    RUN_COMMANDS("runCommands"),
    CREATE_GATEWAYS("createGateways"),
    CREATE_BUDDING_AMETHYSTS("createBuddingAmethysts"),
    ATTRACT_ITEMS("attractItems"),
    CREATE_SPAWNERS("createSpawners");

    private final String powerName;

    Power(String powerName) {
        this.powerName = powerName;
    }

    public String powerName() {
        return powerName;
    }
}
