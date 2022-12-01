package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class Trinket {

    protected final AstsTrinkets plugin;
    protected final NamespacedKey nameKey;
    protected final NamespacedKey powerKey;
    protected final String name;
    protected final TextColor infoColour;
    protected final ItemStack itemStack;
    protected Power power;
    protected boolean enabled;
    protected final boolean isOp;
    protected final Component usage;

    public Trinket(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, String name, TextColor infoColour,
                   Power power, boolean isOp, String usage) {
        this.plugin = plugin;
        this.nameKey = nameKey;
        this.powerKey = powerKey;
        this.name = name;
        if (infoColour == null)
            this.infoColour = TextColor.fromHexString("#4AF626");
        else
            this.infoColour = infoColour;
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
        this.usage = MiniMessage.miniMessage().deserialize("<gold>How to use: <trinketname></gold><br><trinketusage>",
                Placeholder.component("trinketname", this.itemStack.displayName().hoverEvent(this.itemStack.asHoverEvent())),
                Placeholder.parsed("trinketusage", "<green>" + usage));
    }

    public Trinket(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, String name, Power power, boolean isOp,
                   String usage) {
        this(plugin, nameKey, powerKey, name, null, power, isOp, usage);
    }

    protected abstract ItemStack createItemStack();

    public boolean isTrinket(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return isTrinket(container);
    }

    public boolean isTrinket(Projectile projectile) {
        if (projectile == null)
            return false;
        PersistentDataContainer container = projectile.getPersistentDataContainer();
        return isTrinket(container);
    }

    private boolean isTrinket(PersistentDataContainer container) {
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

    public TextColor getInfoColour() {
        return infoColour;
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

    public Component getUsage() {
        return usage;
    }
}
