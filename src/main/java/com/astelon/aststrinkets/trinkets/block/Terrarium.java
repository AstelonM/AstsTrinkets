package com.astelon.aststrinkets.trinkets.block;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.trinkets.creature.CreatureAffectingTrinket;
import com.astelon.aststrinkets.trinkets.inventory.BindingPowder;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Terrarium extends CreatureAffectingTrinket {

    private final MobInfoManager mobInfoManager;
    private boolean allowEnderDragonCapture;

    public Terrarium(AstsTrinkets plugin, NamespacedKeys keys, MobInfoManager mobInfoManager) {
        super(plugin, keys, "terrarium", Power.CREATE_SPAWNERS, true, Usages.TERRARIUM);
        this.mobInfoManager = mobInfoManager;
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.GLASS);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Empty Terrarium", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("The top is actually a door you can"),
                Component.text("open. Thanks to dwarven ingenuity,"),
                Component.text("it can fit creatures of any size.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean canUse(ItemStack terrarium) {
        PersistentDataContainer container = terrarium.getItemMeta().getPersistentDataContainer();
        long lastUse = container.getOrDefault(keys.lastUseKey, PersistentDataType.LONG, 0L);
        return System.currentTimeMillis() - lastUse >= 1000;
    }

    public boolean canTrap(LivingEntity entity) {
        if (entity instanceof Mob) {
            if (entity instanceof EnderDragon) {
                return allowEnderDragonCapture;
            }
            return true;
        }
        return false;
    }

    public boolean hasTrappedCreature(ItemStack terrarium) {
        PersistentDataContainer container = terrarium.getItemMeta().getPersistentDataContainer();
        return container.has(keys.trapKey, PersistentDataType.BYTE_ARRAY);
    }

    public ItemStack trapCreature(ItemStack terrarium, Entity entity) {
        ItemStack newItem = terrarium.asOne();
        ItemMeta meta = newItem.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(keys.trapKey, PersistentDataType.BYTE_ARRAY))
            return null;
        meta.displayName(Component.text("Filled Terrarium", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        ArrayList<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            String ownerName = container.get(keys.ownerKey, PersistentDataType.STRING);
            newLore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
        }
        newLore.add(getCreatureLine(entity));
        newLore.addAll(this.itemStack.lore());
        meta.lore(newLore);
        @SuppressWarnings("deprecation") // No better way to do it yet
        byte[] serialized = Bukkit.getUnsafe().serializeEntity(entity);
        container.set(keys.trapKey, PersistentDataType.BYTE_ARRAY, serialized);
        container.set(keys.lastUseKey, PersistentDataType.LONG, System.currentTimeMillis());
        newItem.setItemMeta(meta);
        return newItem;
    }

    public Entity getTrappedCreature(ItemStack terrarium, World world) {
        PersistentDataContainer container = terrarium.getItemMeta().getPersistentDataContainer();
        if (container.has(keys.trapKey, PersistentDataType.BYTE_ARRAY)) {
            byte[] serialized = container.get(keys.trapKey, PersistentDataType.BYTE_ARRAY);
            return Utils.deserializeEntity(plugin, serialized, world);
        }
        return null;
    }

    public ItemStack emptyTerrarium(ItemStack terrarium) {
        ItemStack result = terrarium.asOne();
        ItemMeta meta = result.getItemMeta();
        meta.displayName(Component.text("Empty Terrarium", NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false));
        PersistentDataContainer container = meta.getPersistentDataContainer();
        ArrayList<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            String ownerName = container.get(keys.ownerKey, PersistentDataType.STRING);
            newLore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
        }
        if (container.has(keys.trapKey, PersistentDataType.BYTE_ARRAY))
            container.remove(keys.trapKey);
        newLore.addAll(this.itemStack.lore());
        meta.lore(newLore);
        container.set(keys.lastUseKey, PersistentDataType.LONG, System.currentTimeMillis());
        result.setItemMeta(meta);
        return result;
    }

    public boolean isLocked(ItemStack terrarium) {
        PersistentDataContainer container = terrarium.getItemMeta().getPersistentDataContainer();
        return container.has(keys.lockedKey, PersistentDataType.BYTE);
    }

    public ItemStack lock(ItemStack terrarium, World world) {
        ItemStack result = terrarium.asOne();
        ItemMeta meta = result.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(keys.trapKey, PersistentDataType.BYTE_ARRAY) || container.has(keys.lockedKey, PersistentDataType.BYTE))
            return null;
        container.set(keys.lockedKey, PersistentDataType.BYTE, (byte) 1);
        meta.displayName(Component.text("Locked Terrarium", NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false));
        ArrayList<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            String ownerName = container.get(keys.ownerKey, PersistentDataType.STRING);
            newLore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
        }
        Entity entity = getTrappedCreature(terrarium, world);
        if (entity == null)
            return null;
        newLore.add(getCreatureLine(entity));
        newLore.add(Component.text("Locked", this.infoColour).decoration(TextDecoration.ITALIC, false));
        newLore.addAll(this.itemStack.lore());
        newLore.add(Component.text("This terrarium has been locked. The"));
        newLore.add(Component.text("creature inside cannot be freed"));
        newLore.add(Component.text("without breaking it."));
        meta.lore(newLore);
        container.set(keys.lastUseKey, PersistentDataType.LONG, System.currentTimeMillis());
        result.setItemMeta(meta);
        return result;
    }

    private Component getCreatureLine(Entity entity) {
        String mobType = mobInfoManager.getMobType(entity);
        return Component.text("Creature trapped: " + mobType, infoColour).decoration(TextDecoration.ITALIC, false);
    }

    public void setAllowEnderDragonCapture(boolean allowEnderDragonCapture) {
        this.allowEnderDragonCapture = allowEnderDragonCapture;
    }
}
