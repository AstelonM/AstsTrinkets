package com.astelon.aststrinkets.trinkets.creature;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InfestedWheat extends Trinket {

    public InfestedWheat(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "infestedWheat", Power.TURN_COWS_INTO_MOOSHROOMS, false, Usages.INTERACT_ENTITY);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.WHEAT);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Infested Wheat", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("This bunch of wheat seems to be"),
                Component.text("infested with the fungus that"),
                Component.text("parasitizes cows.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
