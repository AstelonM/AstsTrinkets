package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

// Technically should be an interface by design, but abstract class fits best as implementation
public abstract class PetCheckingTrinket extends Trinket {

    protected boolean petOwnerOnly;

    public PetCheckingTrinket(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, String name,
                              TextColor infoColour, Power power, boolean isOp, String usage) {
        super(plugin, nameKey, powerKey, name, infoColour, power, isOp, usage);
    }

    public PetCheckingTrinket(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, String name, Power power,
                              boolean isOp, String usage) {
        super(plugin, nameKey, powerKey, name, power, isOp, usage);
    }

    public void setPetOwnerOnly(boolean petOwnerOnly) {
        this.petOwnerOnly = petOwnerOnly;
    }

    public boolean canUseOnPet(Entity entity, Player player) {
        if (petOwnerOnly && !player.hasPermission("aststrinkets.trinket.ignorepetowner"))
            return Utils.isPetOwner(entity, player);
        return true;
    }
}
