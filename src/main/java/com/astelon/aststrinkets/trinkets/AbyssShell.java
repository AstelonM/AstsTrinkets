package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AbyssShell extends RandomTeleportationItemTrinket {

    public AbyssShell(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "abyssShell", TextColor.fromHexString("#CE3574"), Power.REUSABLE_RANDOM_TELEPORTATION,
                true, Usages.SHIFT_RIGHT_CLICK);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Abyss Shell", TextColor.fromHexString("#004269")));
        meta.lore(List.of(Component.text("An old shell of some marine creature."),
                Component.text("You feel intense dread when you look"),
                Component.text("at it. The longer you gaze inside,"),
                Component.text("the more it feels like it gazes"),
                Component.text("back at you.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
