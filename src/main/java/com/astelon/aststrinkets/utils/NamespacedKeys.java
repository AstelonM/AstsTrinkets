package com.astelon.aststrinkets.utils;

import com.astelon.aststrinkets.AstsTrinkets;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class NamespacedKeys {

    private HashMap<NamespacedKey, PersistentDataType<?, ?>> keys;

    public final NamespacedKey nameKey;
    public final NamespacedKey powerKey;
    public final NamespacedKey ownerKey;
    public final NamespacedKey trapKey;
    public final NamespacedKey failureChanceKey;
    public final NamespacedKey criticalFailureChanceKey;
    public final NamespacedKey shulkerBoxKey;
    public final NamespacedKey invulnerabilitySourceKey;
    public final NamespacedKey lastUseKey;
    public final NamespacedKey storedExperienceKey;
    public final NamespacedKey trinketImmuneKey;
    public final NamespacedKey customIdKey;
    public final NamespacedKey customNameKey;
    public final NamespacedKey commandsKey;
    public final NamespacedKey cooldownKey;
    public final NamespacedKey remainingUsesKey;
    public final NamespacedKey functionalCopiesKey;

    public NamespacedKeys(AstsTrinkets plugin) {
        //TODO should make these more uniform eventually + think how to deal with the obsolete versions
        nameKey = new NamespacedKey(plugin, "trinketName");
        powerKey = new NamespacedKey(plugin, "trinketPower");
        ownerKey = new NamespacedKey(plugin, "trinketOwner");
        trapKey = new NamespacedKey(plugin, "trinketTrappedEntity");
        failureChanceKey = new NamespacedKey(plugin, "failureChance");
        criticalFailureChanceKey = new NamespacedKey(plugin, "criticalFailureChance");
        shulkerBoxKey = new NamespacedKey(plugin, "shulkerBoxKey");
        invulnerabilitySourceKey = new NamespacedKey(plugin, "invulnerabilitySource");
        lastUseKey = new NamespacedKey(plugin, "lastUse");
        storedExperienceKey = new NamespacedKey(plugin, "storedExperience");
        trinketImmuneKey = new NamespacedKey(plugin, "trinketImmune");
        customIdKey = new NamespacedKey(plugin, "customId");
        customNameKey = new NamespacedKey(plugin, "customName");
        commandsKey = new NamespacedKey(plugin, "commands");
        cooldownKey = new NamespacedKey(plugin, "cooldown");
        remainingUsesKey = new NamespacedKey(plugin, "remainingUses");
        functionalCopiesKey = new NamespacedKey(plugin, "functionalCopies");

        initKeyMap();
    }

    private void initKeyMap() {
        keys = new HashMap<>();
        keys.put(nameKey, PersistentDataType.STRING);
        keys.put(powerKey, PersistentDataType.STRING);
        keys.put(ownerKey, PersistentDataType.STRING);
        keys.put(trapKey, PersistentDataType.BYTE_ARRAY);
        keys.put(failureChanceKey, PersistentDataType.DOUBLE);
        keys.put(criticalFailureChanceKey, PersistentDataType.DOUBLE);
        keys.put(shulkerBoxKey, PersistentDataType.BYTE_ARRAY);
        keys.put(invulnerabilitySourceKey, PersistentDataType.STRING);
        keys.put(lastUseKey, PersistentDataType.LONG);
        keys.put(storedExperienceKey, PersistentDataType.INTEGER);
        keys.put(trinketImmuneKey, PersistentDataType.BYTE);
        keys.put(customIdKey, PersistentDataType.STRING);
        keys.put(customNameKey, PersistentDataType.STRING);
        keys.put(commandsKey, PersistentDataType.STRING);
        keys.put(cooldownKey, PersistentDataType.INTEGER);
        keys.put(remainingUsesKey, PersistentDataType.INTEGER);
        keys.put(functionalCopiesKey, PersistentDataType.BYTE);
    }

    public HashMap<NamespacedKey, PersistentDataType<?, ?>> getKeyMap() {
        return keys;
    }
}
