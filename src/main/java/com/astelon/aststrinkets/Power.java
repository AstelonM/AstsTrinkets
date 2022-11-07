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
    STRONGER_CAPTURE_ENTITIES("strongerCaptureEntities");

    private final String powerName;

    Power(String powerName) {
        this.powerName = powerName;
    }

    public String powerName() {
        return powerName;
    }
}
