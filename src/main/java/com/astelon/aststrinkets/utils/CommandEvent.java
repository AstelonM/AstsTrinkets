package com.astelon.aststrinkets.utils;

public enum CommandEvent {

    INTERACT_PLAYER("interactPlayer", false),
    INTERACT_MOB("interactMob", false),
    INTERACT_BLOCK("interactBlock", false),
    TARGET_PLAYER("targetPlayer", true),
    TARGET_MOB("targetMob", true),
    TARGET_BLOCK("targetBlock", true);

    private final String tag;
    private final boolean targetBased;

    CommandEvent(String tag, boolean targetBased) {
        this.tag = tag;
        this.targetBased = targetBased;
    }

    public static boolean matches(CommandEvent commandEvent, CommandEvent actionEvent) {
        if (commandEvent == actionEvent)
            return true;
        else if (commandEvent == null) // Command event requires shift right click air, action event is an interaction
            return false;
        else if (commandEvent.targetBased) // Only accepts null action event, since if it's not null, it's an interaction
            return actionEvent == null;
        return false; // If it's an interaction based event, it only accepts itself
    }

    public static CommandEvent getCommandEvent(String runConfig) {
        for (CommandEvent event: values()) {
            if (runConfig.contains(event.tag))
                return event;
        }
        return null;
    }
}
