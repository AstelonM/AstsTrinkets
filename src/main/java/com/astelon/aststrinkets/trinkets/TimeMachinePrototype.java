package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.inventory.BindingPowder;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

public class TimeMachinePrototype extends Trinket {

    public TimeMachinePrototype(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "timeMachinePrototype", TextColor.fromHexString("#3AFC3A"), Power.TELEPORT_BACK_WITHOUT_ITEMS,
                false, Usages.LINK_AND_USE);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.CLOCK);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Time Machine Prototype", NamedTextColor.YELLOW));
        meta.lore(List.of(Component.text("A functional time machine that can"),
                Component.text("send you back in time to a set"),
                Component.text("moment. It only works once though,"),
                Component.text("and you can't bring any item"),
                Component.text("with you.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean hasLocation(ItemStack timeMachine) {
        PersistentDataContainer container = timeMachine.getItemMeta().getPersistentDataContainer();
        return container.has(keys.locationKey, PersistentDataType.STRING);
    }

    public Location getLocation(ItemStack timeMachine) {
        PersistentDataContainer container = timeMachine.getItemMeta().getPersistentDataContainer();
        String serializedLocation = container.get(keys.locationKey, PersistentDataType.STRING);
        if (serializedLocation == null)
            return null;
        return Utils.deserializeCoords(serializedLocation);
    }

    public ItemStack setLocation(ItemStack timeMachine, Location location) {
        ItemStack result = timeMachine.asOne();
        ItemMeta meta = result.getItemMeta();
        String serializedLocation = Utils.serializeCoords(location);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.locationKey, PersistentDataType.STRING, serializedLocation);
        container.set(keys.lastUseKey, PersistentDataType.LONG, System.currentTimeMillis());
        ArrayList<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            String ownerName = container.get(keys.ownerKey, PersistentDataType.STRING);
            newLore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
        }
        newLore.add(Component.text("Location: " + serializedLocation, this.infoColour).decoration(TextDecoration.ITALIC,
                false));
        newLore.addAll(this.itemStack.lore());
        meta.lore(newLore);
        result.setItemMeta(meta);
        return result;
    }
}
