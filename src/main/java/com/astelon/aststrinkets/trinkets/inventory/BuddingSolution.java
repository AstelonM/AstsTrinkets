package com.astelon.aststrinkets.trinkets.inventory;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class BuddingSolution extends Trinket {

    public BuddingSolution(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "buddingSolution", Power.CREATE_BUDDING_AMETHYSTS, false, Usages.INVENTORY);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.THICK));
        meta.displayName(Component.text("Budding Solution", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("A thick solution which causes"),
                Component.text("blocks of amethyst to bud.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
