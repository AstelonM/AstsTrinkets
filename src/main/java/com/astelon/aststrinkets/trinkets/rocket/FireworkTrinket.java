package com.astelon.aststrinkets.trinkets.rocket;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.format.TextColor;

public abstract class FireworkTrinket extends Trinket {

    private boolean allowUseAsFirework;

    public FireworkTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour, Power power,
                           boolean op, String usage) {
        super(plugin, keys, name, infoColour, power, op, usage);
    }

    public boolean isAllowUseAsFirework() {
        return allowUseAsFirework;
    }

    public void setAllowUseAsFirework(boolean allowUseAsFirework) {
        this.allowUseAsFirework = allowUseAsFirework;
    }
}
