package com.astelon.aststrinkets.trinkets.projectile.rocket;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.List;
import java.util.Random;

public class CloudSeeder extends FireworkTrinket {

    private final Random random;

    private int minWeatherDuration;
    private int maxWeatherDuration;

    public CloudSeeder(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "cloudSeeder", Power.CREATE_RAIN, false, Usages.PLACED_FIREWORK);
        random = new Random();
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
        FireworkMeta meta = (FireworkMeta) itemStack.getItemMeta();
        meta.setPower(4);
        meta.displayName(Component.text("Cloud Seeder", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Particles within this rocket allow"),
                Component.text("water from the clouds to condense"),
                Component.text("on them and form water droplets."),
                Component.text("Now with no health hazards!")));
        meta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.WHITE).withTrail().build());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void seedClouds(World world) {
        int duration = random.nextInt(minWeatherDuration, maxWeatherDuration + 1);
        world.setStorm(true);
        world.setWeatherDuration(duration);
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
