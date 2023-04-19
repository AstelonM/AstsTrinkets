package com.astelon.aststrinkets.utils;

import com.astelon.aststrinkets.AstsTrinkets;
import org.bukkit.NamespacedKey;

public class NamespacedKeys {

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
    }
}
