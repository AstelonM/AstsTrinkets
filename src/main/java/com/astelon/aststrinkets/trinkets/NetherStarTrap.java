package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class NetherStarTrap extends CrystalTrap {

    public NetherStarTrap(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, NamespacedKey trapKey) {
        super(plugin, nameKey, powerKey, trapKey, "netherStarTrap", Power.STRONGER_CAPTURE_ENTITIES, true,
                NamedTextColor.GOLD);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Nether Star Trap", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("The Nether Star is a powerful"),
                Component.text("artifact on its own. This one was"),
                Component.text("transformed into a trap which is"),
                Component.text("not destroyed when used.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    protected void setMobs() {
        trappableMobs.add(Mob.class);
        untrappableMobs.add(EnderDragon.class);
    }

    public ItemStack emptyTrap(ItemStack itemStack) {
        if (!isTrinket(itemStack))
            throw new IllegalArgumentException("Not a Nether Star Trap.");
        ItemStack result = itemStack.asOne();
        ItemMeta meta = result.getItemMeta();
        meta.displayName(Component.text("Nether Star Trap", NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false));
        List<Component> lore = meta.lore();
        ArrayList<Component> newLore = new ArrayList<>();
        if (lore != null) {
            PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
            for (Component component: lore) {
                // Not elegant, but should make do for now
                if (!serializer.serialize(component).startsWith("This one contains:"))
                    newLore.add(component);
            }
        } else
            newLore.addAll(this.itemStack.lore());
        meta.lore(newLore);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(trapKey, PersistentDataType.BYTE_ARRAY))
            container.remove(trapKey);
        result.setItemMeta(meta);
        return result;
    }
}
