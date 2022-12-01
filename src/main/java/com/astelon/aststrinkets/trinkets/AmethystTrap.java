package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AmethystTrap extends CrystalTrap {

    public AmethystTrap(AstsTrinkets plugin, MobInfoManager mobInfoManager, NamespacedKey nameKey, NamespacedKey powerKey,
                        NamespacedKey trapKey, NamespacedKey ownerKey) {
        super(plugin, mobInfoManager, nameKey, powerKey, trapKey, ownerKey, "amethystTrap", Power.CAPTURE_MONSTERS,
                true, NamedTextColor.LIGHT_PURPLE, TextColor.fromHexString("#FC3A3A"), Usages.TRAP);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Amethyst Trap", NamedTextColor.LIGHT_PURPLE));
        meta.lore(List.of(Component.text("This amethyst was magically altered"),
                Component.text("in order to trap monsters inside.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    protected void setMobs() {
        trappableMobs.add(Monster.class);
        trappableMobs.add(Golem.class);
        trappableMobs.add(Slime.class);
        trappableMobs.add(Phantom.class);
        trappableMobs.add(SkeletonHorse.class);
        trappableMobs.add(ZombieHorse.class);
        untrappableMobs.add(Enderman.class);
        untrappableMobs.add(Endermite.class);
        untrappableMobs.add(Illager.class);
        untrappableMobs.add(PiglinAbstract.class);
        untrappableMobs.add(Silverfish.class);
        untrappableMobs.add(Spider.class);
        untrappableMobs.add(Witch.class);
        untrappableMobs.add(Wither.class);
    }
}
