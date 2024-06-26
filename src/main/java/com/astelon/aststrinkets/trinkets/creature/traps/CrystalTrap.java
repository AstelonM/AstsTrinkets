package com.astelon.aststrinkets.trinkets.creature.traps;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.trinkets.inventory.BindingPowder;
import com.astelon.aststrinkets.trinkets.creature.CreatureAffectingTrinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class CrystalTrap extends CreatureAffectingTrinket {

    protected final MobInfoManager mobInfoManager;
    protected final TextColor nameColour;
    protected final ArrayList<Class<? extends LivingEntity>> trappableMobs;
    protected final ArrayList<Class<? extends LivingEntity>> untrappableMobs;

    public CrystalTrap(AstsTrinkets plugin, MobInfoManager mobInfoManager, NamespacedKeys keys, String name,
                       Power power, boolean op, TextColor nameColour, TextColor infoColour, String usage) {
        super(plugin, keys, name, infoColour, power, op, usage);
        this.mobInfoManager = mobInfoManager;
        this.nameColour = nameColour;
        trappableMobs = new ArrayList<>();
        untrappableMobs = new ArrayList<>();
        setMobs();
    }

    public CrystalTrap(AstsTrinkets plugin, MobInfoManager mobInfoManager, NamespacedKeys keys, String name,
                       Power power, boolean op, TextColor nameColour, String usage) {
        this(plugin, mobInfoManager, keys, name, power, op,
                nameColour, null, usage);
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
        if (container.has(keys.trapKey, PersistentDataType.BYTE_ARRAY))
            return null;
        String name = mobInfoManager.getTypeAndName(entity);
        meta.displayName(Component.text("Crystal Trap with " + name, nameColour)
                .decoration(TextDecoration.ITALIC, false));
        ArrayList<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            String ownerName = container.get(keys.ownerKey, PersistentDataType.STRING);
            newLore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
        }
        ArrayList<String> mobText = new ArrayList<>();
        mobText.add(mobInfoManager.getMobType(entity) + " info:");
        mobText.addAll(mobInfoManager.getExtraInfo(entity));
        newLore.addAll(mobText.stream().map(line -> Component.text(line, infoColour).decoration(TextDecoration.ITALIC, false)).toList());
        newLore.addAll(this.itemStack.lore());
        meta.lore(newLore);

        @SuppressWarnings("deprecation") // No better way to do it yet
        byte[] serialized = Bukkit.getUnsafe().serializeEntity(entity);
        container.set(keys.trapKey, PersistentDataType.BYTE_ARRAY, serialized);
        newItem.setItemMeta(meta);
        return newItem;
    }

    public boolean hasTrappedCreature(ItemStack trap) {
        if (!isTrinket(trap))
            return false;
        PersistentDataContainer container = trap.getItemMeta().getPersistentDataContainer();
        return container.has(keys.trapKey, PersistentDataType.BYTE_ARRAY);
    }

    public Entity getTrappedCreature(ItemStack trap, World world) {
        if (!isTrinket(trap))
            throw new IllegalArgumentException("Not a trinket");
        PersistentDataContainer container = trap.getItemMeta().getPersistentDataContainer();
        if (container.has(keys.trapKey, PersistentDataType.BYTE_ARRAY)) {
            byte[] serialized = container.get(keys.trapKey, PersistentDataType.BYTE_ARRAY);
            return Utils.deserializeEntity(plugin, serialized, world);
        }
        return null;
    }
}
