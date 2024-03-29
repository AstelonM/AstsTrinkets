package com.astelon.aststrinkets.trinkets.block;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class InfinityItem extends Trinket {

    private final ArrayList<Material> allowedBlocks;

    public InfinityItem(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "infinityItem", TextColor.fromHexString("#C8C8C8"), Power.INFINITE_PLACED_BLOCKS,
                false, Usages.INVENTORY_AND_PLACE);
        allowedBlocks = new ArrayList<>();
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.CLAY_BALL);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("The Infinity Item", NamedTextColor.GRAY));
        meta.lore(List.of(Component.text("Applying it to certain blocks lets"),
                Component.text("you place infinite replicas of them.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean hasBlock(ItemStack infinityItem) {
        return itemStack.getType() != infinityItem.getType();
    }

    public boolean isAllowedBlock(Material block, Player player) {
        return allowedBlocks.contains(block) || player.hasPermission("aststrinkets.trinket.unboundinfinityitem");
    }

    public ItemStack replicateBlock(ItemStack infinityItem, Material block) {
        ItemStack result = infinityItem.asOne();
        result.setType(block);
        ItemMeta meta = result.getItemMeta();
        meta.displayName(Component.text("Infinite " + Utils.getBlockName(block), NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false));
        ArrayList<Component> newLore = new ArrayList<>();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            List<Component> oldLore = meta.lore();
            if (oldLore != null && oldLore.size() >= 1)
                newLore.add(oldLore.get(0));
        }
        newLore.add(Component.text("This block has been replicated"));
        newLore.add(Component.text("using the Infinity Item. Every"));
        newLore.add(Component.text("time it is placed, a replica of it"));
        newLore.add(Component.text("will be created instead."));
        meta.lore(newLore);
        result.setItemMeta(meta);
        return result;
    }

    public void setAllowedBlocks(List<Material> allowedBlocks) {
        this.allowedBlocks.clear();
        this.allowedBlocks.addAll(allowedBlocks);
    }
}
