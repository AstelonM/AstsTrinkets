package com.astelon.aststrinkets.trinkets.block;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.inventory.BindingPowder;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class GatewayAnchor extends Trinket {

    public GatewayAnchor(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "gatewayAnchor", TextColor.fromHexString("#EBEB6A"), Power.CREATE_GATEWAYS,
                true, Usages.LINK_AND_PLACE);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.RESPAWN_ANCHOR);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Gateway Anchor", TextColor.fromHexString("#E048DE")));
        meta.lore(List.of(Component.text("This mysterious device is able to"),
                Component.text("create a gateway to a specific"),
                Component.text("location once placed down.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean hasLocation(ItemStack anchor) {
        PersistentDataContainer container = anchor.getItemMeta().getPersistentDataContainer();
        return container.has(keys.locationKey, PersistentDataType.STRING);
    }

    public Location getLocation(ItemStack anchor) {
        PersistentDataContainer container = anchor.getItemMeta().getPersistentDataContainer();
        String serializedLocation = container.get(keys.locationKey, PersistentDataType.STRING);
        if (serializedLocation == null)
            return null;
        return Utils.deserializeCoords(serializedLocation);
    }

    public ItemStack setLocation(ItemStack anchor, Location location) {
        ItemStack result = anchor.asOne();
        ItemMeta meta = result.getItemMeta();
        String serializedLocation = Utils.serializeCoords(location);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.locationKey, PersistentDataType.STRING, serializedLocation);
        ArrayList<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            String ownerName = container.get(keys.ownerKey, PersistentDataType.STRING);
            newLore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
        }
        newLore.add(Component.text("Location: " + serializedLocation, this.infoColour).decoration(TextDecoration.ITALIC, false));
        newLore.addAll(this.itemStack.lore());
        meta.lore(newLore);
        result.setItemMeta(meta);
        return result;
    }
}
