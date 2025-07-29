package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MysteryShell extends RandomTeleportationItemTrinket {

    public MysteryShell(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "mysteryShell", Power.RANDOM_TELEPORTATION, false, Usages.SHIFT_RIGHT_CLICK);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Mystery Shell", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("An old shell of some marine creature"),
                Component.text("that looks like it could crumble at"),
                Component.text("any moment. You have an odd feeling"),
                Component.text("of dread when you look inside it.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
