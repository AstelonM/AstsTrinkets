package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class CrystalTrap extends PetCheckingTrinket {

    protected final NamespacedKey trapKey;
    protected final TextColor nameColour;
    protected final ArrayList<Class<? extends LivingEntity>> trappableMobs;
    protected final ArrayList<Class<? extends LivingEntity>> untrappableMobs;

    public CrystalTrap(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey, NamespacedKey trapKey,
                       String name, Power power, boolean isOp, TextColor nameColour) {
        super(plugin, nameKey, powerKey, name, power, isOp);
        this.trapKey = trapKey;
        this.nameColour = nameColour;
        trappableMobs = new ArrayList<>();
        untrappableMobs = new ArrayList<>();
        setMobs();
    }

    protected abstract void setMobs();

    public boolean isAllowedMob(Entity entity) {
        for (Class<? extends LivingEntity> entityClass: untrappableMobs)
            if (entityClass.isInstance(entity))
                return false;
        for (Class<? extends LivingEntity> entityClass: trappableMobs)
            if (entityClass.isInstance(entity))
                return true;
        return false;
    }

    @Nullable
    public ItemStack trapCreature(ItemStack trap, Entity entity) {
        if (!isTrinket(trap))
            throw new IllegalArgumentException("Not a trinket");
        ItemStack newItem = trap.asOne();
        ItemMeta meta = newItem.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(trapKey, PersistentDataType.BYTE_ARRAY))
            return null;
        String name = Utils.getMobNameOrType(entity);
        meta.displayName(Component.text("Crystal Trap with " + name, nameColour));

        ArrayList<Component> newLore = new ArrayList<>();
        if (meta.lore() != null)
            newLore.addAll(meta.lore());
        String loreText = "This one contains: " + Utils.getMobType(entity.getType());
        Component entityName = entity.customName();
        if (entityName != null)
            loreText += " named " + PlainTextComponentSerializer.plainText().serialize(entityName);
        newLore.add(Component.text(loreText));
        meta.lore(newLore);

        @SuppressWarnings("deprecation") // No better way to do it yet
        byte[] serialized = Bukkit.getUnsafe().serializeEntity(entity);
        container.set(trapKey, PersistentDataType.BYTE_ARRAY, serialized);
        newItem.setItemMeta(meta);
        return newItem;
    }

    public boolean hasTrappedCreature(ItemStack trap) {
        if (!isTrinket(trap))
            return false;
        PersistentDataContainer container = trap.getItemMeta().getPersistentDataContainer();
        return container.has(trapKey, PersistentDataType.BYTE_ARRAY);
    }

    public Entity freeCreature(ItemStack trap, World world) {
        if (!isTrinket(trap))
            throw new IllegalArgumentException("Not a trinket");
        PersistentDataContainer container = trap.getItemMeta().getPersistentDataContainer();
        if (container.has(trapKey, PersistentDataType.BYTE_ARRAY)) {
            byte[] serialized = container.get(trapKey, PersistentDataType.BYTE_ARRAY);
            // No better way to do it yet
            //noinspection deprecation
            return Bukkit.getUnsafe().deserializeEntity(serialized, world);
        }
        return null;
    }
}
