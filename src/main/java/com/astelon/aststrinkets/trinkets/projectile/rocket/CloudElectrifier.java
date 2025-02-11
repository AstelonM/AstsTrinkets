package com.astelon.aststrinkets.trinkets.projectile.rocket;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import com.astelon.aststrinkets.utils.Utils;
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

public class CloudElectrifier extends WeatherRocketTrinket {

    public CloudElectrifier(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "cloudElectrifier", Power.CREATE_THUNDERSTORMS, false, Usages.PLACED_FIREWORK);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
        FireworkMeta meta = (FireworkMeta) itemStack.getItemMeta();
        meta.setPower(4);
        meta.displayName(Component.text("Cloud Electrifier", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Contrary to its name, this rocket"),
                Component.text("doesn't actually electrify clouds."),
                Component.text("It merely creates the conditions for"),
                Component.text("a thunderstorm to happen while it's"),
                Component.text("raining.")));
        meta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.SILVER).withTrail().build());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void electrifyClouds(World world) {
        int duration = getWeatherDuration();
        world.setThundering(true);
        world.setThunderDuration(Utils.secondsToTicks(duration));
    }
}
