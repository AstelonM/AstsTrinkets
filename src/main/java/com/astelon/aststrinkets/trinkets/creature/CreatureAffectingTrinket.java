package com.astelon.aststrinkets.trinkets.creature;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

//TODO InteractionBasedTrinkets?
public abstract class CreatureAffectingTrinket extends Trinket {

    protected boolean petOwnerOnly;

    public CreatureAffectingTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour, Power power,
                                    boolean op, String usage) {
        super(plugin, keys, name, infoColour, power, op, usage);
    }

    public CreatureAffectingTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, Power power, boolean op,
                                    String usage) {
        super(plugin, keys, name, power, op, usage);
    }

    public void setPetOwnerOnly(boolean petOwnerOnly) {
        this.petOwnerOnly = petOwnerOnly;
    }

    public boolean petOwnedByOtherPlayer(Entity entity, Player player) {
        if (petOwnerOnly && !player.hasPermission("aststrinkets.trinket.ignorepetowner"))
            return !Utils.isPetOwner(entity, player);
        return false;
    }

    public boolean isInvulnerableToPlayer(Entity entity, Player player) {
        if (entity.isInvulnerable()) {
            PersistentDataContainer container = entity.getPersistentDataContainer();
            if (container.has(keys.invulnerabilitySourceKey, PersistentDataType.STRING)) {
                return !player.getName().equals(container.get(keys.invulnerabilitySourceKey, PersistentDataType.STRING));
            }
            return !player.hasPermission("aststrinkets.trinket.ignoreinvulnerable") && !Utils.isPetOwner(entity, player);
        }
        return false;
    }
}
