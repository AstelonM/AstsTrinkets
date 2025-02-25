package com.astelon.aststrinkets.utils;

public enum MaxLevelBehaviour {
    DEFAULT("default"),
    BOUND_WITH_EXCEPTIONS("boundWithExceptions"),
    UNBOUND_WITH_EXCEPTIONS("unboundWithExceptions"),
    UNBOUND("unbound"),;

    private final String behaviourName;

    MaxLevelBehaviour(String behaviourName) {
        this.behaviourName = behaviourName;
    }

    public static MaxLevelBehaviour getBehaviourByName(String name) {
        for (MaxLevelBehaviour behaviour : values()) {
            if (behaviour.behaviourName.equalsIgnoreCase(name)) {
                return behaviour;
            }
        }
        return null;
    }

    public static MaxLevelBehaviour getBehaviourByName(String name, MaxLevelBehaviour defaultBehaviour) {
        for (MaxLevelBehaviour behaviour : values()) {
            if (behaviour.behaviourName.equalsIgnoreCase(name)) {
                return behaviour;
            }
        }
        return defaultBehaviour;
    }
}
