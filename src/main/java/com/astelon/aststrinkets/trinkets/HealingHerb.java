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

public class HealingHerb extends Trinket {

    public HealingHerb(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "healingHerb", TextColor.fromHexString("#00AAAA"), Power.RESTORE_FULL_HEALTH,
                false, Usages.USE_SELF_OR_ON_CREATURE);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.KELP);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Healing Herb", NamedTextColor.DARK_GREEN));
        meta.lore(List.of(Component.text("It smells horrible and it doesn't"),
                Component.text("taste any better, but the effects"),
                Component.text("are worth it.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
