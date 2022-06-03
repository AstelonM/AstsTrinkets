package com.astelon.aststrinkets.managers;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.trinkets.*;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TrinketManager {

    private final AstsTrinkets plugin;
    private final InvisibilityManager invisibilityManager;

    private final NamespacedKey nameKey;
    private final NamespacedKey powerKey;
    private final NamespacedKey ownerKey;

    private final ArrayList<Trinket> trinkets;
    private final HashMap<String, Trinket> trinketMap;

    public TrinketManager(AstsTrinkets plugin, InvisibilityManager invisibilityManager) {
        this.plugin = plugin;
        this.invisibilityManager = invisibilityManager;
        nameKey = new NamespacedKey(plugin, "trinketName");
        powerKey = new NamespacedKey(plugin, "trinketPower");
        ownerKey = new NamespacedKey(plugin, "trinketOwner");
        trinkets = new ArrayList<>();
        trinketMap = new HashMap<>();
        initTrinkets();
    }

    private void initTrinkets() {
        addTrinket(new InvisibilityTunic(plugin, nameKey, powerKey));
        addTrinket(new TrueInvisibilityTunic(plugin, nameKey, powerKey));
        addTrinket(new FragileInvisibilityTunic(plugin, nameKey, powerKey));
        addTrinket(new ShapeShifter(plugin, nameKey, powerKey));
        addTrinket(new MysteryCake(plugin, nameKey, powerKey));
        addTrinket(new Spinneret(plugin, nameKey, powerKey));
        addTrinket(new MendingPowder(plugin, nameKey, powerKey));
    }

    private void addTrinket(Trinket trinket) {
        trinkets.add(trinket);
        trinketMap.put(trinket.getName(), trinket);
    }

    public ArrayList<Trinket> getTrinkets() {
        return trinkets;
    }

    @Nullable
    public Trinket getTrinket(String name) {
        for (Trinket trinket: trinkets) {
            if (trinket.getName().equalsIgnoreCase(name))
                return trinket;
        }
        return null;
    }

    @NotNull
    public Trinket getTrinketExact(String name) {
        Trinket trinket = trinketMap.get(name);
        if (trinket == null)
            throw new IllegalArgumentException("There is no trinket called " + name + ".");
        return trinket;
    }

    @Nullable
    public Trinket getTrinket(ItemStack itemStack) {
        if (itemStack == null)
            return null;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(nameKey, PersistentDataType.STRING))
            return null;
        String name = container.get(nameKey, PersistentDataType.STRING);
        return trinketMap.get(name);
    }

    public void removePlayerTrinkets(Player player) {
        Inventory inventory = player.getInventory();
        ArrayList<Integer> slotsToRemove = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            for (Trinket trinket: trinkets) {
                if (trinket.isTrinket(inventory.getItem(i)))
                    slotsToRemove.add(i);
            }
        }
        for (int slot: slotsToRemove)
            inventory.setItem(slot, null);
        invisibilityManager.removeInvisiblePlayer(player);
        invisibilityManager.removeTrulyInvisiblePlayer(player);
    }

    @Nullable
    public String getOwner(ItemStack itemStack) {
        if (itemStack == null)
            return null;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ownerKey, PersistentDataType.STRING))
            return null;
        return container.get(ownerKey, PersistentDataType.STRING);
    }

    public boolean isOwnedBy(ItemStack itemStack, String playerName) {
        return playerName.equals(getOwner(itemStack));
    }

    public FragileInvisibilityTunic getFragileInvisibilityTunic() {
        return (FragileInvisibilityTunic) getTrinketExact("fragileInvisibilityTunic");
    }

    public InvisibilityTunic getInvisibilityTunic() {
        return (InvisibilityTunic) getTrinketExact("invisibilityTunic");
    }

    public TrueInvisibilityTunic getTrueInvisibilityTunic() {
        return (TrueInvisibilityTunic) getTrinketExact("trueInvisibilityTunic");
    }

    public ShapeShifter getShapeShifter() {
        return (ShapeShifter) getTrinketExact("shapeShifter");
    }

    public MysteryCake getMysteryCake() {
        return (MysteryCake) getTrinketExact("mysteryCake");
    }

    public Spinneret getSpinneret() {
        return (Spinneret) getTrinketExact("spinneret");
    }

    public MendingPowder getMendingPowder() {
        return (MendingPowder) getTrinketExact("mendingPowder");
    }
}
