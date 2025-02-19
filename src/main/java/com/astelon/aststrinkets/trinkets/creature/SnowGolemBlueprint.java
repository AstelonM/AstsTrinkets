package com.astelon.aststrinkets.trinkets.creature;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Set;

public class SnowGolemBlueprint extends Trinket {

    private Set<Material> allowedHeads;

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
                if (!pumpkin && allowedHeads.contains(item.getType())) {
                    materials[2] = item;
                    pumpkin = true;
                }
                if (snow >= 2 && pumpkin)
                    return materials;
            }
        }
        return null;
    }

    //TODO generalize for all trinkets probably
    public int getCooldown(ItemStack blueprint) {
        ItemMeta meta = blueprint.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.getOrDefault(keys.cooldownKey, PersistentDataType.INTEGER, -1);
    }

    public Set<Material> getAllowedHeads() {
        return allowedHeads;
    }

    public void setAllowedHeads(Set<Material> allowedHeads) {
        this.allowedHeads = allowedHeads;
    }
}
