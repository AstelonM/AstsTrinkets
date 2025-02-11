package com.astelon.aststrinkets.api;

import com.astelon.aststrinkets.managers.MobInfoManager;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public class TrinketApi {

    private final TrinketManager trinketManager;
    private final NamespacedKeys keys;
    private final MobInfoManager mobInfoManager;

    public TrinketApi(TrinketManager trinketManager, NamespacedKeys keys, MobInfoManager mobInfoManager) {
        this.trinketManager = trinketManager;
        this.keys = keys;
        this.mobInfoManager = mobInfoManager;
    }

    @Nullable
    public Trinket getTrinket(String name) {
        return trinketManager.getTrinket(name);
    }

    public MobInfoManager getMobInfoManager() {
        return mobInfoManager;
    }

    public NamespacedKeys getKeys() {
        return keys;
    }

    public Object getKeyValue(ItemStack itemStack, NamespacedKey key) {
        NamespacedKeys.KeyTypePair keyType = keys.getKeyTypePair(key.getKey());
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.get(key, keyType.type());
    }

    public void setKeyValue(ItemStack itemStack, NamespacedKey key, Object value) {
        NamespacedKeys.KeyTypePair keyType = keys.getKeyTypePair(key.getKey());
        PersistentDataType<?, ?> type = keyType.type();
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        try {
            if (type.equals(PersistentDataType.STRING))
                container.set(key, PersistentDataType.STRING, (String) value);
            else if (type.equals(PersistentDataType.BYTE_ARRAY))
                container.set(key, PersistentDataType.BYTE_ARRAY, (byte[]) value);
            else if (type.equals(PersistentDataType.DOUBLE))
                container.set(key, PersistentDataType.DOUBLE, (double) value);
            else if (type.equals(PersistentDataType.LONG))
                container.set(key, PersistentDataType.LONG, (long) value);
            else if (type.equals(PersistentDataType.INTEGER))
                container.set(key, PersistentDataType.INTEGER, (int) value);
            else if (type.equals(PersistentDataType.BYTE))
                container.set(key, PersistentDataType.BYTE, (byte) value);
            else if (type.equals(PersistentDataType.FLOAT))
                container.set(key, PersistentDataType.FLOAT, (float) value);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        itemStack.setItemMeta(meta);
    }

    public boolean isTrinketImmune(ItemStack itemStack) {
        return trinketManager.isTrinketImmune(itemStack);
    }

    public void makeTrinketImmune(ItemStack itemStack) {
        trinketManager.makeTrinketImmune(itemStack);
    }

    public void removeTrinketImmunity(ItemStack itemStack) {
        trinketManager.removeTrinketImmunity(itemStack);
    }

    public boolean isTrinketImmune(Entity entity) {
        return trinketManager.isTrinketImmune(entity);
    }

    public void makeTrinketImmune(Entity entity) {
        trinketManager.makeTrinketImmune(entity);
    }

    public void removeTrinketImmunity(Entity entity) {
        trinketManager.removeTrinketImmunity(entity);
    }
}
