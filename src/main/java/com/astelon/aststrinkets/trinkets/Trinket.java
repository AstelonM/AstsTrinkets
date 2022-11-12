package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class Trinket {

    protected final AstsTrinkets plugin;
    protected final NamespacedKey nameKey;
    protected final NamespacedKey powerKey;
    protected final String name;
    protected final ItemStack itemStack;
    protected Power power;
    protected boolean enabled;
    protected final boolean isOp;

    public Trinket(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, String name, Power power, boolean isOp) {
        this.plugin = plugin;
        this.nameKey = nameKey;
        this.powerKey = powerKey;
        this.name = name;
        this.power = power;
        this.itemStack = createItemStack();
        this.isOp = isOp;
        ItemMeta meta = itemStack.getItemMeta();
        Component displayName = meta.displayName();
        if (displayName != null)
            meta.displayName(displayName.decoration(TextDecoration.ITALIC, false));
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(this.nameKey, PersistentDataType.STRING, name);
        container.set(this.powerKey, PersistentDataType.STRING, power.powerName());
        itemStack.setItemMeta(meta);
    }

    protected abstract ItemStack createItemStack();

    public boolean isTrinket(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(nameKey, PersistentDataType.STRING)) {
            String trinketName = container.get(nameKey, PersistentDataType.STRING);
            return name.equals(trinketName);
        }
        return false;
    }

    public boolean isEnabledTrinket(ItemStack itemStack) {
        if (!enabled)
            return false;
        return isTrinket(itemStack);
    }

    public String getName() {
        return name;
    }

    public ItemStack getItemStack() {
        return new ItemStack(itemStack);
    }

    public Power getPower() {
        return power;
    }

    public void setPower(Power power) {
        this.power = power;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isOp() {
        return isOp;
    }
}
