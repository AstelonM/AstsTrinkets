package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class Trinket {

    protected final AstsTrinkets plugin;
    protected final NamespacedKeys keys;
    protected final String name;
    protected final TextColor infoColour;
    protected final ItemStack itemStack;
    protected Power power;
    protected boolean enabled;
    protected final boolean op;
    protected final Component usage;

    public Trinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour, Power power, boolean op,
                   String usage) {
        this.plugin = plugin;
        this.keys = keys;
        this.name = name;
        if (infoColour == null)
            this.infoColour = TextColor.fromHexString("#4AF626");
        else
            this.infoColour = infoColour;
        this.power = power;
        this.itemStack = createItemStack();
        this.op = op;
        ItemMeta meta = itemStack.getItemMeta();
        Component displayName = meta.displayName();
        if (displayName != null)
            meta.displayName(displayName.decoration(TextDecoration.ITALIC, false));
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(this.keys.nameKey, PersistentDataType.STRING, name);
        container.set(this.keys.powerKey, PersistentDataType.STRING, power.powerName());
        itemStack.setItemMeta(meta);
        this.usage = MiniMessage.miniMessage().deserialize("<gold>How to use: <trinketname></gold><br><trinketusage>",
                Placeholder.component("trinketname", this.itemStack.displayName().hoverEvent(this.itemStack.asHoverEvent())),
                Placeholder.parsed("trinketusage", "<green>" + usage));
    }

    public Trinket(AstsTrinkets plugin, NamespacedKeys keys, String name, Power power, boolean op, String usage) {
        this(plugin, keys, name, null, power, op, usage);
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
        if (container.has(keys.nameKey, PersistentDataType.STRING)) {
            String trinketName = container.get(keys.nameKey, PersistentDataType.STRING);
            return name.equals(trinketName);
        }
        return false;
    }

    /**
     * Checks whether this trinket is enabled, and the given {@code ItemStack} is an instance of it.
     * @param itemStack the {@code ItemStack} to check
     * @return {@code true} if this trinket is enabled and the given {@code ItemStack} is an instance of it, {@code false} otherwise
     */
    public boolean isEnabledTrinket(ItemStack itemStack) {
        if (enabled)
            return isTrinket(itemStack);
        return false;
    }

    /**
     * Checks whether this trinket is enabled, and the given {@code Projectile} is an instance of it.
     * @param projectile the {@code Projectile} to check
     * @return {@code true} if this trinket is enabled and the given {@code Projectile} is an instance of it, {@code false} otherwise
     */
    public boolean isEnabledTrinket(Projectile projectile) {
        if (enabled)
            return isTrinket(projectile);
        return false;
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
        return op;
    }

    public Component getUsage() {
        return usage;
    }
}
