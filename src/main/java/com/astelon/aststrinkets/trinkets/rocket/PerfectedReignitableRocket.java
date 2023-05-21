package com.astelon.aststrinkets.trinkets.rocket;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PerfectedReignitableRocket extends FireworkTrinket {

    public PerfectedReignitableRocket(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "perfectedReignitableRocket", TextColor.fromHexString("#726780"),
                Power.PERFECTED_REIGNITION, true, Usages.FIREWORK_ROCKET);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Perfected Reignitable Rocket", TextColor.fromHexString("#677580")));
        meta.lore(List.of(Component.text("The re-ignitable rocket design has"),
                Component.text("been perfected so that the rocket"),
                Component.text("can be reused without any chance"),
                Component.text("of failure.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
