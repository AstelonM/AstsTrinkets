package com.astelon.aststrinkets.trinkets.projectile.rocket;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.format.TextColor;

import java.util.Random;

public abstract class WeatherRocketTrinket extends FireworkTrinket {

    protected final Random random;

    protected int minWeatherDuration;
    protected int maxWeatherDuration;

    public WeatherRocketTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour, Power power,
                                boolean op, String usage) {
        super(plugin, keys, name, infoColour, power, op, usage);
        random = new Random();
    }

    public WeatherRocketTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, Power power, boolean op, String usage) {
        super(plugin, keys, name, power, op, usage);
        random = new Random();
    }

    public int getWeatherDuration() {
        return random.nextInt(minWeatherDuration, maxWeatherDuration + 1);
    }

    public int getMinWeatherDuration() {
        return minWeatherDuration;
    }

    public void setMinWeatherDuration(int minWeatherDuration) {
        this.minWeatherDuration = minWeatherDuration;
    }

    public int getMaxWeatherDuration() {
        return maxWeatherDuration;
    }

    public void setMaxWeatherDuration(int maxWeatherDuration) {
        this.maxWeatherDuration = maxWeatherDuration;
    }
}
