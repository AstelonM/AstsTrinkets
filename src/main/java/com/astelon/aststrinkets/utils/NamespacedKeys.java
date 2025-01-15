package com.astelon.aststrinkets.utils;

import com.astelon.aststrinkets.AstsTrinkets;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class NamespacedKeys {

    private final AstsTrinkets plugin;

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
    public final NamespacedKey useWorldDefaultKey;
    public final NamespacedKey surfaceOnlyKey;
    public final NamespacedKey minXKey;
    public final NamespacedKey maxXKey;
    public final NamespacedKey minYKey;
    public final NamespacedKey maxYKey;
    public final NamespacedKey minZKey;
    public final NamespacedKey maxZKey;
    public final NamespacedKey hideHelpKey;
    public final NamespacedKey virusInfectivityKey;
    public final NamespacedKey virusMinSpreadKey;
    public final NamespacedKey virusMaxSpreadKey;
    public final NamespacedKey virusLethalityKey;

    public NamespacedKeys(AstsTrinkets plugin) {
        this.plugin = plugin;
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
        useWorldDefaultKey = new NamespacedKey(plugin, "useWorldDefault");
        surfaceOnlyKey = new NamespacedKey(plugin, "surfaceOnly");
        minXKey = new NamespacedKey(plugin, "minX");
        maxXKey = new NamespacedKey(plugin, "maxX");
        minYKey = new NamespacedKey(plugin, "minY");
        maxYKey = new NamespacedKey(plugin, "maxY");
        minZKey = new NamespacedKey(plugin, "minZ");
        maxZKey = new NamespacedKey(plugin, "maxZ");
        hideHelpKey = new NamespacedKey(plugin, "hideHelp");
        virusInfectivityKey = new NamespacedKey(plugin, "virusInfectivity");
        virusMinSpreadKey = new NamespacedKey(plugin, "virusMinSpread");
        virusMaxSpreadKey = new NamespacedKey(plugin, "virusMaxSpread");
        virusLethalityKey = new NamespacedKey(plugin, "virusLethality");
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
        keys.put(useWorldDefaultKey.getKey(), new KeyTypePair(useWorldDefaultKey, PersistentDataType.BYTE));
        keys.put(surfaceOnlyKey.getKey(), new KeyTypePair(surfaceOnlyKey, PersistentDataType.BYTE));
        keys.put(minXKey.getKey(), new KeyTypePair(minXKey, PersistentDataType.INTEGER));
        keys.put(maxXKey.getKey(), new KeyTypePair(maxXKey, PersistentDataType.INTEGER));
        keys.put(minYKey.getKey(), new KeyTypePair(minYKey, PersistentDataType.INTEGER));
        keys.put(maxYKey.getKey(), new KeyTypePair(maxYKey, PersistentDataType.INTEGER));
        keys.put(minZKey.getKey(), new KeyTypePair(minZKey, PersistentDataType.INTEGER));
        keys.put(maxZKey.getKey(), new KeyTypePair(maxZKey, PersistentDataType.INTEGER));
        keys.put(hideHelpKey.getKey(), new KeyTypePair(hideHelpKey, PersistentDataType.BYTE));
        keys.put(virusInfectivityKey.getKey(), new KeyTypePair(virusInfectivityKey, PersistentDataType.DOUBLE));
        keys.put(virusMinSpreadKey.getKey(), new KeyTypePair(virusMinSpreadKey, PersistentDataType.INTEGER));
        keys.put(virusMaxSpreadKey.getKey(), new KeyTypePair(virusMaxSpreadKey, PersistentDataType.INTEGER));
        keys.put(virusLethalityKey.getKey(), new KeyTypePair(virusLethalityKey, PersistentDataType.DOUBLE));
        keys = Collections.unmodifiableMap(keys);
    }

    public Collection<KeyTypePair> getKeys() {
        return keys.values();
    }

    public KeyTypePair getKeyTypePair(String keyName) {
        return keys.get(keyName);
    }

    public boolean setKey(PersistentDataContainer container, KeyTypePair keyTypePair, String value) {
        NamespacedKey key = keyTypePair.key();
        PersistentDataType<?, ?> type = keyTypePair.type();
        try {
            if (type.equals(PersistentDataType.STRING))
                container.set(key, PersistentDataType.STRING, value);
            else if (type.equals(PersistentDataType.BYTE_ARRAY))
                container.set(key, PersistentDataType.BYTE_ARRAY, Base64.getDecoder().decode(value));
            else if (type.equals(PersistentDataType.DOUBLE))
                container.set(key, PersistentDataType.DOUBLE, Double.parseDouble(value));
            else if (type.equals(PersistentDataType.LONG))
                container.set(key, PersistentDataType.LONG, Long.parseLong(value));
            else if (type.equals(PersistentDataType.INTEGER))
                container.set(key, PersistentDataType.INTEGER, Integer.parseInt(value));
            else if (type.equals(PersistentDataType.BYTE))
                container.set(key, PersistentDataType.BYTE, Byte.parseByte(value));
            else if (type.equals(PersistentDataType.FLOAT))
                container.set(key, PersistentDataType.FLOAT, Float.parseFloat(value));
            else
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean setKey(PersistentDataContainer container, KeyTypePair keyTypePair, Object value) {
        if (value == null)
            return false;
        NamespacedKey key = keyTypePair.key();
        PersistentDataType<?, ?> type = keyTypePair.type();
        try {
            if (type.equals(PersistentDataType.STRING))
                container.set(key, PersistentDataType.STRING, (String) value);
            else if (type.equals(PersistentDataType.BYTE_ARRAY))
                container.set(key, PersistentDataType.BYTE_ARRAY, (byte[]) value);
            else if (type.equals(PersistentDataType.DOUBLE))
                container.set(key, PersistentDataType.DOUBLE, (double) value);
            else if (type.equals(PersistentDataType.LONG))
                container.set(key, PersistentDataType.LONG, (long) value);
            else if (type.equals(PersistentDataType.INTEGER))
                container.set(key, PersistentDataType.INTEGER, (int) value);
            else if (type.equals(PersistentDataType.BYTE))
                container.set(key, PersistentDataType.BYTE, (byte) value);
            else if (type.equals(PersistentDataType.FLOAT))
                container.set(key, PersistentDataType.FLOAT, (float) value);
            else
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void transferKeys(ItemStack source, ItemStack destination) {
        ItemMeta sourceMeta = source.getItemMeta();
        if (sourceMeta == null)
            return;
        PersistentDataContainer sourceContainer = sourceMeta.getPersistentDataContainer();
        ItemMeta destinationMeta = destination.getItemMeta();
        if (destinationMeta == null)
            destinationMeta = plugin.getServer().getItemFactory().getItemMeta(destination.getType());
        PersistentDataContainer destinationContainer = destinationMeta.getPersistentDataContainer();
        for (KeyTypePair keyTypePair : getKeys()) {
            if (sourceContainer.has(keyTypePair.key, keyTypePair.type)) {
                //TODO decide what to do if one of the keys could not be set
                setKey(destinationContainer, keyTypePair, sourceContainer.get(keyTypePair.key, keyTypePair.type));
            }
        }
        destination.setItemMeta(destinationMeta);
    }

    public record KeyTypePair(NamespacedKey key, PersistentDataType<?, ?> type) {}
}
