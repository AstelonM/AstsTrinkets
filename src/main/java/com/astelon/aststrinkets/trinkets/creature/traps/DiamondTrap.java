package com.astelon.aststrinkets.trinkets.creature.traps;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class DiamondTrap extends CrystalTrap {

    public DiamondTrap(AstsTrinkets plugin, MobInfoManager mobInfoManager, NamespacedKeys keys) {
        super(plugin, mobInfoManager, keys, "diamondTrap", Power.CAPTURE_ENTITIES, true, NamedTextColor.BLUE,
                NamedTextColor.GOLD, Usages.TRAP);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Diamond Trap", NamedTextColor.BLUE));
        meta.lore(List.of(Component.text("This diamond was magically altered"),
                Component.text("in order to trap creatures inside.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    protected void setMobs() {
        trappableMobs.add(Mob.class);
        untrappableMobs.add(EnderDragon.class);
        untrappableMobs.add(Wither.class);
    }
}
