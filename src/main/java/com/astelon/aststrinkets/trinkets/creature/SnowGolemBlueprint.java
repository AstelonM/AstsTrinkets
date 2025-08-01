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

public class SnowGolemBlueprint extends GolemBlueprintTrinket {

    public SnowGolemBlueprint(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "snowGolemBlueprint", TextColor.fromHexString("#18AD99"), Power.BUILD_SNOW_GOLEM_FROM_INVENTORY,
                false, Usages.RIGHT_CLICK_ON_BLOCK_WITH_SPACE);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Snow Golem Blueprint", TextColor.fromHexString("#007AD1")));
        meta.lore(List.of(Component.text("A list of detailed instructions"),
                Component.text("on how to build a snowman."),
                Component.text("Now with less than 100 steps!")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Collects the item stacks that are required for the golem in an array.
     * @param player the Player who supplies the materials
     * @return an ItemStack array with 3 elements where the first two are ItemStacks containing snow (possibly the same one)
     * and the last one contains the head material, or null if not enough materials are available
     */
    @Override
    public ItemStack[] getMaterials(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] materials = new ItemStack[3];
        int snow = 0;
        boolean pumpkin = false;
        for (ItemStack item: inventory.getStorageContents()) {
            //TODO do I avoid materials that are modified? Custom names, lores etc.
            if (item != null) {
                if (snow < 2 && item.getType() == Material.SNOW_BLOCK) {
                    materials[snow] = item;
                    snow++;
                    if (snow < 2 && item.getAmount() > 1) {
                        materials[snow] = item;
                        snow++;
                    }
                }
                if (!pumpkin && getAllowedHeads().contains(item.getType())) {
                    materials[2] = item;
                    pumpkin = true;
                }
                if (snow >= 2 && pumpkin)
                    return materials;
            }
        }
        return null;
    }
}
