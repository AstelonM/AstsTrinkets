package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

//TODO InteractionBasedTrinkets?
public abstract class CreatureAffectingTrinket extends Trinket {

    protected final NamespacedKey invulnerabilitySourceKey;
    protected boolean petOwnerOnly;

    public CreatureAffectingTrinket(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey,
                                    NamespacedKey invulnerabilitySourceKey, String name, TextColor infoColour, Power power,
                                    boolean isOp, String usage) {
        super(plugin, nameKey, powerKey, name, infoColour, power, isOp, usage);
        this.invulnerabilitySourceKey = invulnerabilitySourceKey;
    }

    public CreatureAffectingTrinket(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey,
                                    NamespacedKey invulnerabilitySourceKey, String name, Power power, boolean isOp,
                                    String usage) {
        super(plugin, nameKey, powerKey, name, power, isOp, usage);
        this.invulnerabilitySourceKey = invulnerabilitySourceKey;
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
            if (container.has(invulnerabilitySourceKey, PersistentDataType.STRING) &&
                    player.getName().equals(container.get(invulnerabilitySourceKey, PersistentDataType.STRING)))
                return false;
            return !player.hasPermission("aststrinkets.trinket.ignoreinvulnerable") && !Utils.isPetOwner(entity, player);
        }
        return false;
    }
}
