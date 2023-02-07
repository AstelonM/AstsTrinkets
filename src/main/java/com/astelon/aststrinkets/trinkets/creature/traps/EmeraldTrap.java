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

public class EmeraldTrap extends CrystalTrap {

    public EmeraldTrap(AstsTrinkets plugin, MobInfoManager mobInfoManager, NamespacedKeys keys) {
        super(plugin, mobInfoManager, keys, "emeraldTrap", Power.CAPTURE_ANIMALS, true, NamedTextColor.GREEN,
                NamedTextColor.AQUA, Usages.TRAP);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.EMERALD);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Emerald Trap", NamedTextColor.GREEN));
        meta.lore(List.of(Component.text("This emerald was magically altered"),
                Component.text("in order to trap animals inside."),
                Component.text("The animals can be aggressive.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    protected void setMobs() {
        trappableMobs.add(Ambient.class);
        trappableMobs.add(Animals.class);
        trappableMobs.add(WaterMob.class);
        trappableMobs.add(Spider.class);
        trappableMobs.add(Silverfish.class);
        trappableMobs.add(Endermite.class);
        untrappableMobs.add(SkeletonHorse.class);
        untrappableMobs.add(ZombieHorse.class);
    }
}
