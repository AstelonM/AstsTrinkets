package com.astelon.aststrinkets.utils;

public final class Usages {

    public static final String WEAR = "Equip this trinket to start using it. Unequip it " +
            "to stop using it.";
    public static final String DONT = "You don't use it.";
    public static final String PLACE_AND_EAT = "Place it down and eat it.";
    public static final String SPINNERET = "Place down string while holding the trinket in " +
            "the other hand.";
    public static final String INVENTORY = "1. Open your inventory.<br>" +
            "2. Click on the trinket so that it's on your mouse.<br>" +
            "3. Do <b>NOT</b> close your inventory.<br>" +
            "4. Hold shift and right click on the item you want to use the trinket on.";
    public static final String YOUTH_MILK = "Right click an entity that has a baby form " +
            "using the trinket.";
    public static final String TRAP = "If the trap is empty, right click an entity. " +
            "Do note that not all entities can be caught in any trap.<br>" +
            "If the trap has an entity inside, right click on a block that has enough space above.";
    public static final String INVENTORY_AND_PLACE = INVENTORY + "<br>" +
            "5. The trinket should now be a block. You can simply place it down.";
    public static final String FIREWORK_ROCKET = "Boost your elytra with it.";
    public static final String ARROW = "Shoot the trinket from a bow or crossbow.";
    public static final String SHULKER_BOX_UNIT = INVENTORY + "<br>" +
            "5. You can retrieve the shulker box by placing down the trinket or by right clicking air while sneaking " +
            "with the trinket in your hand.";
    public static final String INTERACT_ENTITY_WITH_BABY_FORM = "Right click an entity that has a baby form using the trinket.";
    public static final String HIT_AND_DAMAGE = "Hit a player or creature and damage them.";
    public static final String THROW = "Throw it somewhere with right click.";
    public static final String BAIT = "Hold in your other hand when fishing.";
    public static final String SHIFT_RIGHT_CLICK = "Hold it in your hand, then right click while sneaking.";
    public static final String CUT_TREES = "You should cut some trees.";
    public static final String SPELLBOOK_USE = "Depending on the spellbook, right click air, players, mobs or blocks while sneaking and " +
            "holding the spellbook.";
    public static final String SPELLBOOK_CREATE = "Write commands inside of the book and sign it. You can use several " +
            "tokens to change how the commands are run.<br>" +
            "The following tokens should be written on their own line:<br>" +
            "1. <display> - add this at the end of a page to make all the pages after it the only ones displayed.<br>" +
            "2. <cooldown:(cooldown in seconds)> - change the cooldown of the spellbook. By default, it is 1 second.<br>" +
            "3. <uses:(number of uses)> - set how many times the spellbook can be uses. By default, it can be used infinitely.<br>" +
            "4. <title:(custom title here)> - set the title of the signed book to this one.<br>" +
            "5. <noCopy> - makes the spellbook impossible to copy.<br>" +
            "6. <functionalCopies:(original or copy)> - if set to 'original', makes copies of original work as spellbooks. " +
            "If set to 'copy', makes copies of a copy work as spellbooks. By default, copies can be created, but not used.<br>" +
            "The following tokens should be written inside the actual command and act as placeholders:<br>" +
            "1. <playerName> - replaced with the name of the player that uses the spellbook.<br>" +
            "2. <playerCoords> - replaced with the coordinates of the player using the spellbook, written as x y z.<br>" +
            "3. <targetCoords> - replaced with the coordinates of the target of the player, be it a mob, block or player, if available.<br>" +
            "4. <targetPlayerName> - replaced with the name of the target player, if available.<br>" +
            "5. <mobType> - replaced with the type of the target mob, if available.<br>" +
            "6. <blockType> - replaced with the type of the block the player is targeting, if available.<br>" +
            "7. <otherHandItemType> - replaced with the type of the item the player is holding in the other hand. If nothing is held, " +
            "it defaults to AIR.<br>" +
            "8. <x:(amount)>, <y:(amount)>, <z:(amount)> - replaced with the corresponding player coordinate to which the amount " +
            "is added.<br>" +
            "9. <targetX:(amount)>, <targetY:(amount)>, <targetZ:(amount)> - replaced with the corresponding target coordinate " +
            "to which the amount is added.<br>"+
            "The following tokens should be written right before the command, with no spaces between:<br>" +
            "1. <playerRun> - the command is run by the player using the spellbook, instead of by console.<br>" +
            "2. <otherHandItem> - the command is run only if the player has an item in the other hand.<br>" +
            "3. <otherHandEmpty> - the command is run only if the player has the other hand empty.<br>" +
            "4. <interactPlayer> - the command is run only if the player is right clicking another player.<br>" +
            "5. <interactMob> - the command is run only if the player is right clicking a mob.<br>" +
            "6. <interactBlock> - the command is run only if the player is right clicking a block.<br>" +
            "7. <targetPlayer> - the command is run only if the player is looking at another player. The maximum distance is " +
            "120 blocks.<br>" +
            "8. <targetMob> - the command is run only if the player is looking at a mob. The maximum distance is 120 blocks.<br>" +
            "9. <targetBlock> - the command is run only if the player is looking at a block. The maximum distance is 400 blocks.<br>" +
            "10. <world:(world name)> - the command is run only if the player is in the specified world.";
    public static final String LINK_AND_PLACE = "Right click air while sneaking and holding the trinket to link it to your " +
            "current location. After that, you can place it down like a block.";
    public static final String SHIFT_RIGHT_CLICK_AND_HOLD_WHILE_MINING = SHIFT_RIGHT_CLICK + " You can also hold it in the " +
            "other hand while mining.";
    public static final String TERRARIUM = "If the terrarium is empty, right click a creature to trap it inside.<br>" +
            "If the terrarium has a creature inside, place it down as a block to create a spawner or right click the block " +
            "while sneaking to release the creature.<br>" +
            "A filled terrarium can be locked by right clicking air while sneaking twice, while holding it. A locked terrarium " +
            "is consumed when the creature is released. One that hasn't been locked can be reused.";
    public static final String LINK_AND_USE = "Right click air while sneaking and holding the trinket to link it to your " +
            "current location. After that, right click air while sneaking again to use it.";
    public static final String BUNDLE = "Use like a regular bundle.";
    public static final String INVENTORY_OR_INTERACT = "Use on items with the following steps:<br>" + INVENTORY + "<br>" +
            "Use on entities by right clicking them.";
    public static final String INTERACT_ENTITY = "Right click an entity using the trinket.";
    public static final String USE_SELF_OR_ON_CREATURE = "How to use it on yourself:<br>" + SHIFT_RIGHT_CLICK +
            "<br>How to use it on other creatures:<br>" + INTERACT_ENTITY;
    public static final String FIREWORK = "Use it like a regular firework.";
}
