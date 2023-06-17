package com.astelon.aststrinkets.trinkets.projectile.arrow;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.projectile.ProjectileTrinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.format.TextColor;

public abstract class ArrowTrinket extends ProjectileTrinket {

    private boolean piercingAllowed;
    private boolean multishotAllowed;

    public ArrowTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour, Power power,
                        boolean op, String usage) {
        super(plugin, keys, name, infoColour, power, op, usage);
    }

    public ArrowTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, Power power, boolean op, String usage) {
        super(plugin, keys, name, power, op, usage);
    }

    public boolean isPiercingAllowed() {
        return piercingAllowed;
    }

    public void setPiercingAllowed(boolean piercingAllowed) {
        this.piercingAllowed = piercingAllowed;
    }

    public boolean isMultishotAllowed() {
        return multishotAllowed;
    }

    public void setMultishotAllowed(boolean multishotAllowed) {
        this.multishotAllowed = multishotAllowed;
    }
}
