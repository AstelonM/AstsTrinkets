package com.astelon.aststrinkets.trinkets.creature;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class IronGolemBlueprint extends GolemBlueprintTrinket {

    public IronGolemBlueprint(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "ironGolemBlueprint", TextColor.fromHexString("#18AD99"), Power.BUILD_SNOW_GOLEM_FROM_INVENTORY,
                false, Usages.RIGHT_CLICK_ON_BLOCK_WITH_SPACE);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Iron Golem Blueprint", TextColor.fromHexString("#007AD1")));
        meta.lore(List.of(Component.text("A successor to the critically"),
                Component.text("acclaimed Snow Golem Blueprint, the"),
                Component.text("Iron Golem Blueprint provides you"),
                Component.text("with a step by step guide on"),
                Component.text("building your very own iron golem.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Collects the item stacks that are required for the golem in an array.
     * @param player the Player who supplies the materials
     * @return an ItemStack array with 5 elements where the first four are ItemStacks containing iron (possibly the same one)
     * and the last one contains the head material, or null if not enough materials are available
     */
    @Override
    public ItemStack[] getMaterials(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] materials = new ItemStack[5];
        int iron = 0;
        boolean pumpkin = false;
        for (ItemStack item: inventory.getStorageContents()) {
            //TODO do I avoid materials that are modified? Custom names, lores etc.
            if (item != null) {
                if (iron < 4 && item.getType() == Material.IRON_BLOCK) {
                    for (int i = item.getAmount(); i > 0 && iron < 4; i--) {
                        materials[iron] = item;
                        iron++;
                    }
                }
                if (!pumpkin && getAllowedHeads().contains(item.getType())) {
                    materials[4] = item;
                    pumpkin = true;
                }
                if (iron >= 4 && pumpkin)
                    return materials;
            }
        }
        return null;
    }
}
