package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class UniversalFertilizer extends Trinket {

    private boolean dispenserAllowed;

    public UniversalFertilizer(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "universalFertilizer", TextColor.fromHexString("#0000FF"), Power.DUPLICATE_PLANTS, false,
                Usages.INTERACT_PLANT);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.CYAN_DYE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Universal Fertilizer", TextColor.fromHexString("#00FFFF")));
        meta.lore(List.of(Component.text("A special formula that works with"),
                Component.text("almost any plant.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack getPlant(Material material) {
        Material result = switch (material) {
            case BIG_DRIPLEAF_STEM -> Material.BIG_DRIPLEAF;
            case KELP_PLANT -> Material.KELP;
            case CARROTS -> Material.CARROT;
            case POTATOES -> Material.POTATO;
            case BEETROOTS -> Material.BEETROOT;
            case MELON_STEM -> Material.MELON;
            case PUMPKIN_STEM -> Material.PUMPKIN;
            case COCOA -> Material.COCOA_BEANS;
            case WEEPING_VINES_PLANT -> Material.WEEPING_VINES;
            case TWISTING_VINES_PLANT -> Material.TWISTING_VINES;
            case CHORUS_PLANT -> Material.CHORUS_FRUIT;
            case SWEET_BERRY_BUSH -> Material.SWEET_BERRIES;
            case CAVE_VINES, CAVE_VINES_PLANT -> Material.GLOW_BERRIES;
            case GRASS, TALL_GRASS, SEAGRASS, FERN, LARGE_FERN, BROWN_MUSHROOM, RED_MUSHROOM, SMALL_DRIPLEAF, DEAD_BUSH,
                 VINE, KELP, SUGAR_CANE, WHEAT, CACTUS, CRIMSON_FUNGUS, WARPED_FUNGUS, CRIMSON_ROOTS, WARPED_ROOTS, NETHER_SPROUTS,
                 NETHER_WART, WEEPING_VINES, TWISTING_VINES, CHORUS_FLOWER, SEA_PICKLE, LILY_PAD, BAMBOO, MOSS_CARPET,
                 GLOW_LICHEN -> material;
            default -> null;
        };
        if (result == null && Tag.FLOWERS.isTagged(material)) {
            result = material;
        }
        if (result == null)
            return null;
        return new ItemStack(result);
    }

    public boolean isDispenserAllowed() {
        return dispenserAllowed;
    }

    public void setDispenserAllowed(boolean dispenserAllowed) {
        this.dispenserAllowed = dispenserAllowed;
    }
}
