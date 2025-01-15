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
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class CopperOxidationSolution extends Trinket {

    public CopperOxidationSolution(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "copperOxidationSolution", Power.OXIDIZE_COPPER, false, Usages.INVENTORY);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.MUNDANE));
        meta.displayName(Component.text("Copper Oxidation Solution", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("A vinegar and salt solution."),
                Component.text("You estimate there's enough for"),
                Component.text("around 64 blocks of copper.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack oxidize(ItemStack copperBlock) {
        Material material = copperBlock.getType();
        Material resultingMaterial = switch (material) {
            case COPPER_BLOCK -> Material.EXPOSED_COPPER;
            case EXPOSED_COPPER -> Material.WEATHERED_COPPER;
            case WEATHERED_COPPER -> Material.OXIDIZED_COPPER;
            case CUT_COPPER -> Material.EXPOSED_CUT_COPPER;
            case EXPOSED_CUT_COPPER -> Material.WEATHERED_CUT_COPPER;
            case WEATHERED_CUT_COPPER -> Material.OXIDIZED_CUT_COPPER;
            case CUT_COPPER_SLAB -> Material.EXPOSED_CUT_COPPER_SLAB;
            case EXPOSED_CUT_COPPER_SLAB -> Material.WEATHERED_CUT_COPPER_SLAB;
            case WEATHERED_CUT_COPPER_SLAB -> Material.OXIDIZED_CUT_COPPER_SLAB;
            case CUT_COPPER_STAIRS -> Material.EXPOSED_CUT_COPPER_STAIRS;
            case EXPOSED_CUT_COPPER_STAIRS -> Material.WEATHERED_CUT_COPPER_STAIRS;
            case WEATHERED_CUT_COPPER_STAIRS -> Material.OXIDIZED_CUT_COPPER_STAIRS;
            default -> null;
        };
        if (resultingMaterial == null)
            return null;
        copperBlock.setType(resultingMaterial);
        return copperBlock;
    }
}
