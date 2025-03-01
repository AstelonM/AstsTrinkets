package com.astelon.aststrinkets.trinkets.inventory;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.MaxLevelBehaviour;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ForbiddenTome extends EnchantmentChangingTrinket {

    public ForbiddenTome(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "forbiddenTome", TextColor.fromHexString("#DEC210"), Power.IMPROVE_RANDOM_ENCHANTMENT,
                true, Usages.INVENTORY);
        setMaxLevelBehaviour(MaxLevelBehaviour.UNBOUND);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Forbidden Tome", TextColor.fromHexString("#800020")));
        meta.lore(List.of(Component.text("A tome of great power, feared"),
                Component.text("as much as craved.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
