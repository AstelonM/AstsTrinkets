package com.astelon.aststrinkets.utils;

import com.astelon.aststrinkets.AstsTrinkets;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NamespacedKeys {

    private Map<String, KeyTypePair> keys;

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
    public final NamespacedKey locationKey;
    public final NamespacedKey rangeKey;
    public final NamespacedKey lockedKey;
    public final NamespacedKey amountKey;
    public final NamespacedKey useChanceKey;
    public final NamespacedKey breakBlocksKey;
    public final NamespacedKey explosionPowerKey;
    public final NamespacedKey setFireKey;
    public final NamespacedKey maxAmountKey;

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
        locationKey = new NamespacedKey(plugin, "location");
        rangeKey = new NamespacedKey(plugin, "range");
        lockedKey = new NamespacedKey(plugin, "locked");
        amountKey = new NamespacedKey(plugin, "amount");
        useChanceKey = new NamespacedKey(plugin, "useChance");
        breakBlocksKey = new NamespacedKey(plugin, "breakBlocks");
        explosionPowerKey = new NamespacedKey(plugin, "explosionPower");
        setFireKey = new NamespacedKey(plugin, "setFire");
        maxAmountKey = new NamespacedKey(plugin, "maxAmount");
        initKeyMap();
    }

    private void initKeyMap() {
        keys = new HashMap<>();
        keys.put(nameKey.getKey(), new KeyTypePair(nameKey, PersistentDataType.STRING));
        keys.put(powerKey.getKey(), new KeyTypePair(powerKey, PersistentDataType.STRING));
        keys.put(ownerKey.getKey(), new KeyTypePair(ownerKey, PersistentDataType.STRING));
        keys.put(trapKey.getKey(), new KeyTypePair(trapKey, PersistentDataType.BYTE_ARRAY));
        keys.put(failureChanceKey.getKey(), new KeyTypePair(failureChanceKey, PersistentDataType.DOUBLE));
        keys.put(criticalFailureChanceKey.getKey(), new KeyTypePair(criticalFailureChanceKey, PersistentDataType.DOUBLE));
        keys.put(shulkerBoxKey.getKey(), new KeyTypePair(shulkerBoxKey, PersistentDataType.BYTE_ARRAY));
        keys.put(invulnerabilitySourceKey.getKey(), new KeyTypePair(invulnerabilitySourceKey, PersistentDataType.STRING));
        keys.put(lastUseKey.getKey(), new KeyTypePair(lastUseKey, PersistentDataType.LONG));
        keys.put(storedExperienceKey.getKey(), new KeyTypePair(storedExperienceKey, PersistentDataType.INTEGER));
        keys.put(trinketImmuneKey.getKey(), new KeyTypePair(trinketImmuneKey, PersistentDataType.BYTE));
        keys.put(customIdKey.getKey(), new KeyTypePair(customIdKey, PersistentDataType.STRING));
        keys.put(customNameKey.getKey(), new KeyTypePair(customNameKey, PersistentDataType.STRING));
        keys.put(commandsKey.getKey(), new KeyTypePair(commandsKey, PersistentDataType.STRING));
        keys.put(cooldownKey.getKey(), new KeyTypePair(cooldownKey, PersistentDataType.INTEGER));
        keys.put(remainingUsesKey.getKey(), new KeyTypePair(remainingUsesKey, PersistentDataType.INTEGER));
        keys.put(functionalCopiesKey.getKey(), new KeyTypePair(functionalCopiesKey, PersistentDataType.BYTE));
        keys.put(locationKey.getKey(), new KeyTypePair(locationKey, PersistentDataType.STRING));
        keys.put(rangeKey.getKey(), new KeyTypePair(rangeKey, PersistentDataType.INTEGER));
        keys.put(lockedKey.getKey(), new KeyTypePair(lockedKey, PersistentDataType.BYTE));
        keys.put(amountKey.getKey(), new KeyTypePair(amountKey, PersistentDataType.INTEGER));
        keys.put(useChanceKey.getKey(), new KeyTypePair(useChanceKey, PersistentDataType.DOUBLE));
        keys.put(breakBlocksKey.getKey(), new KeyTypePair(breakBlocksKey, PersistentDataType.BYTE));
        keys.put(explosionPowerKey.getKey(), new KeyTypePair(explosionPowerKey, PersistentDataType.FLOAT));
        keys.put(setFireKey.getKey(), new KeyTypePair(setFireKey, PersistentDataType.BYTE));
        keys.put(maxAmountKey.getKey(), new KeyTypePair(maxAmountKey, PersistentDataType.INTEGER));
        keys = Collections.unmodifiableMap(keys);
    }

    public Collection<KeyTypePair> getKeys() {
        return keys.values();
    }

    public KeyTypePair getKeyTypePair(String keyName) {
        return keys.get(keyName);
    }

    public record KeyTypePair(NamespacedKey key, PersistentDataType<?, ?> type) {}
}
