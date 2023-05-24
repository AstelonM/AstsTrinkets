package com.astelon.aststrinkets.trinkets.creature.traps;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.trinkets.inventory.BindingPowder;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class NetherStarTrap extends CrystalTrap {

    public NetherStarTrap(AstsTrinkets plugin, MobInfoManager mobInfoManager, NamespacedKeys keys) {
        super(plugin, mobInfoManager, keys, "netherStarTrap",
                Power.STRONGER_CAPTURE_ENTITIES, true, NamedTextColor.GOLD, Usages.TRAP);
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
        PersistentDataContainer container = meta.getPersistentDataContainer();
        ArrayList<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            String ownerName = container.get(keys.ownerKey, PersistentDataType.STRING);
            newLore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
        }
        if (container.has(keys.trapKey, PersistentDataType.BYTE_ARRAY))
            container.remove(keys.trapKey);
        newLore.addAll(this.itemStack.lore());
        meta.lore(newLore);
        result.setItemMeta(meta);
        return result;
    }
}
