package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

//TODO InteractionBasedTrinkets?
public abstract class CreatureAffectingTrinket extends Trinket {

    protected boolean petOwnerOnly;

    public CreatureAffectingTrinket(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, String name,
                                    TextColor infoColour, Power power, boolean isOp, String usage) {
        super(plugin, nameKey, powerKey, name, infoColour, power, isOp, usage);
    }

    public CreatureAffectingTrinket(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, String name, Power power,
                                    boolean isOp, String usage) {
        super(plugin, nameKey, powerKey, name, power, isOp, usage);
    }

    public void setPetOwnerOnly(boolean petOwnerOnly) {
        this.petOwnerOnly = petOwnerOnly;
    }

    public boolean isOwnedByAnother(Entity entity, Player player) {
        if (petOwnerOnly && !player.hasPermission("aststrinkets.trinket.ignorepetowner"))
            return !Utils.isPetOwner(entity, player);
        return false;
    }

    public boolean isImmune(Entity entity, Player player) {
        if (entity.isInvulnerable() && !player.hasPermission("aststrinkets.trinket.ignoreinvulnerable"))
            return true;
        return false;
    }
}
