package com.astelon.aststrinkets.trinkets.inventory;

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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class PortableConcreteMixer extends Trinket {

    public PortableConcreteMixer(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "portableConcreteMixer", Power.HARDEN_CONCRETE_POWDER, false, Usages.INVENTORY);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.BUCKET);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Portable Concrete Mixer", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Your personal concrete mixer."),
                Component.text("This bad boy is for life!")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean hasWater(ItemStack mixer) {
        return mixer.getType() == Material.WATER_BUCKET;
    }

    public ItemStack hardenConcretePowder(ItemStack powder) {
        Material material = powder.getType();
        String name = material.name();
        if (name.contains("CONCRETE_POWDER")) {
            Material hardened = Material.getMaterial(name.substring(0, name.length() - 7));
            if (hardened != null) {
                powder.setType(hardened);
                return powder;
            }
        }
        return null;
    }

    public ItemStack fillMixer(ItemStack mixer) {
        ItemStack result = mixer.asOne();
        result.setType(Material.WATER_BUCKET);
        return result;
    }

    public ItemStack emptyMixer(ItemStack mixer) {
        ItemStack result = mixer.asOne();
        result.setType(Material.BUCKET);
        return result;
    }

    private void recreateTrinket(ItemStack mixer, ItemStack result) {
        keys.copyKeys(mixer, result);
        ItemMeta meta = result.getItemMeta();
        meta.displayName(Component.text("Portable Concrete Mixer", NamedTextColor.GOLD));
        ItemMeta sourceMeta = mixer.getItemMeta();
        if (sourceMeta.hasLore())
            meta.lore(sourceMeta.lore());
        else {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            List<Component> lore = new ArrayList<>();
            if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
                String ownerName = container.get(keys.ownerKey, PersistentDataType.STRING);
                lore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
            }
            lore.addAll(List.of(Component.text("Your personal concrete mixer."),
                    Component.text("This bad boy is for life!")));
            meta.lore(lore);
        }
        result.setItemMeta(meta);
    }
}
