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

import java.util.ArrayList;
import java.util.List;

public abstract class Trinket {

    protected final AstsTrinkets plugin;
    protected final NamespacedKeys keys;

    protected final String name;
    protected Power power;
    protected final boolean op;
    protected boolean enabled;

    protected ItemStack itemStack;
    protected Component trinketName;
    protected List<Component> lore;
    protected TextColor primaryInfoColour;
    protected TextColor secondaryInfoColour;

    protected final Component usage;

    public Trinket(AstsTrinkets plugin, NamespacedKeys keys, String name, Power power, boolean op, String usage,
                   Component trinketName, List<Component> lore, TextColor primaryInfoColour, TextColor secondaryInfoColour) {
        this.plugin = plugin;
        this.keys = keys;
        this.name = name;
        this.power = power;
        this.op = op;

        this.trinketName = trinketName;
        this.lore = lore;
        if (primaryInfoColour == null)
            this.primaryInfoColour = TextColor.fromHexString("#4AF626");
        else
            this.primaryInfoColour = primaryInfoColour;
        if (secondaryInfoColour == null)
            this.secondaryInfoColour = primaryInfoColour;
        else
            this.secondaryInfoColour = secondaryInfoColour;

        itemStack = createItemStack();
        addTrinketData(this.itemStack);
        customizeTrinket(itemStack);

        this.usage = MiniMessage.miniMessage().deserialize("<gold>How to use: <trinketname></gold><br><trinketusage>",
                Placeholder.component("trinketname", this.itemStack.displayName().hoverEvent(this.itemStack.asHoverEvent())),
                Placeholder.parsed("trinketusage", "<green>" + usage));
    }

    protected abstract ItemStack createItemStack();

    protected void addTrinketData(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(this.keys.nameKey, PersistentDataType.STRING, name);
        container.set(this.keys.powerKey, PersistentDataType.STRING, power.powerName());
        itemStack.setItemMeta(meta);
    }

    protected void customizeTrinket(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(getTrinketName(itemStack).decoration(TextDecoration.ITALIC, false));
        /* TODO check if above is enough
        Component displayName = meta.displayName();
        if (displayName != null)
            meta.displayName(displayName.decoration(TextDecoration.ITALIC, false));

         */
        ArrayList<Component> newLore = new ArrayList<>(lore.size());
        newLore.addAll(getBaseInfo(itemStack));
        newLore.addAll(getTrinketInfo(itemStack));
        newLore.addAll(getTrinketLore(itemStack));
        meta.lore(newLore);
        itemStack.setItemMeta(meta);
    }

    protected Component getTrinketName(ItemStack itemStack) {
        return trinketName;
    }

    protected List<Component> getBaseInfo(ItemStack itemStack) {
        ArrayList<Component> result = new ArrayList<>(0);
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if (container.has(keys.ownerKey, PersistentDataType.STRING))
            result.add(Component.text("Owner: ", primaryInfoColour).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(name, secondaryInfoColour)));
        return result;
    }

    protected List<Component> getTrinketInfo(ItemStack itemStack) {
        return List.of();
    }

    protected List<Component> getTrinketLore(ItemStack itemStack) {
        return lore;
    }

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

    public TextColor getPrimaryInfoColour() {
        return primaryInfoColour;
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
