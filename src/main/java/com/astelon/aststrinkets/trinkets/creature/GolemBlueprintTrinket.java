package com.astelon.aststrinkets.trinkets.creature;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;

public abstract class GolemBlueprintTrinket extends Trinket {

    private Set<Material> allowedHeads;

    public GolemBlueprintTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour, Power power,
                                 boolean op, String usage) {
        super(plugin, keys, name, infoColour, power, op, usage);
    }

    public GolemBlueprintTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, Power power, boolean op, String usage) {
        super(plugin, keys, name, power, op, usage);
    }

    public abstract ItemStack[] getMaterials(Player player);

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
