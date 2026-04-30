package com.astelon.aststrinkets.trinkets.projectile;

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
import java.util.Random;

public class MysteryObject extends ProjectileTrinket {

    private final Random random;

    public MysteryObject(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "mysteryObject", Power.RANDOM_ITEM_WHEN_THROWN, false, Usages.THROW);
        random = new Random();
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.SNOWBALL);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Mystery Object", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("You could call it a UFO if it"),
                Component.text("could fly.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack getRandomItem() {
        Material material;
        do {
            material = Material.values()[random.nextInt(Material.values().length)];
        } while (!material.isItem());
        return new ItemStack(material);
    }
}
