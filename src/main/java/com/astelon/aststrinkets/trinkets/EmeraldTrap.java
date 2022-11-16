package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.managers.MobInfoManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EmeraldTrap extends CrystalTrap {

    public EmeraldTrap(AstsTrinkets plugin, MobInfoManager mobInfoManager, NamespacedKey nameKey, NamespacedKey powerKey,
                       NamespacedKey trapKey, NamespacedKey ownerKey) {
        super(plugin, mobInfoManager, nameKey, powerKey, trapKey, ownerKey, "emeraldTrap", Power.CAPTURE_ANIMALS,
                true, NamedTextColor.GREEN, NamedTextColor.AQUA);
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
