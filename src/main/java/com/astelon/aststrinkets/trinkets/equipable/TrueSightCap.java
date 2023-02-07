package com.astelon.aststrinkets.trinkets.equipable;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.Repairable;

import java.util.List;

public class TrueSightCap extends Trinket {

    public TrueSightCap(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "trueSightCap", NamedTextColor.GRAY, Power.TRUE_SIGHT, false, Usages.WEAR);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(Color.WHITE);
        meta.displayName(Component.text("Cap of True Sight", NamedTextColor.WHITE));
        meta.lore(List.of(Component.text("The wearer can see those wearing"),
                Component.text("invisibility tunics.")));
        Repairable repairableMeta = (Repairable) meta;
        repairableMeta.setRepairCost(100);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
