package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.inventory.BindingPowder;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class NotebookFishingRod extends Trinket {

    public NotebookFishingRod(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "notebookFishingRod", Power.TALLY_FISHING_CATCHES, false, Usages.FISH);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Fishing Rod With Notebook", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("Fish: 0", infoColour).decoration(TextDecoration.ITALIC, false),
                Component.text("Treasures: 0", infoColour).decoration(TextDecoration.ITALIC, false),
                Component.text("Mending books: 0", infoColour).decoration(TextDecoration.ITALIC, false),
                Component.text("Junk: 0", infoColour).decoration(TextDecoration.ITALIC, false),
                Component.text("Just a trusty fishing rod with a"),
                Component.text("notebook to tally your catches.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void tally(ItemStack fishingRod, ItemStack caught) {
        NamespacedKey key;
        if (caught == null) // special case for the shapeshifter
            key = keys.treasureTallyKey;
        else {
            Material material = caught.getType();
            ItemMeta caughtMeta = caught.getItemMeta();
            key = getTallyKey(caught, caughtMeta, material);
        }
        ItemMeta meta = fishingRod.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int amount = container.getOrDefault(key, PersistentDataType.INTEGER, 0);
        container.set(key, PersistentDataType.INTEGER, amount + 1);
        List<Component> newLore = updateLore(meta, container);
        meta.lore(newLore);
        fishingRod.setItemMeta(meta);
    }

    private NamespacedKey getTallyKey(ItemStack caught, ItemMeta caughtMeta, Material material) {
        NamespacedKey key = switch (material) {
            case COD, SALMON, PUFFERFISH, TROPICAL_FISH -> keys.fishTallyKey;
            case BOW, NAME_TAG, NAUTILUS_SHELL, SADDLE, PLAYER_HEAD -> keys.treasureTallyKey;
            case LILY_PAD, BONE, BOWL, LEATHER, LEATHER_BOOTS, ROTTEN_FLESH, TRIPWIRE_HOOK, STICK, STRING, INK_SAC,
                 BAMBOO,
                 COCOA_BEANS -> keys.junkTallyKey;
            case FISHING_ROD -> {
                if (caught.getEnchantments().isEmpty())
                    yield keys.junkTallyKey;
                yield keys.treasureTallyKey;
            }
            case ENCHANTED_BOOK -> {
                EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) caught.getItemMeta();
                if (storageMeta.getStoredEnchantLevel(Enchantment.MENDING) == 0)
                    yield keys.treasureTallyKey;
                yield keys.mendingTallyKey;
            }
            default -> keys.otherTallyKey;
        };
        if (key != keys.fishTallyKey && caughtMeta.hasDisplayName())
            key = keys.treasureTallyKey;
        return key;
    }

    private List<Component> updateLore(ItemMeta meta, PersistentDataContainer container) {
        List<Component> lore = meta.lore();
        List<Component> newLore = new ArrayList<>();
        int index = 4;
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            String ownerName = container.get(keys.ownerKey, PersistentDataType.STRING);
            newLore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
            index++;
        }
        int fishAmount = container.getOrDefault(keys.fishTallyKey, PersistentDataType.INTEGER, 0);
        int treasureAmount = container.getOrDefault(keys.treasureTallyKey, PersistentDataType.INTEGER, 0);
        int mendingAmount = container.getOrDefault(keys.mendingTallyKey, PersistentDataType.INTEGER, 0);
        int junkAmount = container.getOrDefault(keys.junkTallyKey, PersistentDataType.INTEGER, 0);
        newLore.addAll(List.of(Component.text("Fish: " + fishAmount, infoColour).decoration(TextDecoration.ITALIC, false),
                Component.text("Treasures: " + treasureAmount, infoColour).decoration(TextDecoration.ITALIC, false),
                Component.text("Mending books: " + mendingAmount, infoColour).decoration(TextDecoration.ITALIC, false),
                Component.text("Junk: " + junkAmount, infoColour).decoration(TextDecoration.ITALIC, false)));
        int otherAmount = container.getOrDefault(keys.otherTallyKey, PersistentDataType.INTEGER, 0);
        if (otherAmount > 0) {
            newLore.add(Component.text("Other: " +  otherAmount, infoColour).decoration(TextDecoration.ITALIC, false));
            index++;
        }

        if (lore == null)
            newLore.addAll(List.of(Component.text("Just a trusty fishing rod with a"),
                    Component.text("notebook to tally your catches.")));
        else {
            List<Component> subList = lore.subList(index, lore.size());
            newLore.addAll(subList);
        }
        return newLore;
    }
}
