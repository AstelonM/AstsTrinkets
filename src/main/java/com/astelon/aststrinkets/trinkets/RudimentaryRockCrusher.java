package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RudimentaryRockCrusher extends Trinket {

    public RudimentaryRockCrusher(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "rudimentaryRockCrusher", TextColor.fromHexString("#A4A4A4"), Power.TURN_STONE_INTO_GRAVEL,
                false, Usages.MINE);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Rudimentary Rock Crusher", TextColor.fromHexString("#666666")));
        meta.lore(List.of(Component.text("A simple tool designed for"),
                Component.text("crushing stone into gravel.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
